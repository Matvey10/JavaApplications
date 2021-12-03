package com.maga.saiapp.repository;

import com.maga.saiapp.data.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    List<Article> findAllByIdIn(List<Long> ids);
//    @Modifying
//    @Query(value = "insert into article_author values (:article_id, author_id)", nativeQuery = true)
//    void saveArticleAuthor(@Param("article_id") Long articleId, @Param("author_id") Long author_id);
}
