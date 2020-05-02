package com.company.services;

import com.company.Entities.Article;
import com.company.Entities.User;
import com.company.Entities.Word;

import java.util.List;

public interface ArticleService {
    boolean addArticle(Article article);
    boolean removeArticleById(Integer id);
    List<Article> findAllArticles();
    Article findArticleByTitle(String title);
    List<Article> findByKeywords(List<String> keywords);
}
