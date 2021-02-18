package ru.rbs.tokengenerator.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.bpc.phoenix.model.ApplePay;
import ru.rbs.tokengenerator.dao.ApplePayDao;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ApplePayDaoImpl implements ApplePayDao{

    private final static Logger logger = LoggerFactory.getLogger(ApplePayDaoImpl.class);

    private static String APPLE_VENDOR = "APPLE";

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    @Transactional
    public List<ApplePay> findByMerchantId(long merchantId) {
       return namedParameterJdbcTemplate.query(
                "SELECT * FROM apple_pay AP WHERE AP.id IN " +
                "(SELECT VP.id FROM vendor_pay VP WHERE VP.merchant_id = :merchantId AND VP.vendor = :vendor AND VP.enabled = :enabled)",

                new MapSqlParameterSource()
                        .addValue("merchantId", merchantId)
                        .addValue("vendor", APPLE_VENDOR)
                        .addValue("enabled", true),

                APPLE_PAY_ROW_MAPPER

        );
    }

    @Override
    @Transactional
    public void update(ApplePay applePay) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", applePay.getId())
                .addValue("algorithm", applePay.getAlgorithm())
                .addValue("publicKey", applePay.getPublicKey())
                .addValue("publicKeyHash", applePay.getPublicKeyHash())
                .addValue("privateKey", applePay.getPrivateKey())
                .addValue("merchantAppleId", applePay.getMerchantAppleId())
                .addValue("csr", applePay.getCsr());
        int status = namedParameterJdbcTemplate.update(
                "UPDATE apple_pay " +
                        "SET public_key = :publicKey, private_key = :privateKey, " +
                        "csr = :csr, algorithm = :algorithm, merchant_apple_id = :merchantAppleId, public_key_hash = :publicKeyHash " +
                        "WHERE id = :id",
                namedParameters);

        if (status != 0) {
            logger.debug("ApplePay with ID [{}] updated", applePay.getId());
        } else {
            logger.debug("ApplePay with ID [{}] is not updated", applePay.getId());
        }
    }

    private static final RowMapper<ApplePay> APPLE_PAY_ROW_MAPPER = (rs, rowNum) -> {
        ApplePay applePay = new ApplePay();
        applePay.setId(rs.getLong("id"));
        applePay.setAlgorithm(rs.getString("algorithm"));
        applePay.setPublicKey(rs.getString("public_key"));
        applePay.setPublicKeyHash(rs.getString("public_key_hash"));
        applePay.setPrivateKey(rs.getString("private_key"));
        applePay.setMerchantAppleId(rs.getString("merchant_apple_id"));
        applePay.setCsr(rs.getString("csr"));
        return applePay;
    };
}
