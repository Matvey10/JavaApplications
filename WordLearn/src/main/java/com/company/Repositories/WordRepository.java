package com.company.Repositories;

import com.company.Entities.SpecialSqlResults.WordsAndCount;
import com.company.Entities.User;
import com.company.Entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
}
