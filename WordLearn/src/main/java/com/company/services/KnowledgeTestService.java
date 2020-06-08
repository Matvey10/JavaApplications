package com.company.services;

import com.company.Entities.Test;
import com.company.Entities.WordInTest;
import com.company.Repositories.TestRepository;
import com.company.Repositories.WordInTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class KnowledgeTestService {
    @Autowired
    WordInTestRepository wordInTestRepository;
    @Autowired
    TestRepository testRepository;
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveTest(Test test){
        testRepository.save(test);
    }
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveWordInTest(WordInTest wordInTest){
        wordInTestRepository.save(wordInTest);
    }

}
