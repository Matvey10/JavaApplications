package ru.rbs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.api.client.util.Value;
import com.google.crypto.tink.apps.paymentmethodtoken.PaymentMethodTokenRecipient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.rbs.tokengenerator.web.GooglePayTokenRequest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestMainConfiguration.class)
public class GooglePayTokenGeneratorTest {

    private final String GOOGLE_PAY_URI = "/rest/generateGooglePayPaymentToken.do";
    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGooglePayToken_successful() throws Exception {
        String payload = "{\"gatewayMerchantId\":\"googlePayMerchant\",\"messageExpiration\":\"1907738377032\",\"paymentMethod\":\"CARD\",\"messageId\":\"AH2EjtcHYs1Ye-ZIZuZXd7eNO4QjQfZjBDtP2ti0tob_a5o22lHmGWHsBVYrrSylkFC3ZTsRdvMadQpwOGCIl7XxhTKcfElmgF7UFbcI8CeUZCWRmbTH5s7h69Baqr4FAM735VNThPiP\",\"paymentMethodDetails\":{\"expirationYear\":2019,\"expirationMonth\":12,\"pan\":\"4111111111111111\"}}";
        String recipientId = "gateway:sberbank";

        GooglePayTokenRequest tokenRequest = new GooglePayTokenRequest();
        tokenRequest.setRecipientId(recipientId);
        tokenRequest.setEncryptionPublicKey("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEwUuKx4+NEqDlOuviG6KXscNXMz1cYEzI2/zhp+nI\n" +
                "aUM+x7A40P37Ujeir2YHjLzG/eLINZOx68ALFxgqfpnbyQ==");
        tokenRequest.setSigningPrivateKey("MIICSwIBADCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////wAAAAEAAAAAAAAAAAAA\n" +
                "AAD///////////////8wRAQg/////wAAAAEAAAAAAAAAAAAAAAD///////////////wEIFrGNdiq\n" +
                "OpPns+u9VXaYhrxlHQawzFOw9jvOPD4n0mBLBEEEaxfR8uEsQkf4vOblY6RA8ncDfYEt6zOg9KE5\n" +
                "RdiYwpZP40Li/hp/m47n60p8D54WK84zV2sxXs7LtkBoN79R9QIhAP////8AAAAA//////////+8\n" +
                "5vqtpxeehPO5ysL8YyVRAgEBBIIBVTCCAVECAQEEIBG2OHYDpN6uc08EtlDV5dSkzO15ddn0yVY0\n" +
                "+pYB2cSnoIHjMIHgAgEBMCwGByqGSM49AQECIQD/////AAAAAQAAAAAAAAAAAAAAAP//////////\n" +
                "/////zBEBCD/////AAAAAQAAAAAAAAAAAAAAAP///////////////AQgWsY12Ko6k+ez671VdpiG\n" +
                "vGUdBrDMU7D2O848PifSYEsEQQRrF9Hy4SxCR/i85uVjpEDydwN9gS3rM6D0oTlF2JjClk/jQuL+\n" +
                "Gn+bjufrSnwPnhYrzjNXazFezsu2QGg3v1H1AiEA/////wAAAAD//////////7zm+q2nF56E87nK\n" +
                "wvxjJVECAQGhRANCAATm+4DWLvfspNBN/nUQ/lKrVVyzVWfMKg+WHEuVlMZg6Ov9TPqfZ4Ph2Ksf\n" +
                "EjhqQf5L6r4zlaDdMFy5CcU0w//4");

        MockHttpServletRequestBuilder mockRequestBuilder = getMockRequestInternal(get(GOOGLE_PAY_URI), tokenRequest, payload);
        MvcResult result = mockMvc.perform(mockRequestBuilder).andDo(print()).andReturn();

        String token = new String(Base64.decodeBase64(result.getResponse().getContentAsString()));

        String signingPublicKey = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE5vuA1i737KTQTf51EP5Sq1Vcs1VnzCoPlhxLlZTGYOjr/Uz6n2eD4dirHxI4akH+S+q+M5Wg3TBcuQnFNMP/+A";
        String encryptionPrivateKey = "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg9SF1Oi5gI6sfcZBDj3JhBKyiAvEk\n" +
                "wXS+KTs5iykIGl+hRANCAATBS4rHj40SoOU66+Ibopexw1czPVxgTMjb/OGn6chpQz7HsDjQ/ftS\n" +
                "N6KvZgeMvMb94sg1k7HrwAsXGCp+mdvJ\n";

        PaymentMethodTokenRecipient decryptor = new PaymentMethodTokenRecipient.Builder()
                .addSenderVerifyingKey(signingPublicKey)
                .recipientId(recipientId)
                .addRecipientPrivateKey(encryptionPrivateKey)
                .protocolVersion("ECv1")
                .build();

        String payloadDecrypted = decryptor.unseal(token);
        Assert.assertEquals(payload, payloadDecrypted);
    }

    private <T> MockHttpServletRequestBuilder  getMockRequestInternal(MockHttpServletRequestBuilder mockRequestBuilder, T tokenRequest, String payload) throws IOException {
        mockRequestBuilder.locale(Locale.forLanguageTag("EN"));

        mockRequestBuilder.param("tokenRequestJson", new ObjectMapper().writeValueAsString(tokenRequest));
        mockRequestBuilder.param("payload", payload);

        return mockRequestBuilder;
    }
}
