package com.maga.saiapp.service;

import com.maga.saiapp.dao.ArticleDao;
import com.maga.saiapp.data.entity.AiArea;
import com.maga.saiapp.data.entity.Article;
import com.maga.saiapp.data.entity.Author;
import com.maga.saiapp.data.entity.Development;
import com.maga.saiapp.data.entity.Keyword;
import com.maga.saiapp.data.entity.Method;
import com.maga.saiapp.dto.article.AiAreaDto;
import com.maga.saiapp.dto.article.ArticleDto;
import com.maga.saiapp.dto.article.FindArticleResult;
import com.maga.saiapp.dto.article.FindArticlesSearchFilter;
import com.maga.saiapp.dto.article.MethodDto;
import com.maga.saiapp.dto.article.SimpleArticle;
import com.maga.saiapp.dto.article.SimpleDevelopmentDto;
import com.maga.saiapp.dto.author.AuthorDto;
import com.maga.saiapp.exception.NotFoundException;
import com.maga.saiapp.repository.AiAreaRepository;
import com.maga.saiapp.repository.ArticleRepository;
import com.maga.saiapp.repository.AuthorRepository;
import com.maga.saiapp.repository.DevelopmentRepository;
import com.maga.saiapp.repository.KeyWordRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ArticleService {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private AuthorRepository authorRepository;
    @Resource
    private KeyWordRepository keyWordRepository;
    @Resource
    private AiAreaRepository aiAreaRepository;
    @Resource
    private DevelopmentRepository developmentRepository;
    @Resource
    private ArticleDao articleDao;

    @Transactional
    public void addArticle(ArticleDto articleDto) {
        Article article = new Article();
        article.setIssueNumber(articleDto.getIssuerNumber())
            .setMagazine(articleDto.getArticleMagazine())
            .setArticleTitle(articleDto.getArticleTitle())
            .setYear(articleDto.getArticleYear());

        List<Long> authorsIds = Arrays.stream(articleDto.getAuthorsIds().split(","))
            .map(s -> Long.valueOf(s.trim()))
            .collect(Collectors.toList());
        List<Author> authors = authorRepository.findAllByIdIn(authorsIds);
        article.addAuthors(authors);
        authors.forEach(author -> {
            author.addArticle(article);
        });

        List<Keyword> newKeywords = new ArrayList<>();
        List<Keyword> existedKeywords = new ArrayList<>();
        Arrays.stream(articleDto.getKeyWords()
            .split(","))
            .forEach(keyWord -> {
                keyWord = keyWord.trim();
                Optional<Keyword> keyWordEntity = keyWordRepository.findByValue(keyWord);
                if (keyWordEntity.isPresent()) {
                    existedKeywords.add(keyWordEntity.get());
                } else {
                    Keyword newKeyword = new Keyword().setValue(keyWord);
                    Keyword saved = keyWordRepository.save(newKeyword);
                    newKeywords.add(saved);
                }
            });

        // добавили ключевые слова
        List<Keyword> articleKeywords = Stream.concat(existedKeywords.stream(), newKeywords.stream())
            .collect(Collectors.toList());
        article.addKeywords(articleKeywords);

        //добавили AI areas
        List<Long> aiAreasIds = Arrays.stream(articleDto.getAiAreasIds().split(","))
            .map(s -> Long.valueOf(s.trim()))
            .collect(Collectors.toList());
        List<AiArea> aiAreas = aiAreaRepository.findAllByIdIn(aiAreasIds);
        article.addAiAreas(aiAreas);

        //добавили зарегистрированные разработки связанные со статьей
        if (StringUtils.isNotBlank(articleDto.getDevelopmentsIds())) {
            List<Long> developmentIds = Arrays.stream(articleDto.getDevelopmentsIds().split(","))
                .map(s -> Long.valueOf(s.trim()))
                .collect(Collectors.toList());
            List<Development> developments = developmentRepository.findAllByIdIn(developmentIds);
            article.addDevelopments(developments);
        }

        //сохранили статью
        Article saved = articleRepository.save(article);
        log.debug("Article saved: [{}]", saved);
    }

    public FindArticleResult find(Long id) throws NotFoundException {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            throw new NotFoundException("Article not found");
        }
        FindArticleResult findArticleResult = new FindArticleResult();
        List<SimpleDevelopmentDto> developmentDtos = article.getDevelopments().stream()
            .map(development -> {
                SimpleDevelopmentDto developmentDto = new SimpleDevelopmentDto();
                return developmentDto.setId(development.getId())
                    .setDevelopmentName(development.getDevelopmentName())
                    .setDevelopmentType(development.getDevelopmentType().getTypeName())
                    .setDevelopmentYear(development.getYear());
            }).collect(Collectors.toList());
        findArticleResult.setDevelopments(developmentDtos);

        List<AuthorDto> authorDtos = article.getAuthors().stream()
            .map(author -> {
                AuthorDto authorDto = new AuthorDto();
                return authorDto.setId(author.getId())
                    .setName(author.getName())
                    .setSurname(author.getSurname())
                    .setPatronymic(author.getPatronymic())
                    .setSex(author.getSex())
                    .setCountry(author.getCountry());
            }).collect(Collectors.toList());
        findArticleResult.setAuthors(authorDtos);

        List<AiAreaDto> aiAreaDtos = article.getAiAreas().stream()
            .map(aiArea -> {
                List<MethodDto> methods = aiArea.getMethods().stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
                return new AiAreaDto(aiArea.getId(), aiArea.getAreaName(), aiArea.getAreaBranch(), methods);
            })
            .collect(Collectors.toList());
        findArticleResult.setAiAreas(aiAreaDtos);

        findArticleResult.setArticleTitle(article.getArticleTitle());
        findArticleResult.setArticleMagazine(article.getMagazine());
        findArticleResult.setArticleYear(article.getYear());
        findArticleResult.setIssuerNumber(article.getIssueNumber());


        findArticleResult.setKeywords(article.getKeywords().stream().collect(Collectors.toList()));
        return findArticleResult;
    }

    public List<SimpleArticle> findAll(FindArticlesSearchFilter filter) throws NotFoundException {
        List<String> keywords = StringUtils.isBlank(filter.getKeywords()) ?
            null : Arrays.stream(filter.getKeywords().split(",")).map(String::trim).collect(Collectors.toList());
        List<String> aiAreas = StringUtils.isBlank(filter.getAiAreas()) ?
            null : Arrays.stream(filter.getAiAreas().split(",")).map(String::trim).collect(Collectors.toList());
        List<SimpleArticle> articles = articleDao.findAllBy(filter.getArticleTitle(),
            filter.getMagazine(), keywords, aiAreas, filter.getOrderBy());
        if (articles == null) {
            throw new NotFoundException("No suitable articles");
        }
        return articles;
    }

    public List<AiAreaDto> findAiAreas() {
        List<AiAreaDto> aiAreaDtos = new ArrayList<>();
        aiAreaRepository.findAll().forEach(aiArea -> {
            List<MethodDto> methods = aiArea.getMethods().stream()
                .map(this::convert)
                .collect(Collectors.toList());
            AiAreaDto aiAreaDto = new AiAreaDto(aiArea.getId(), aiArea.getAreaName(), aiArea.getAreaBranch(), methods);
            aiAreaDtos.add(aiAreaDto);
        });
        return aiAreaDtos;
    }

    public AiAreaDto findAiArea(Long id) throws NotFoundException {
        AiArea aiArea = aiAreaRepository.findById(id).orElse(null);
        if (aiArea == null) {
            throw new NotFoundException("AI area not found");
        }
        List<MethodDto> methods = aiArea.getMethods().stream()
            .map(this::convert)
            .collect(Collectors.toList());
        return new AiAreaDto(aiArea.getId(), aiArea.getAreaName(), aiArea.getAreaBranch(), methods);
    }

    private MethodDto convert(Method method) {
        return new MethodDto()
            .setId(method.getId())
            .setName(method.getMethodName())
            .setScienceBranch(method.getScienceBranch());
    }
}
