package com.maga.saiapp.dao;

import com.maga.saiapp.dto.article.SimpleArticle;
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
public class ArticleDao {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<SimpleArticle> findAllBy(String articleTitleFilter, String articleMagazineFilter, List<String> keywords,
                                         List<String> aiAreas, String orderBy) {
        String sql = "select a.id, a.article_title, a.magazine, a.issue_number, a.year from article a ";
        boolean keywordsPresent = !CollectionUtils.isEmpty(keywords);
        boolean aiAreasPresent = !CollectionUtils.isEmpty(aiAreas);
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (keywordsPresent) {
            sql += "join article_keyword ak on ak.article_id=a.id " +
                "join keyword k on ak.keyword_id=k.id ";
        }
        if (aiAreasPresent) {
            sql += "join article_ai_area aaa on aaa.article_id=a.id " +
                "join ai_area aa on aa.id=aaa.ai_area_id ";
        }

        boolean whereAdded = false;

        if (StringUtils.isNotBlank(articleTitleFilter)) {
            sql += "where lower(a.article_title) like :articleTitleFilter ";
            params.addValue("articleTitleFilter", likeEscape(articleTitleFilter));
            whereAdded = true;
        }

        if (StringUtils.isNotBlank(articleMagazineFilter)) {
            if (whereAdded) {
                sql += "and lower(a.magazine) like :magazine ";
            } else {
                sql += "where lower(a.magazine) like :magazine ";
                whereAdded = true;
            }
            params.addValue("magazine", likeEscape(articleMagazineFilter));
        }

        if (keywordsPresent) {
            if (whereAdded) {
                sql += "and k.value in :keywords ";
            } else {
                sql += "where k.value in :keywords ";
                whereAdded = true;
            }
            params.addValue("keywords", keywords);
        }

        if (aiAreasPresent) {
            if (whereAdded) {
                sql += "and aa.id in :aiAreas ";
            } else {
                sql += "where aa.id in :aiAreas ";
            }
            params.addValue("aiAreas", aiAreas);
        }

        if (StringUtils.isNotBlank(orderBy)) {
            if (orderBy.equals("magazine")) {
                sql += "order by magazine ";
            }
            if (orderBy.equals("year")) {
                sql += "order by year ";
            }
        }

        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    private String likeEscape(String s) {
        return "%" + s.toLowerCase() + "%";
    }

    private static final RowMapper<SimpleArticle> rowMapper = ((rs, rowNum) -> {
        SimpleArticle simpleArticle = new SimpleArticle();
        simpleArticle.setId(rs.getLong("id"));
        simpleArticle.setArticleTitle(rs.getString("article_title"));
        simpleArticle.setMagazine(rs.getString("magazine"));
        simpleArticle.setIssueNumber(rs.getInt("issue_number"));
        simpleArticle.setYear(rs.getInt("year"));
        return simpleArticle;
    });

}
