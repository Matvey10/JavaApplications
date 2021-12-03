package com.maga.saiapp.dao;

import com.maga.saiapp.dto.development.SimpleDevelopment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class DevelopmentDao {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<SimpleDevelopment> findAllBy(String nameFilter, List<Long> saiPropertiesIds, List<Long> technologiesIds) {
        String sql = "select d.id, d.development_name, dt.id as type_id, dt.type_name, d.year from development d " +
            "    join development_type dt on d.development_type_id = dt.id ";
        boolean saiPropertiesPresent = !CollectionUtils.isEmpty(saiPropertiesIds);
        boolean technologiesPresent = !CollectionUtils.isEmpty(technologiesIds);
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (saiPropertiesPresent) {
            sql += "join development_sai_property dsp on d.id = dsp.development_id " +
                " join sai_property sp on sp.id=dsp.sai_property_id ";
        }
        if (technologiesPresent) {
            sql += "join development_technology dtech on d.id = dtech.development_id " +
                " join technology t on dtech.technology_id = t.id ";
        }

        boolean whereAdded = false;

        if (StringUtils.isNotBlank(nameFilter)) {
            sql += "where lower(d.development_name) like :nameFilter ";
            params.addValue("nameFilter", likeEscape(nameFilter));
            whereAdded = true;
        }

        if (saiPropertiesPresent) {
            if (whereAdded) {
                sql += "and sp.id in :saiIds ";
            } else {
                sql += "where sp.id in :saiIds ";
                whereAdded = true;
            }
            params.addValue("saiIds", saiPropertiesIds);
        }

        if (technologiesPresent) {
            if (whereAdded) {
                sql += "and t.id in :techIds ";
            } else {
                sql += "where t.id in :techIds ";
            }
            params.addValue("techIds", technologiesIds);
        }

        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String likeEscape(String s) {
        return "%" + s + "%";
    }

    private static final RowMapper<SimpleDevelopment> rowMapper = ((rs, rowNum) -> {
        SimpleDevelopment development = new SimpleDevelopment();
        development.setId(rs.getLong("id"));
        development.setDevelopmentName(rs.getString("development_name"));
        development.setDevelopmentYear(rs.getInt("year"));
        development.setDevelopmentType(rs.getString("type_name"));
        return development;
    });

}
