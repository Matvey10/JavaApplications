package ru.rbs.decoders.apple;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.bpc.phoenix.model.ApplePay;
import ru.rbs.tokengenerator.applepay.ApplePayCertificateAlgorithm;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentData;
import ru.rbs.tokengenerator.applepay.ApplePayPaymentToken;

import java.util.List;

public interface ApplePayDecoder {

    @Nullable
    ApplePayPaymentData decode(@NotNull List<ApplePay> applePays, @NotNull ApplePayPaymentToken applePayPaymentToken);

    ApplePayCertificateAlgorithm getCertificateType();
}
