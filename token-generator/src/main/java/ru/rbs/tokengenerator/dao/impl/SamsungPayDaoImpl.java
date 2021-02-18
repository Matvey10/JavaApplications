package ru.rbs.tokengenerator.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.bpc.phoenix.model.SamsungPay;
import ru.rbs.tokengenerator.dao.SamsungPayDao;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class SamsungPayDaoImpl implements SamsungPayDao {
    private static String SAMSUNG_VENDOR = "SAMSUNG";

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<SamsungPay> findByMerchantId(long merchantId) {
        return namedParameterJdbcTemplate.query("SELECT * FROM samsung_pay SP WHERE SP.id IN " +
                "(SELECT VP.id FROM vendor_pay VP WHERE VP.merchant_id = :merchantId AND VP.vendor = :vendor AND VP.enabled = :enabled)",
                new MapSqlParameterSource()
                        .addValue("merchantId", merchantId)
                        .addValue("vendor", SAMSUNG_VENDOR)
                        .addValue("enabled", true),
                SAMSUNG_PAY_ROW_MAPPER);
    }

    @Override
    public void update(SamsungPay samsungPay) {
    }

    private static final RowMapper<SamsungPay> SAMSUNG_PAY_ROW_MAPPER = (rs, rowNum) -> {
        SamsungPay samsungPay = new SamsungPay();
        samsungPay.setId(rs.getLong("id"));
        samsungPay.setCsr(rs.getString("csr"));
        samsungPay.setPrivateKey(rs.getString("private_key"));
        samsungPay.setPublicKey(rs.getString("public_key"));
        return  samsungPay;
    };

}
