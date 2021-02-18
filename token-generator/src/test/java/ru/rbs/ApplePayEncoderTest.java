package ru.rbs;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestMainConfiguration.class)
@TestPropertySource(properties = {
        "data.scripts:classpath:data/bpcmerchantdetails.sql,classpath:data/merchant_currency.sql,classpath:data/vendor_pay.sql,classpath:data/apple_pay.sql",
})
public class ApplePayEncoderTest {
    public void encodeApplePayPaymentTokenTest(){

    }
}
