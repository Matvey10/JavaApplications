package ru.bpc.common.encrypt;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;

import java.security.KeyStore;
/**
 * copied from ru.bpc.phoenix.common.encrypt.JCEKeystoreFactory
 */
public class JCEKeystoreFactory extends AbstractFactoryBean<KeyStore> {

    private Resource keystoreFile;
    private String password;

    @Override
    public Class<?> getObjectType() {
        return KeyStore.class;
    }

    @Override
    public KeyStore createInstance() throws Exception {
        KeyStore keystore = KeyStore.getInstance("jceks");
        keystore.load(keystoreFile.getInputStream(), password.toCharArray());
        return keystore;
    }

    public Resource getKeystoreFile() {
        return keystoreFile;
    }

    @Required
    public void setKeystoreFile(Resource keystoreFile) {
        this.keystoreFile = keystoreFile;
    }

    public String getPassword() {
        return password;
    }

    @Required
    public void setPassword(String password) {
        this.password = password;
    }
}
