package ru.rbs.tokengenerator.encoder;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.rbs.tokengenerator.applepay.ApplePayCertificateAlgorithm;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApplePayDataEncoderProvider {

    @Resource
    private List<ApplePayDataEncoder> applePayDataEncoders;

    @Nullable
    public ApplePayDataEncoder get(ApplePayCertificateAlgorithm applePayCertificateAlgorithm) {

        for (ApplePayDataEncoder applePayEncoder : applePayDataEncoders) {
            if (applePayCertificateAlgorithm == applePayEncoder.getCertificateType()) return applePayEncoder;
        }
        throw new IllegalArgumentException("Unsupportable algorithm: " + applePayCertificateAlgorithm);
    }
}
