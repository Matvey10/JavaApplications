package ru.rbs.tokengenerator.dao;

import ru.bpc.phoenix.model.SamsungPay;

import java.util.List;

public interface SamsungPayDao {
    List<SamsungPay> findByMerchantId(long merchantId);
    void update(SamsungPay samsungPay);
}
