package ru.rbs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.crypto.tink.apps.paymentmethodtoken.PaymentMethodTokenRecipient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.rbs.decoders.apple.ApplePayDecoder;
import ru.rbs.decoders.apple.RsaApplePayDecoder;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentData;
import ru.rbs.tokengenerator.exception.ApplePayNotFoundException;
import ru.rbs.tokengenerator.exception.ApplePayPaymentDataValidationException;
import ru.rbs.tokengenerator.exception.MerchantNotFoundException;
import ru.rbs.tokengenerator.exception.RequestParamException;
import ru.rbs.tokengenerator.service.EncodingService;
import ru.rbs.tokengenerator.web.GooglePayTokenRequest;
import ru.rbs.tokengenerator.web.TokenRequest;
import ru.rbs.tokengenerator.web.TokenResponse;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestMainConfiguration.class)
@TestPropertySource(properties = {
        "data.scripts:classpath:data/bpcmerchantdetails.sql,classpath:data/merchant_currency.sql,classpath:data/vendor_pay.sql,classpath:data/apple_pay.sql",
})
public class ApplepayTokenGeneratorApplicationTests {

	private final String PAN = "4111111111111111";
	private final String AMOUNT ="3566";
	private final String CURRENCY = "810";
	private final String CARD_HOLDER_NAME = "IvanovIvan";
	private final String EXPIRY = "191231";
	private final String ALGORITHM_RSA = "RSA_v1";
	private final String ALGORITHM_EC = "EC_v1";
	private final String MERCHANT_LOGIN = "ApplePayTest";
	private final String PAYMENT_TOKEN_EC = "eyJ2ZXJzaW9uIjoiRUNfdjEiLCJzaWduYXR1cmUiOiJNSUFHQ1NxR1NJYjNEUUVIQXFDQU1JQUNBUUV4RHpBTkJnbGdoa2dCWlFNRUFnRUZBRENBQmdrcWhraUc5dzBCQndFQUFLQ0FNSUlENWpDQ0E0dWdBd0lCQWdJSWFHRDJtZG5NcHc4d0NnWUlLb1pJemowRUF3SXdlakV1TUN3R0ExVUVBd3dsUVhCd2JHVWdRWEJ3YkdsallYUnBiMjRnU1c1MFpXZHlZWFJwYjI0Z1EwRWdMU0JITXpFbU1DUUdBMVVFQ3d3ZFFYQndiR1VnUTJWeWRHbG1hV05oZEdsdmJpQkJkWFJvYjNKcGRIa3hFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVE1CNFhEVEUyTURZd016RTRNVFkwTUZvWERUSXhNRFl3TWpFNE1UWTBNRm93WWpFb01DWUdBMVVFQXd3ZlpXTmpMWE50Y0MxaWNtOXJaWEl0YzJsbmJsOVZRelF0VTBGT1JFSlBXREVVTUJJR0ExVUVDd3dMYVU5VElGTjVjM1JsYlhNeEV6QVJCZ05WQkFvTUNrRndjR3hsSUVsdVl5NHhDekFKQmdOVkJBWVRBbFZUTUZrd0V3WUhLb1pJemowQ0FRWUlLb1pJemowREFRY0RRZ0FFZ2pEOXE4T2M5MTRnTEZEWm0wVVM1amZpcVFIZGJMUGdzYzFMVW1lWStNOU92ZWdhSmFqQ0hrd3ozYzZPS3BiQzlxK2hrd05GeE9oNlJDYk9sUnNTbGFPQ0FoRXdnZ0lOTUVVR0NDc0dBUVVGQndFQkJEa3dOekExQmdnckJnRUZCUWN3QVlZcGFIUjBjRG92TDI5amMzQXVZWEJ3YkdVdVkyOXRMMjlqYzNBd05DMWhjSEJzWldGcFkyRXpNREl3SFFZRFZSME9CQllFRkFJa01BdWE3dTFHTVpla3Bsb3Bua0p4Z2h4Rk1Bd0dBMVVkRXdFQi93UUNNQUF3SHdZRFZSMGpCQmd3Rm9BVUkvSkp4RStUNU84bjVzVDJLR3cvb3J2OUxrc3dnZ0VkQmdOVkhTQUVnZ0VVTUlJQkVEQ0NBUXdHQ1NxR1NJYjNZMlFGQVRDQi9qQ0J3d1lJS3dZQkJRVUhBZ0l3Z2JZTWdiTlNaV3hwWVc1alpTQnZiaUIwYUdseklHTmxjblJwWm1sallYUmxJR0o1SUdGdWVTQndZWEowZVNCaGMzTjFiV1Z6SUdGalkyVndkR0Z1WTJVZ2IyWWdkR2hsSUhSb1pXNGdZWEJ3YkdsallXSnNaU0J6ZEdGdVpHRnlaQ0IwWlhKdGN5QmhibVFnWTI5dVpHbDBhVzl1Y3lCdlppQjFjMlVzSUdObGNuUnBabWxqWVhSbElIQnZiR2xqZVNCaGJtUWdZMlZ5ZEdsbWFXTmhkR2x2YmlCd2NtRmpkR2xqWlNCemRHRjBaVzFsYm5SekxqQTJCZ2dyQmdFRkJRY0NBUllxYUhSMGNEb3ZMM2QzZHk1aGNIQnNaUzVqYjIwdlkyVnlkR2xtYVdOaGRHVmhkWFJvYjNKcGRIa3ZNRFFHQTFVZEh3UXRNQ3N3S2FBbm9DV0dJMmgwZEhBNkx5OWpjbXd1WVhCd2JHVXVZMjl0TDJGd2NHeGxZV2xqWVRNdVkzSnNNQTRHQTFVZER3RUIvd1FFQXdJSGdEQVBCZ2txaGtpRzkyTmtCaDBFQWdVQU1Bb0dDQ3FHU000OUJBTUNBMGtBTUVZQ0lRRGFIR091aStYMlQ0NFI2R1ZwTjdtMm5FY3I2VDZzTWpPaFo1TnVTbzFlZ3dJaEFMMWErL2hwODhES0owc3YzZVQzRnhXY3M3MXhtYkxLRC9RSjNtV2FnckpOTUlJQzdqQ0NBbldnQXdJQkFnSUlTVzB2dnpxWTJwY3dDZ1lJS29aSXpqMEVBd0l3WnpFYk1Ca0dBMVVFQXd3U1FYQndiR1VnVW05dmRDQkRRU0F0SUVjek1TWXdKQVlEVlFRTERCMUJjSEJzWlNCRFpYSjBhV1pwWTJGMGFXOXVJRUYxZEdodmNtbDBlVEVUTUJFR0ExVUVDZ3dLUVhCd2JHVWdTVzVqTGpFTE1Ba0dBMVVFQmhNQ1ZWTXdIaGNOTVRRd05UQTJNak0wTmpNd1doY05Namt3TlRBMk1qTTBOak13V2pCNk1TNHdMQVlEVlFRRERDVkJjSEJzWlNCQmNIQnNhV05oZEdsdmJpQkpiblJsWjNKaGRHbHZiaUJEUVNBdElFY3pNU1l3SkFZRFZRUUxEQjFCY0hCc1pTQkRaWEowYVdacFkyRjBhVzl1SUVGMWRHaHZjbWwwZVRFVE1CRUdBMVVFQ2d3S1FYQndiR1VnU1c1akxqRUxNQWtHQTFVRUJoTUNWVk13V1RBVEJnY3Foa2pPUFFJQkJnZ3Foa2pPUFFNQkJ3TkNBQVR3RnhHRUdkZGtoZFVhWGlXQkIzYm9nS0x2M251dVRlQ04vRXVUNFROVzFXWmJOYTRpMEpkMkRTSk9lN29JL1hZWHpvakxkcnRtY0w3STZDbUUvMVJGbzRIM01JSDBNRVlHQ0NzR0FRVUZCd0VCQkRvd09EQTJCZ2dyQmdFRkJRY3dBWVlxYUhSMGNEb3ZMMjlqYzNBdVlYQndiR1V1WTI5dEwyOWpjM0F3TkMxaGNIQnNaWEp2YjNSallXY3pNQjBHQTFVZERnUVdCQlFqOGtuRVQ1UGs3eWZteFBZb2JEK2l1LzB1U3pBUEJnTlZIUk1CQWY4RUJUQURBUUgvTUI4R0ExVWRJd1FZTUJhQUZMdXczcUZZTTRpYXBJcVozcjY5NjYvYXl5U3JNRGNHQTFVZEh3UXdNQzR3TEtBcW9DaUdKbWgwZEhBNkx5OWpjbXd1WVhCd2JHVXVZMjl0TDJGd2NHeGxjbTl2ZEdOaFp6TXVZM0pzTUE0R0ExVWREd0VCL3dRRUF3SUJCakFRQmdvcWhraUc5Mk5rQmdJT0JBSUZBREFLQmdncWhrak9QUVFEQWdObkFEQmtBakE2ejNLRFVSYVpzWWI3TmNOV3ltSy85QmZ0MlE5MVRhS092dkdjZ1Y1Q3Q0bjRtUGViV1orWTFVRU5qNTNwd3Y0Q01ESXQxVVFoc0tNRmQyeGQ4emc3a0dmOUYzd3NJVzJXVDhaeWFZSVNiMVQ0ZW4wYm1jdWJDWWtoWVFhWkR3bVNIUUFBTVlJQlhqQ0NBVm9DQVFFd2dZWXdlakV1TUN3R0ExVUVBd3dsUVhCd2JHVWdRWEJ3YkdsallYUnBiMjRnU1c1MFpXZHlZWFJwYjI0Z1EwRWdMU0JITXpFbU1DUUdBMVVFQ3d3ZFFYQndiR1VnUTJWeWRHbG1hV05oZEdsdmJpQkJkWFJvYjNKcGRIa3hFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVEFnaG9ZUGFaMmN5bkR6QU5CZ2xnaGtnQlpRTUVBZ0VGQUtCcE1CZ0dDU3FHU0liM0RRRUpBekVMQmdrcWhraUc5dzBCQndFd0hBWUpLb1pJaHZjTkFRa0ZNUThYRFRFMk1URXlPREV3TlRReE0xb3dMd1lKS29aSWh2Y05BUWtFTVNJRUlIQy9VUFFxY3M4QSt4MFhIWHhQN043V0xjeGIrNXhaT084dXJ0aEtoOVJMTUFvR0NDcUdTTTQ5QkFNQ0JFWXdSQUlnUFdnRG4rYjF3cCtIYWo1ZHkxQlZPcVM4NWpVUkxUcDY2SFgxa3B1NW13SUNJRnoxeStnWnRsQTN1dEk2SEVVQ2QxMUhRQlhzeHlyQWl2Um5MS1JDaTJJL0FBQUFBQUFBIiwiaGVhZGVyIjp7IndyYXBwZWRLZXkiOm51bGwsImVwaGVtZXJhbFB1YmxpY0tleSI6Ik1Ga3dFd1lIS29aSXpqMENBUVlJS29aSXpqMERBUWNEUWdBRUNMc3RmZUJTVWs3RUVqeFl5cWhHbmFYQWtnMWUwOENqcGFnclZNWHZQZ1orY2lzNGZNMjM0SmtTM3NaTm5HaXBPZFVVMHE0TTRBVmZ2cDg4anF6YklnPT0iLCJwdWJsaWNLZXlIYXNoIjoiT04vaW45eDVjU25lYXpWNnpoQ2tmRFNHbkV5eCt0UGQvZ3N5dnhoc1dVdz0iLCJ0cmFuc2FjdGlvbklkIjoiYTc3Y2I4NDk3Njk5YWY5MWEyM2ZhYzgzY2QyZjBjZTJlZmNhMzk5M2E5MTczZGMzOGRiMzY1YzNlY2ZiMTliZSJ9LCJkYXRhIjoiV2Z6WjB6NzZ2bEtERDdkcGJ3WTFZdjNtUmpEYWN0clN0Z3ZxZFhaYUlLZ2o0ZnpzT3g4STM1Z0t4dE4xK3pZVkpPaExubkxsZHcxUStJNHFNRHBBTW8rYVlsaTVkbkZCTzF6dU4wNU8yN256Z1k4Q3EzbWF6VW5vRkcwUFJFS1Z3ekgxUHU0MFNxUmpBbjg1S2VqV3FKTHVQczZUMGsrREJ0Qm5KNWlRWSszcE9ZMFFBUXRwc0NrNW9JOXRUVmhMaWZPWWVTaStmL2tWc0RnYnZCTFRtZDVhbGUxZlRSUElKdDI5NVhsQVcxMStuV3dBaVNsVGFXUTZ2UlpPckRZVFFCNlNoZ3h5TWlsY0hlYWJJamQ2UjcySlRxdHliOU95UElGNXFQeXNkaVduNytUclphdWhJaXRxdU1NMmZxd0tLUjdNUk8rZ0RyT3VnSG9HWkozbGVqeDgwOW1zbEpzY052SjROaHBUcjJPTHUxNHo5R01rK2FFVXhYWFlSZ0NMbUMzRjJFZEMvRTJyVjZ5Ujkxb1RKRCtIVW0veFRPbkpZcy92SWpHRDFXWWE1NE90Y1YrdnI3ZGFyRzZ5cnNaeHNEZStuSTN3VW9ONW03YnFydmtlbmc9PSJ9";
	private final String URI = "/rest/generateApplePayPaymentToken.do";
	private MockMvc mockMvc;

