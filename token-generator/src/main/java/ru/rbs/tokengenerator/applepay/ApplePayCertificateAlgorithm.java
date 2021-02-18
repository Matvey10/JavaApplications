package ru.rbs.tokengenerator.applepay;

public enum ApplePayCertificateAlgorithm {
    RSA_v1("RSA"),
    EC_v1("EC");

    private final String algorithm;

    ApplePayCertificateAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
