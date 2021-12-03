package com.company;

import com.company.Entities.Article;
import com.company.services.ArticleService;
import com.company.services.UserService;
import com.company.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class Application {
    ArticleService articleService;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}