	@Resource
	private WebApplicationContext context;

	@Autowired
	private RsaApplePayDecoder applePayDecoder;

	@Autowired
	private EncodingService encodingService;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void applePayEncoderTest() throws RequestParamException, ApplePayNotFoundException, ApplePayPaymentDataValidationException, MerchantNotFoundException {
		TokenRequest tokenRequest = generateTokenRequest();
		String paymentToken = encodingService.encodingRequestDataToPaymentToken(tokenRequest);

		ApplePayPaymentData applePayPaymentData = applePayDecoder.decodeToken(paymentToken, MERCHANT_LOGIN);
		Assert.assertNotNull(applePayPaymentData);
	}

	@Test(expected = RequestParamException.class)
	public void validateInCorrectTokenRequestTest() throws RequestParamException {
		TokenRequest tokenRequest = generateTokenRequest();
		tokenRequest.setPan(null);
		tokenRequest.validate();
	}

	@Test
	public void validateCorrectTokenRequestTest() throws RequestParamException {
		generateTokenRequest().validate();
	}

	@Test
	public void getTokenPostMethodEcAlgorithmTest() throws Exception {
		TokenRequest tokenRequest = generateTokenRequest();
		tokenRequest.setAlgorithm(ALGORITHM_EC);

		MockHttpServletRequestBuilder mockRequestBuilder = getPostMockRequest(tokenRequest);
        MvcResult result = mockMvc.perform(mockRequestBuilder).andReturn();

		Assert.assertEquals(getSuccessfulResponseAsString(),result.getResponse().getContentAsString());
	}

