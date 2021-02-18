package ru.bpc.common.encrypt;

import org.springframework.beans.factory.annotation.Required;
import ru.rbs.commons.encrypt.CryptoService;
import ru.rbs.commons.encrypt.EncryptingException;
import ru.rbs.commons.encrypt.MessageWithVector;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;

import static java.util.Objects.requireNonNull;
/**
 * copied from ru.bpc.phoenix.common.encrypt.AESCryptoService
 */
public class AESCryptoService implements CryptoService {

    private KeyStore keystore;
    private String keyName;
    private String keyPassword;

    private static final byte[] iv = { 0x0a, 0x0f, 0x02, 0x0a, 0x04, 0x0b, 0x0c, 0x0d, 0x0c, 0x0f, 0x03, 0x06, 0x08, 0x08, 0x09, 0x01 };

    @Override
    public byte[] decryptWithIv(MessageWithVector messageWithVector) throws EncryptingException {
        requireNonNull(messageWithVector);

        try {
            final Key key = keystore.getKey(keyName, keyPassword.toCharArray());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(messageWithVector.getIv());
            cipher.init(Cipher.DECRYPT_MODE, key, ips);
            return cipher.doFinal(messageWithVector.getMessage());

        } catch (Exception e) {
            throw new EncryptingException("Failed to decrypt message.", e);
        }
    }

    public byte[] decrypt(byte[] message) throws EncryptingException {
        requireNonNull(message);

        return decryptWithIv(new MessageWithVector(message, iv));
    }

    @Override
    public MessageWithVector encryptWithIv(byte[] message) throws EncryptingException {
        requireNonNull(message);

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return encrypt(message, iv);
    }

    public byte[] encrypt(byte[] message) throws EncryptingException {
        requireNonNull(message);

        return encrypt(message, iv).getMessage();
    }

    private MessageWithVector encrypt(byte[] message, byte[] iv) throws EncryptingException {
        requireNonNull(message);
        requireNonNull(iv);

        try {
            final Key key = keystore.getKey(keyName, keyPassword.toCharArray());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ips);
            final byte[] crypt = cipher.doFinal(message);
            return new MessageWithVector(crypt, iv);
        } catch (Exception e) {
            throw new EncryptingException("Failed to encrypt message.", e);
        }
    }

    public KeyStore getKeystore() {
        return keystore;
    }

    @Required
    public void setKeystore(KeyStore keystore) {
        this.keystore = keystore;
    }

    public String getKeyName() {
        return keyName;
    }

    @Required
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    @Required
    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }
}
