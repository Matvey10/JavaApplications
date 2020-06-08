package com.company.Repositories;

import com.company.Entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
     Article findByArticleTitle(String title);
     List<Article> findByArticleTextContaining(String regex);
}
