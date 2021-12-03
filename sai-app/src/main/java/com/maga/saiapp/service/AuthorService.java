package com.maga.saiapp.service;

import com.maga.saiapp.data.entity.Author;
import com.maga.saiapp.dto.author.AuthorDto;
import com.maga.saiapp.dto.author.FindAuthorSearchFilter;
import com.maga.saiapp.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthorService {
    @Resource
    private AuthorRepository authorRepository;

    @Transactional
    public void addAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.getName())
            .setSurname(authorDto.getSurname())
            .setPatronymic(authorDto.getPatronymic())
            .setCountry(authorDto.getCountry())
            .setSex(authorDto.getSex());
        Author saved = authorRepository.save(author);
        log.debug("Saved author: [{}]", saved.toString());
    }

    @Transactional
    public List<AuthorDto> findAll(FindAuthorSearchFilter filter) {
        List<AuthorDto> authorDtos = new ArrayList<>();
        if (StringUtils.isBlank(filter.getCountry()) && StringUtils.isBlank(filter.getName()) && StringUtils.isBlank(filter.getSurname()) ) {
            authorRepository.findAll().forEach(author -> {
                    authorDtos.add(convert(author));
            });
            return authorDtos;
        }
        if (StringUtils.isNotBlank(filter.getSurname()) && StringUtils.isBlank(filter.getCountry()) && StringUtils.isBlank(filter.getName())) {
            return authorRepository.findAllBySurnameLike(filter.getSurname()).stream()
                .map(this::convert).collect(Collectors.toList());
        }
        if (StringUtils.isBlank(filter.getSurname()) && StringUtils.isNotBlank(filter.getCountry()) && StringUtils.isBlank(filter.getName())) {
            return authorRepository.findAllByCountryLike(filter.getCountry()).stream()
                .map(this::convert).collect(Collectors.toList());
        }
        if (StringUtils.isBlank(filter.getSurname()) && StringUtils.isBlank(filter.getCountry()) && StringUtils.isNotBlank(filter.getName())) {
            return authorRepository.findAllByNameLike(filter.getName()).stream()
                .map(this::convert).collect(Collectors.toList());
        }
        return authorRepository.findAllByNameLikeAndSurnameLikeAndCountryLike(filter.getName(),
            filter.getSurname(), filter.getCountry()).stream()
            .map(this::convert).collect(Collectors.toList());
    }

    @Transactional
    public AuthorDto findById(Long id) {
        Author author = authorRepository.findById(id).get();
        return convert(author);
    }

    @Transactional
    public List<String> getAllCountries() {
        return authorRepository.findAllCountries();
    }

    private AuthorDto convert(Author author) {
        AuthorDto authorDto = new AuthorDto()
            .setId(author.getId())
            .setName(author.getName())
            .setSurname(author.getSurname())
            .setPatronymic(author.getPatronymic())
            .setCountry(author.getCountry());
        return authorDto;
    }
}