    @Test
    public void getTokenGetMethodEcAlgorithmTest() throws Exception {
        TokenRequest tokenRequest = generateTokenRequest();
        tokenRequest.setAlgorithm(ALGORITHM_EC);

        MockHttpServletRequestBuilder mockRequestBuilder = getGetMockRequest(tokenRequest);
        MvcResult result = mockMvc.perform(mockRequestBuilder).andReturn();

        Assert.assertEquals(getSuccessfulResponseAsString(),result.getResponse().getContentAsString());
    }

	private TokenRequest generateTokenRequest() {
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest.setMerchantLogin(MERCHANT_LOGIN);
		tokenRequest.setAlgorithm(ALGORITHM_RSA);
		tokenRequest.setAmount(AMOUNT);
		tokenRequest.setCardHolderName(CARD_HOLDER_NAME);
		tokenRequest.setCurrency(CURRENCY);
		tokenRequest.setPan(PAN);
		tokenRequest.setExpiry(EXPIRY);
		return tokenRequest;
	}

	private String getSuccessfulResponseAsString() throws JsonProcessingException {
        TokenResponse response = new TokenResponse();
        response.setStatus(TokenResponse.SUCCESS_STATUS);
        response.setPaymentToken(PAYMENT_TOKEN_EC);
        TokenResponse.Error error = new TokenResponse.Error();
        error.setErrorCode("0");
        error.setMessage("success");
        error.setDescription("token was successfully generated");
        response.setError(error);

        return new ObjectMapper().writeValueAsString(response);
    }

    private MockHttpServletRequestBuilder getPostMockRequest(TokenRequest tokenRequest) throws Exception {
	    return getMockRequestInternal(post(URI), tokenRequest);
    }

    private MockHttpServletRequestBuilder getGetMockRequest(TokenRequest tokenRequest) throws Exception {
        return getMockRequestInternal(get(URI), tokenRequest);
    }

	private <T> MockHttpServletRequestBuilder  getMockRequestInternal (MockHttpServletRequestBuilder mockRequestBuilder, T tokenRequest) throws IOException {
		mockRequestBuilder.locale(Locale.forLanguageTag("EN"));
		ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> params = objectMapper.readValue(objectMapper.writeValueAsString(tokenRequest), HashMap.class);
        for (String key : params.keySet()) {
            mockRequestBuilder.param(key, params.get(key));
        }

		return mockRequestBuilder;
	}

}
