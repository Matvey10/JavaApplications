package ru.rbs.tokengenerator.encoder;

import org.jetbrains.annotations.NotNull;
import ru.bpc.phoenix.model.ApplePay;
import ru.rbs.tokengenerator.applepay.ApplePayCertificateAlgorithm;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentData;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentToken;
import ru.rbs.tokengenerator.exception.ApplePayNotFoundException;

import java.util.List;

public interface ApplePayDataEncoder {

    ApplePayPaymentToken encode(@NotNull List<ApplePay> applePays, @NotNull ApplePayPaymentData applePayPaymentData) throws ApplePayNotFoundException;

    ApplePayCertificateAlgorithm getCertificateType();
}
