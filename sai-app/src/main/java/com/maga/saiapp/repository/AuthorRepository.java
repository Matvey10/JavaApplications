package com.maga.saiapp.repository;

import com.maga.saiapp.data.entity.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    List<Author> findAllByIdIn(List<Long> ids);
    List<Author> findAllByNameLikeAndSurnameLikeAndCountryLike(String name, String surname, String country);
    List<Author> findAllByNameLike(String name);
    List<Author> findAllBySurnameLike(String surname);
    List<Author> findAllByCountryLike(String country);
    @Query(value = "select distinct a.country from author a", nativeQuery = true)
    List<String> findAllCountries();
}
