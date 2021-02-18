package ru.rbs.tokengenerator.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import ru.bpc.common.encrypt.AESCryptoService;
import ru.bpc.common.encrypt.CryptoServicesProvider;
import ru.bpc.common.encrypt.JCEKeystoreFactory;
import ru.bpc.payment.mpi.SecureConnectionsManager;
import ru.rbs.commons.encrypt.CryptoService;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class SecureConfig {

    @Value("${sslKeyStoreFile}")
    private Resource sslKeyStoreFile;
    @Value("${sslKeyStorePassword}")
    private String sslKeyStorePassword;
    @Value("${ssl.hostname.verify}")
    private Boolean sslHostnameVerify;
    @Value("${sslHistoryDir}")
    private String sslHistoryDir;
    @Value("${sslUploadDir}")
    private String sslUploadDir;
    @Value("${secureKeystoreFile}")
    private Resource secureKeystoreFile;
    @Value("${secureKeystorePassword}")
    private String secureKeystorePassword;
    @Value("${systemSettingsCryptKeyPassword}")
    private String systemSettingsCryptKeyPassword;
    @Value("${secureKeyName}")
    private String secureKeyName;

    @Bean
    public SecureConnectionsManager getSecureConnectionsManager() {
        return new SecureConnectionsManager(sslKeyStoreFile, sslKeyStorePassword, sslHostnameVerify);
    }

    @Bean
    public CryptoServicesProvider getCryptoServicesProvider() {
        Map<String, CryptoService> cryptoServices = new HashMap<>();
        cryptoServices.put("AES", getAesCryptoService());
        return new CryptoServicesProvider(cryptoServices);
    }

    @Bean
    public AESCryptoService getAesCryptoService() {
        AESCryptoService aesCryptoService = new AESCryptoService();
        aesCryptoService.setKeyName(secureKeyName);
        aesCryptoService.setKeyPassword(systemSettingsCryptKeyPassword);
        try {
            aesCryptoService.setKeystore(getJceKeystoreFactory().createInstance());
        } catch (Exception e) {
            //log.error("Bean AesCryptoService was'n created because: {}", e.getMessage());
        }

        return aesCryptoService;
    }

    @Bean
    public JCEKeystoreFactory getJceKeystoreFactory() {
        JCEKeystoreFactory jceKeystoreFactory = new JCEKeystoreFactory();
        jceKeystoreFactory.setKeystoreFile(secureKeystoreFile);
        jceKeystoreFactory.setPassword(secureKeystorePassword);
        return jceKeystoreFactory;
    }
}
