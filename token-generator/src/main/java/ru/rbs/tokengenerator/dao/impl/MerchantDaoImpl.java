package ru.rbs.tokengenerator.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.rbs.tokengenerator.exception.MerchantNotFoundException;
import ru.rbs.tokengenerator.model.SimpleCurrency;
import ru.rbs.tokengenerator.model.SimpleMerchant;
import ru.rbs.tokengenerator.dao.MerchantDao;

import javax.annotation.Resource;

@Repository
@Slf4j
public class MerchantDaoImpl implements MerchantDao {

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public SimpleMerchant findByMerchantLogin(String merchantLogin) throws MerchantNotFoundException {
        try {
            SimpleMerchant simpleMerchant = namedParameterJdbcTemplate.queryForObject(
                    "SELECT M.id as merchantId, M.merchantLogin " +
                            "FROM bpcmerchantdetails M " +
                            "WHERE M.merchantLogin = :merchantLogin",
                    new MapSqlParameterSource().addValue("merchantLogin", merchantLogin),
                    SIMPLE_MERCHANT_ROW_MAPPER
            );
            simpleMerchant.setDefaultCurrency(getDefaultCurrencyByMerchantId(simpleMerchant.getMerchantId()));
            return simpleMerchant;
        } catch (EmptyResultDataAccessException e) {
            throw new MerchantNotFoundException(String.format("Merchant [%s] not found", merchantLogin));
        }
    }

    @Override
    @Nullable
    public SimpleCurrency getDefaultCurrencyByMerchantId(Long merchantId) {
        try {
            SimpleCurrency currency = namedParameterJdbcTemplate.queryForObject(
                    "SELECT C.numeric_code as currency, C.is_default as isDefault " +
                            "FROM merchant_currency C " +
                            "WHERE C.merchant_id = :merchantId AND C.is_default = :isDefault",
                    new MapSqlParameterSource()
                            .addValue("merchantId", merchantId)
                            .addValue("isDefault", true),
                    SIMPLE_CURRENCY_ROW_MAPPER
            );
            return currency;
        } catch (EmptyResultDataAccessException e) {
            //log.info("Default currency for merchant [{}] not found", merchantId);
            return null;
        }
    }


    private static final RowMapper<SimpleMerchant> SIMPLE_MERCHANT_ROW_MAPPER = (rs, rowNum) -> {
        SimpleMerchant simpleMerchant = new SimpleMerchant();

        simpleMerchant.setMerchantId(rs.getLong("merchantId"));
        simpleMerchant.setMerchantLogin(rs.getString("merchantLogin"));

        return simpleMerchant;
    };

    private static final RowMapper<SimpleCurrency> SIMPLE_CURRENCY_ROW_MAPPER = (rs, rowNum) -> {
        SimpleCurrency simpleCurrency = new SimpleCurrency();

        simpleCurrency.setCurrency(rs.getString("currency"));
        simpleCurrency.setDefault(rs.getBoolean("isDefault"));

        return simpleCurrency;
    };
}
