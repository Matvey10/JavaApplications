package ru.bpc.common.encrypt;

import ru.rbs.commons.encrypt.CryptoService;
import ru.rbs.commons.encrypt.ICryptoServicesProvider;

import java.util.HashMap;
import java.util.Map;
/**
 * copied from ru.bpc.phoenix.common.encrypt.CryptoServicesProvider
 */
public class CryptoServicesProvider implements ICryptoServicesProvider {

    private Map<String, CryptoService> map = new HashMap<>();

    public CryptoServicesProvider(Map<String, CryptoService> cryptoServices) {
        map.putAll(cryptoServices);
    }

    public CryptoServicesProvider() {
    }

    @Override
    public CryptoService get(String name) {
        return map.get(name);
    }
}
