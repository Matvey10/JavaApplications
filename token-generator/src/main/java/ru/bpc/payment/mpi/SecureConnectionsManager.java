package ru.bpc.payment.mpi;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import ru.bpc.service.CertificateService;
import ru.rbs.commons.http.HostnameVerifierProvider;
import ru.rbs.commons.http.SSLContextProvider;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
/**
 * copied from ru.bpc.payment.mpi.SecureConnectionsManager
 */
public class SecureConnectionsManager implements SSLContextProvider, HostnameVerifierProvider {
    private static final Logger logger = LoggerFactory.getLogger(SecureConnectionsManager.class);

    private static final HostnameVerifier HOSTNAME_VERIFIER_NO_VERIFY = (s, sslSession) -> true;

    private static final String SIGNATURE_SHA256_ALGO = "SHA256withRSA";

    private Resource keyStoreResource;
    private String password;
    private boolean doVerifyHostname;
    private CertificateService certificateService;
    private KeyStore keystore;

    private TrustManager[] trustManagers;
    private KeyManager[] keyManagers;
    private SSLContext sslContext;

    @Value("${sslHistoryDir}")
    private String sslHistoryDir;

    @Value("${sslUploadDir}")
    private String sslUploadDir;

    private static final Provider BC_PROVIDER = new BouncyCastleProvider();

    static {
        Security.addProvider(BC_PROVIDER);
    }

    public SecureConnectionsManager(Resource keyStore, String password, boolean doVerifyHostname) {
        this.keyStoreResource = keyStore;
        this.password = password;
        this.doVerifyHostname = doVerifyHostname;
        try {
            certificateService = new CertificateService(keyStore, password);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to create certificate service", e);
        }
        load();
    }

    public void load() {
        InputStream stream = null;
        try {
            stream = keyStoreResource.getInputStream();
        } catch (IOException e) {
            logger.error("Cannot load input stream from given resource", e);
        }
        if (stream == null) {
            logger.error("Invalid certificate resource given");
        }

        try {
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(stream, password.toCharArray());
            logger.debug("KeyStore has " + keystore.size() + " elements.");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keystore, password.toCharArray());

            TrustManagerFactory trustManagerFactoryDefault = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactoryDefault.init((KeyStore) null);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);

            trustManagers = new TrustManager[]{
                    new DelegatingTrustManager(
                            trustManagerFactoryDefault.getTrustManagers(),
                            trustManagerFactory.getTrustManagers()
                    )
            };
            keyManagers = new KeyManager[] {
                    new DelegatingKeyManager(kmf.getKeyManagers())
            };

            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(keyManagers, trustManagers, new java.security.SecureRandom());

            // todo выпилить hell
            SSLContext.setDefault(sslContext);
            // todo выпилить hell!!!!
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            if (!doVerifyHostname) {
                // todo выпилить hell!!!!!!!
                HttpsURLConnection.setDefaultHostnameVerifier(HOSTNAME_VERIFIER_NO_VERIFY);
            }

