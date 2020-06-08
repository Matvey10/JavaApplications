package com.company.controllers;

import com.company.Entities.Article;
import com.company.services.ArticleService;
import com.company.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ArticlesController {
    @Autowired
    public Logger logger;
    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;
    @GetMapping("/articles")
    public String showArticlesInfo(Model model){
        return "javaArticles.html";
    }
    @GetMapping("/articles/new")
    public String newArticle(){
        return "newArticle";
    }
    @PostMapping("/articles/new")
    public String addNewArticle(@RequestParam String title, @RequestParam String ref,
                                @RequestParam String description, Model model){
        System.out.println(title + " " + ref + " " + description);
        Article article = new Article();
        article.setArticleTitle(title);
        article.setResourceRef(ref);
        article.setArticleText(description);
        if (articleService.addArticle(article)){
            System.out.println("add article");
            logger.info("adding new article:" + title);
        }
        else {
            System.out.println("can't add article");
            logger.info("can't add new article");
        }
        return "newArticle";
    }
    @GetMapping("articles/allArticles")
    public String allArticles(Model model){
        List<Article> articles = articleService.findAllArticles();
        model.addAttribute("articles", articles);
        return "allArticles";
    }
}
