package com.company.Repositories;

import com.company.Entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    public Article findByArticleTitle(String title);
}
