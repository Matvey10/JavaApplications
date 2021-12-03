package com.maga.saiapp.controller;

import com.maga.saiapp.dto.OperationResult;
import com.maga.saiapp.dto.article.AiAreaDto;
import com.maga.saiapp.dto.article.ArticleDto;
import com.maga.saiapp.dto.article.FindAiAreasResult;
import com.maga.saiapp.dto.article.FindArticleResult;
import com.maga.saiapp.dto.article.FindArticlesSearchFilter;
import com.maga.saiapp.dto.article.SearchArticlesResult;
import com.maga.saiapp.dto.article.SimpleArticle;
import com.maga.saiapp.service.ArticleService;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/rest/article")
@Slf4j
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @PostMapping(value = "/add")
    public ResponseEntity<OperationResult> addArticle(@RequestBody ArticleDto articleDto) {
        OperationResult operationResult = new OperationResult();
        try {
            articleService.addArticle(articleDto);
            operationResult.setErrorCode("0");
            operationResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during article saving", ex);
            operationResult.setErrorCode("1");
            operationResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(operationResult, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<FindArticleResult> find(@RequestParam Long id) {
        FindArticleResult findArticleResult = new FindArticleResult();
        try {
            findArticleResult = articleService.find(id);
            findArticleResult.setErrorCode("0");
            findArticleResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during finding article", ex);
            findArticleResult.setErrorCode("1");
            findArticleResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(findArticleResult, HttpStatus.OK);
    }

    @PostMapping("/find")
    public ResponseEntity<SearchArticlesResult> findAll(@RequestBody FindArticlesSearchFilter filter) {
        SearchArticlesResult searchArticlesResult = new SearchArticlesResult();
        try {
            List<SimpleArticle> articles = articleService.findAll(filter);
            searchArticlesResult.setArticles(articles);
            searchArticlesResult.setErrorCode("0");
            searchArticlesResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during searching articles", ex);
            searchArticlesResult.setErrorCode("1");
            searchArticlesResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(searchArticlesResult, HttpStatus.OK);
    }

    @GetMapping(value = "/aiAreas/{id}")
    public ResponseEntity<FindAiAreasResult> findProperty(@PathVariable Long id) {
        FindAiAreasResult findAiAreasResult = new FindAiAreasResult();
        try {
            AiAreaDto aiAreaDto = articleService.findAiArea(id);
            findAiAreasResult.setAiAreaDtos(List.of(aiAreaDto));
            findAiAreasResult.setErrorCode("0");
            findAiAreasResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during searching aiArea", ex);
            findAiAreasResult.setErrorCode("1");
            findAiAreasResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(findAiAreasResult, HttpStatus.OK);
    }

    @GetMapping(value = "/aiAreas")
    public ResponseEntity<FindAiAreasResult> findProperties() {
        FindAiAreasResult findAiAreasResult = new FindAiAreasResult();
        try {
            List<AiAreaDto> aiAreaDtos = articleService.findAiAreas();
            findAiAreasResult.setAiAreaDtos(aiAreaDtos);
            findAiAreasResult.setErrorCode("0");
            findAiAreasResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during searching aiAreas", ex);
            findAiAreasResult.setErrorCode("1");
            findAiAreasResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(findAiAreasResult, HttpStatus.OK);
    }
}

//добавить статью

//на странице статьи - все связанные разработки + переход на разработку с ее инфой (добавить ее описание)
//+ все связанные aiAreas + переход на ее страницу с описанием

//Поиск
//поиск по названию, по году, по журналу, по AiAreas

//вкладка с AiAreas - там весь список с переходом на страницу AiArea, где ее описание + методы
