package com.company.Repositories;

import com.company.Entities.User;
import com.company.Entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Integer> {
    /*@Query("select distinct from Words w where w.user_id =: user_id")
    List<User> findAllByUser(Integer user_id);*/
    //доделать связь и запросы
}