            logger.info("Certificates installed successfully");
        } catch (Exception ex) {
            logger.error("Cannot init keystore", ex);
        }
    }

    @Override
    @NotNull
    public SSLContext getSslContext() {
        SSLContext sslContext = this.sslContext;
        Preconditions.checkState(sslContext != null, "sslContext not initialized");
        return sslContext;
    }

    @Nullable
    @Override
    public HostnameVerifier getHostnameVerifier() {
        if (isDoVerifyHostname()) {
            // todo здесь нужно более сложную логику, но пока этого хватает
            return null;
        }

        return HOSTNAME_VERIFIER_NO_VERIFY;
    }

    public boolean isDoVerifyHostname() {
        return doVerifyHostname;
    }

    @NotNull
    private static X509TrustManager getX509TrustManager(TrustManager... trustManagers) {
        for (TrustManager tm : trustManagers) {
            if (tm instanceof X509TrustManager) {
                return (X509TrustManager) tm;
            }
            logger.debug("Skipping " + tm);
        }
        throw new RuntimeException("Not found X509TrustManager in array " + Arrays.toString(trustManagers));
    }

    class DelegatingTrustManager implements X509TrustManager {

        private final X509TrustManager trustManagerDefault;
        /**
         * The default X509TrustManager returned by SunX509.  We'll delegate
         * decisions to it, and fall back to the logic in this class if the
         * default X509TrustManager doesn't trust it.
         */
        private final X509TrustManager trustManager;

        DelegatingTrustManager(TrustManager[] trustManagersDefault, TrustManager[] trustManagers) {
            this.trustManagerDefault = getX509TrustManager(trustManagersDefault);
            this.trustManager = getX509TrustManager(trustManagers);
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            logger.debug("Check client trusted");
            try {
                trustManager.checkClientTrusted(chain, authType);
            } catch (CertificateException e) {
                try {
                    // default CA check
                    trustManagerDefault.checkClientTrusted(chain, authType);
                } catch (CertificateException e2) {
                    logger.error("Client trust failed", e);
                }
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            logger.debug("Check server trusted");
            try {
                trustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException e) {
                try {
                    // default CA check
                    trustManagerDefault.checkServerTrusted(chain, authType);
                } catch (CertificateException e2) {
                    logger.error("Server check failed", e);
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] certs = trustManager.getAcceptedIssuers();
            X509Certificate[] result = (X509Certificate[]) ArrayUtils.addAll(
                    certs,
                    trustManagerDefault.getAcceptedIssuers()
            );
            // log only explicit jks certs
            for (X509Certificate cert : certs) {
                logger.trace("Accepted issuer: " + cert.getSubjectDN());
            }
            return result;
        }
    }

    @NotNull
    private static X509KeyManager getX509KeyManager(KeyManager... keyManagers) {
        for (KeyManager keyManager : keyManagers) {
            if (keyManager instanceof X509KeyManager) {
                return (X509KeyManager) keyManager;
            }
            logger.debug("Skipping " + keyManager);
        }
        throw new RuntimeException("Not found X509KeyManager in array " + Arrays.toString(keyManagers));
    }

    class DelegatingKeyManager implements X509KeyManager {
        private final X509KeyManager defaultManager;

        public DelegatingKeyManager(KeyManager[] keyManagers) {
            this.defaultManager = getX509KeyManager(keyManagers);
        }

        @Override
        public String[] getClientAliases(String s, Principal[] principals) {
            logger.debug("getClientAliases, s=" + s);
            return defaultManager.getClientAliases(s, principals);
        }

        @Override
        public String chooseClientAlias(String[] strings, Principal[] principals, Socket socket) {
            logger.trace("chooseClientAlias for principals, see below");
            for (Principal pr : principals) {
                logger.trace("Principal: " + pr.getName());
            }
            String result = defaultManager.chooseClientAlias(strings, principals, socket);
            logger.trace("chooseClientAlias result: " + result);
            return result;
        }

        @Override
        public String[] getServerAliases(String s, Principal[] principals) {
            logger.debug("getServerAliases");
            return defaultManager.getServerAliases(s, principals);
        }

        @Override
        public String chooseServerAlias(String s, Principal[] principals, Socket socket) {
            logger.debug("chooseServerAlias");
            return defaultManager.chooseServerAlias(s, principals, socket);
        }

        @Override
        public X509Certificate[] getCertificateChain(String s) {
            logger.trace("getCertificateChain for " + s);
            return defaultManager.getCertificateChain(s);
        }

        @Override
        public PrivateKey getPrivateKey(String s) {
            logger.trace("Get certificate chain for " + s);
            return defaultManager.getPrivateKey(s);
        }
    }

    public byte[] signData(byte[] data, String keyAlias) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, InvalidKeyException, SignatureException {
        PrivateKey privateKey = (PrivateKey) keystore.getKey(keyAlias, password.toCharArray());
        Signature signature = Signature.getInstance(SIGNATURE_SHA256_ALGO, BC_PROVIDER);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }
}
