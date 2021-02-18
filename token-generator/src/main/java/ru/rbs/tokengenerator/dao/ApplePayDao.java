package ru.rbs.tokengenerator.dao;


import ru.bpc.phoenix.model.ApplePay;

import java.util.List;

public interface ApplePayDao {
    List<ApplePay> findByMerchantId(long merchantId);
    void update(ApplePay applePay);
}
