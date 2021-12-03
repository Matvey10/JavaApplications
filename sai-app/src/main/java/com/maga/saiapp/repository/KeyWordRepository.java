package com.maga.saiapp.repository;

import com.maga.saiapp.data.entity.Keyword;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KeyWordRepository extends CrudRepository<Keyword, Long> {
    Optional<Keyword> findByValue(String value);
}
