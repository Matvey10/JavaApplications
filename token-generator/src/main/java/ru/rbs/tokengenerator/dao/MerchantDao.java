package ru.rbs.tokengenerator.dao;

import org.jetbrains.annotations.Nullable;
import ru.rbs.tokengenerator.exception.MerchantNotFoundException;
import ru.rbs.tokengenerator.model.SimpleCurrency;
import ru.rbs.tokengenerator.model.SimpleMerchant;

public interface MerchantDao {
    SimpleMerchant findByMerchantLogin(String merchantLogin) throws MerchantNotFoundException;
    @Nullable
    SimpleCurrency getDefaultCurrencyByMerchantId(Long merchantId);
}
