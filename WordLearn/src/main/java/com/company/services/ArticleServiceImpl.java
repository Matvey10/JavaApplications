package com.company.services;

import com.company.Entities.Article;
import com.company.Repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{
    @Autowired
    ArticleRepository articleRepository;
    public boolean addArticle(Article article){
        articleRepository.save(article);
        return true;
    };
    public boolean removeArticleById(Integer id){
        articleRepository.deleteById(id);
        return true;
    };
    public List<Article> findAllArticles(){
        return articleRepository.findAll();
    };
    public Article findArticleByTitle(String title){
        return articleRepository.findByArticleTitle(title);
    };
    public List<Article> findByKeywords(List<String> keywords){
        List<Article> articles = new ArrayList<>();
        return articles;
    };
}
