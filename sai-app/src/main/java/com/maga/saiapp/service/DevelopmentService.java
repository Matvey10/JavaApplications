package com.maga.saiapp.service;

import com.maga.saiapp.dao.DevelopmentDao;
import com.maga.saiapp.data.entity.Article;
import com.maga.saiapp.data.entity.Author;
import com.maga.saiapp.data.entity.Development;
import com.maga.saiapp.data.entity.DevelopmentType;
import com.maga.saiapp.data.entity.SaiProperty;
import com.maga.saiapp.data.entity.Technology;
import com.maga.saiapp.dto.article.ArticleDto;
import com.maga.saiapp.dto.author.AuthorDto;
import com.maga.saiapp.dto.development.DevelopmentDto;
import com.maga.saiapp.dto.development.FindDevelopmentResult;
import com.maga.saiapp.dto.development.FindDevelopmentsSearchFilter;
import com.maga.saiapp.dto.development.SaiPropertyDto;
import com.maga.saiapp.dto.development.SimpleDevelopment;
import com.maga.saiapp.dto.development.TechnologyDto;
import com.maga.saiapp.exception.NotFoundException;
import com.maga.saiapp.repository.ArticleRepository;
import com.maga.saiapp.repository.AuthorRepository;
import com.maga.saiapp.repository.DevelopmentRepository;
import com.maga.saiapp.repository.DevelopmentTypeRepository;
import com.maga.saiapp.repository.SaiPropertyRepository;
import com.maga.saiapp.repository.TechnologyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DevelopmentService {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private AuthorRepository authorRepository;
    @Resource
    private SaiPropertyRepository saiPropertyRepository;
    @Resource
    private TechnologyRepository technologyRepository;
    @Resource
    private DevelopmentRepository developmentRepository;
    @Resource
    private DevelopmentTypeRepository developmentTypeRepository;
    @Resource
    private DevelopmentDao developmentDao;

    @Transactional
    public void addDevelopment(DevelopmentDto developmentDto) {
        DevelopmentType type = developmentTypeRepository.findById(developmentDto.getDevelopmentTypeId()).get();
        Development development = new Development()
            .setDevelopmentName(developmentDto.getDevelopmentName())
            .setDevelopmentType(type)
            .setYear(developmentDto.getDevelopmentYear());

        //добавили авторов
        List<Long> authorsIds = Arrays.stream(developmentDto.getAuthorsIds().split(","))
            .map(s -> Long.valueOf(s.trim()))
            .collect(Collectors.toList());
        List<Author> authors = authorRepository.findAllByIdIn(authorsIds);
        development.addAuthors(authors);
        authors.forEach(author -> {
            author.addDevelopment(development);
        });

        //добавили SaiProperties
        List<Long> saiPropertiesIds = Arrays.stream(developmentDto.getSaiPropertiesIds().split(","))
            .map(s -> Long.valueOf(s.trim()))
            .collect(Collectors.toList());
        List<SaiProperty> saiProperties = saiPropertyRepository.findAllByIdIn(saiPropertiesIds);
        development.addSaiProperties(saiProperties);

        //добавили technologies
        List<Long> technologiesIds = Arrays.stream(developmentDto.getTechnologiesIds().split(","))
            .map(s -> Long.valueOf(s.trim()))
            .collect(Collectors.toList());
        List<Technology> technologies = technologyRepository.findAllByIdIn(technologiesIds);
        development.addTechnologies(technologies);

        //добавили зарегистрированные статьи связанные с разработкой
        if (StringUtils.isNotBlank(developmentDto.getArticleIds())) {
            List<Long> articlesIds = Arrays.stream(developmentDto.getArticleIds().split(","))
                .map(s -> Long.valueOf(s.trim()))
                .collect(Collectors.toList());
            List<Article> articles = articleRepository.findAllByIdIn(articlesIds);
            development.addArticles(articles);
        }

        //сохранили разработку
        Development saved = developmentRepository.save(development);
        log.debug("Development saved: [{}]", saved);
    }

    public FindDevelopmentResult find(Long id) throws NotFoundException {
        Development development = developmentRepository.findById(id).orElse(null);
        if (development == null) {
            throw new NotFoundException("Development not found");
        }
        FindDevelopmentResult findDevelopmentResult = new FindDevelopmentResult();
        List<ArticleDto> articleDtos = development.getArticles().stream()
            .map(article -> {
                ArticleDto articleDto = new ArticleDto();
                return articleDto.setArticleTitle(article.getArticleTitle())
                    .setArticleYear(article.getYear())
                    .setIssuerNumber(article.getIssueNumber())
                    .setArticleMagazine(article.getMagazine());
            }).collect(Collectors.toList());
        findDevelopmentResult.setArticles(articleDtos);

        List<AuthorDto> authorDtos = development.getAuthors().stream()
            .map(author -> {
                AuthorDto authorDto = new AuthorDto();
                return authorDto.setId(author.getId())
                    .setName(author.getName())
                    .setSurname(author.getSurname())
                    .setPatronymic(author.getPatronymic())
                    .setSex(author.getSex())
                    .setCountry(author.getCountry());
            }).collect(Collectors.toList());
        findDevelopmentResult.setAuthors(authorDtos);

        List<SaiPropertyDto> saiPropertyDtos = development.getSaiProperties().stream()
            .map(SaiProperty::getPropertyName)
            .map(SaiPropertyDto::new)
            .collect(Collectors.toList());
        findDevelopmentResult.setSaiProperties(saiPropertyDtos);

        List<TechnologyDto> technologyDtos = development.getTechnologies().stream()
            .map(Technology::getTechnologyName)
            .map(TechnologyDto::new)
            .collect(Collectors.toList());
        findDevelopmentResult.setTechnologies(technologyDtos);

        findDevelopmentResult.setDevelopmentName(development.getDevelopmentName());
        findDevelopmentResult.setDevelopmentYear(development.getYear());
        findDevelopmentResult.setDevelopmentType(development.getDevelopmentType().getTypeName());

        return findDevelopmentResult;
    }

    public List<SimpleDevelopment> findAll(FindDevelopmentsSearchFilter filter) throws NotFoundException {
        List<Long> saiPropertiesIds = null;
        if (filter.getSaiProperties() != null) {
            List<String> saiPropertiesNames = Arrays.stream(filter.getSaiProperties().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
            saiPropertiesIds = saiPropertyRepository.findAllByPropertyNameIn(saiPropertiesNames)
                .stream().map(SaiProperty::getId).collect(Collectors.toList());
        }

        List<Long> technologiesIds = null;
        if (filter.getTechnologies() != null) {
            List<String> technologiesNames = Arrays.stream(filter.getTechnologies().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
            List<Technology> technologies = technologyRepository.findAllByTechnologyNameIn(technologiesNames);
            technologiesIds = technologies.stream().map(Technology::getId).collect(Collectors.toList());
        }

        List<SimpleDevelopment> developments = developmentDao.findAllBy(filter.getNameFilter(),
            saiPropertiesIds, technologiesIds);
        if (developments == null) {
            throw new NotFoundException("No suitable developments");
        }
        return developments;
    }

    public List<SaiPropertyDto> findSaiProperties() {
        List<SaiPropertyDto> saiPropertyDtos = new ArrayList<>();
        saiPropertyRepository.findAll().forEach(saiProperty -> {
            SaiPropertyDto saiPropertyDto = new SaiPropertyDto(saiProperty.getPropertyName());
            saiPropertyDtos.add(saiPropertyDto);
        });
        return saiPropertyDtos;
    }

    public SaiPropertyDto findSaiProperty(Long id) throws NotFoundException {
        SaiProperty saiProperty = saiPropertyRepository.findById(id).orElse(null);
        if (saiProperty == null) {
            throw new NotFoundException("Sai property not found");
        }
        return new SaiPropertyDto(saiProperty.getPropertyName());
    }

    public TechnologyDto findTechnology(Long id) throws NotFoundException {
        Technology technology = technologyRepository.findById(id).orElse(null);
        if (technology == null) {
            throw new NotFoundException("Sai property not found");
        }
        return new TechnologyDto(technology.getTechnologyName());
    }

    public List<TechnologyDto> findTechnologies() {
        List<TechnologyDto> technologyDtos = new ArrayList<>();
        technologyRepository.findAll().forEach(technology -> {
            TechnologyDto technologyDto = new TechnologyDto(technology.getTechnologyName());
            technologyDtos.add(technologyDto);
        });
        return technologyDtos;
    }

    public List<DevelopmentType> getDevelopmentTypes() {
        List<DevelopmentType> developmentTypes = new ArrayList<>();
        developmentTypeRepository.findAll().forEach(developmentType -> developmentTypes.add(developmentType));
        return developmentTypes;
    }
}
