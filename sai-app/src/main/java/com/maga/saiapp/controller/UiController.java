package com.maga.saiapp.controller;

import com.maga.saiapp.data.entity.AiArea;
import com.maga.saiapp.data.entity.DevelopmentType;
import com.maga.saiapp.dto.article.AiAreaDto;
import com.maga.saiapp.dto.article.FindArticleResult;
import com.maga.saiapp.dto.article.FindArticlesSearchFilter;
import com.maga.saiapp.dto.article.SimpleArticle;
import com.maga.saiapp.dto.author.AuthorDto;
import com.maga.saiapp.dto.author.FindAuthorSearchFilter;
import com.maga.saiapp.dto.development.FindDevelopmentResult;
import com.maga.saiapp.dto.development.FindDevelopmentsSearchFilter;
import com.maga.saiapp.dto.development.SaiPropertyDto;
import com.maga.saiapp.dto.development.SimpleDevelopment;
import com.maga.saiapp.dto.development.TechnologyDto;
import com.maga.saiapp.exception.NotFoundException;
import com.maga.saiapp.repository.AiAreaRepository;
import com.maga.saiapp.service.ArticleService;
import com.maga.saiapp.service.AuthorService;
import com.maga.saiapp.service.DevelopmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UiController {
    @Resource
    DevelopmentService developmentService;
    @Resource
    ArticleService articleService;
    @Resource
    AiAreaRepository aiAreaRepository;
    @Resource
    AuthorService authorService;

    @GetMapping("/")
    public String getStartPage() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }

    @RequestMapping(value = "/article/add")
    public String addArticlePage() {
        return "/article/addArticle";
    }

    @RequestMapping(value = "/article/search")
    public String searchArticlesPage(Model model, @ModelAttribute FindArticlesSearchFilter findArticlesSearchFilter) throws NotFoundException {
        model.addAttribute("findArticlesSearchFilter", findArticlesSearchFilter);
        model.addAttribute("orderByParams", List.of("magazine", "year"));
        List<SimpleArticle> articles = articleService.findAll(findArticlesSearchFilter);
        model.addAttribute("articles", articles);
        return "/article/searchArticles";
    }

    @GetMapping(value = "/article/find")
    public String find(Model model, @RequestParam Long id) throws NotFoundException {
        FindArticleResult findArticleResult = articleService.find(id);
        model.addAttribute("findArticleResult", findArticleResult);
        return "/article/article";
    }

    @GetMapping(value = "/article/aiAreas")
    public String find(Model model) {
        List<AiAreaDto> aiAreas = articleService.findAiAreas();
        model.addAttribute("aiAreas", aiAreas);
        return "/article/aiAreas";
    }

    @GetMapping(value = "/article/aiArea/find")
    public String findAiArea(Model model, @RequestParam Long id) {
        AiArea aiArea = aiAreaRepository.findById(id).get();
        model.addAttribute("aiArea", aiArea);
        return "/article/aiArea";
    }

    @RequestMapping(value = "/author/add")
    public String addAuthorPage(Model model) {
        List<String> sexes = List.of("M", "F");
        model.addAttribute("sexes", sexes);
        return "/author/addAuthor";
    }

    @RequestMapping(value = "/author/search")
    public String searchAuthorsPage(Model model, @ModelAttribute FindAuthorSearchFilter findAuthorSearchFilter) throws NotFoundException {
        model.addAttribute("findAuthorSearchFilter", findAuthorSearchFilter);
        List<AuthorDto> authors = authorService.findAll(findAuthorSearchFilter);
        model.addAttribute("authors", authors);
        List<String> countries = authorService.getAllCountries();
        model.addAttribute("countries", countries);
        return "/author/searchAuthors";
    }

    @GetMapping(value = "/author/find")
    public String findAuthor(Model model, @RequestParam Long id) {
        AuthorDto author = authorService.findById(id);
        model.addAttribute("author", author);
        return "/author/author";
    }

    @RequestMapping(value = "/development/add")
    public String addDevelopmentPage(Model model) {
        List<DevelopmentType> developmentTypes = developmentService.getDevelopmentTypes();
        model.addAttribute("developmentTypes", developmentTypes);
        return "/development/addDevelopment";
    }

    @RequestMapping(value = "/development/search")
    public String searchDevelopmentPage(Model model, @ModelAttribute FindDevelopmentsSearchFilter findDevelopmentsSearchFilter) throws NotFoundException {
        model.addAttribute("findDevelopmentsSearchFilter", findDevelopmentsSearchFilter);
        List<SimpleDevelopment> developments = developmentService.findAll(findDevelopmentsSearchFilter);
        model.addAttribute("developments", developments);
        return "/development/searchDevelopments";
    }

    @GetMapping(value = "/development/find")
    public String findDevelopment(Model model, @RequestParam Long id) throws NotFoundException {
        FindDevelopmentResult findDevelopmentResult = developmentService.find(id);
        model.addAttribute("findDevelopmentResult", findDevelopmentResult);
        return "/development/development";
    }

    @GetMapping(value = "/development/saiProperties")
    public String saiPropertiesPage(Model model) {
        List<SaiPropertyDto> saiProperties = developmentService.findSaiProperties();
        model.addAttribute("saiProperties", saiProperties);
        return "/development/saiProperties";
    }

    @GetMapping(value = "/development/technologies")
    public String technologiesPage(Model model) {
        List<TechnologyDto> technologies = developmentService.findTechnologies();
        model.addAttribute("technologies", technologies);
        return "/development/technologies";
    }
}
