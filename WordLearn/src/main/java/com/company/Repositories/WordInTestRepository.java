package com.company.Repositories;

import com.company.Entities.WordInTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordInTestRepository extends JpaRepository<WordInTest, Integer> {
}
