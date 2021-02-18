package com.company.services;

import com.company.Entities.Test;
import com.company.Entities.WordInTest;
import com.company.Entities.WordTestResult;
import com.company.Repositories.TestRepository;
import com.company.Repositories.WordInTestRepository;
import com.company.Repositories.WordTestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class KnowledgeTestService {
    @Autowired
    WordInTestRepository wordInTestRepository;
    @Autowired
    TestRepository testRepository;
    @Autowired
    WordTestResultRepository resultRepository;
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveTest(Test test){
        testRepository.save(test);
    }
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveWordInTest(WordInTest wordInTest){
        wordInTestRepository.save(wordInTest);
    }
    @Transactional
    public void saveTestResult(WordTestResult result){
        resultRepository.save(result);
    }

}
