package com.company.KnowledgeTest;

import com.company.Entities.*;
import com.company.Entities.SpecialEntities.AnswersForWordTranslation;
import com.company.services.KnowledgeTestService;
import com.company.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TestGenerator {
    @Autowired
    UserService userService;
    @Autowired
    KnowledgeTestService knowledgeTestService;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map<Word, List<AnswersForWordTranslation>> generateTest(String testName, int count){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Word> userWords = userService.findAllUserWords(user.getId());
        Collections.shuffle(userWords);
        userWords.stream().forEach(System.out::println);
        Test test = new Test();
        test.setTestName(testName);
        knowledgeTestService.saveTest(test);
        List<Word> forTest = userWords.stream().limit(count).collect(Collectors.toList());
        List<WordInTest> wordsInTest = new ArrayList<>();
        for (Word word : forTest){
            WordInTest wordInTest = new WordInTest();
            wordInTest.setWord(word);
            wordInTest.setWordTest(test);
            wordInTest.setKnow(0);
            wordsInTest.add(wordInTest);
            WordInTestKey wordInTestKey = new WordInTestKey();
            wordInTestKey.setWordId(word.getId());
            wordInTestKey.setTestId(test.getId());
            wordInTest.setId(wordInTestKey);
            knowledgeTestService.saveWordInTest(wordInTest);
        }
        Map<Word, List<AnswersForWordTranslation>> wordsWithAnswers = addAnswers(forTest, userWords);
        return wordsWithAnswers;
    }
    public List<Word> getWords(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Word> userWords = userService.findAllUserWords(user.getId());
        Collections.shuffle(userWords);
        userWords.stream().forEach(System.out::println);
        return userWords;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Map<Word, List<AnswersForWordTranslation>> addAnswers(List<Word> words, List<Word> allWords){
        Map<Word, List<AnswersForWordTranslation>> wordsWithAnswers = new HashMap<>();
        for (Word w : words){
            allWords.remove(w);
            Collections.shuffle(allWords);
            Function<String, AnswersForWordTranslation> getAns = (text)-> new AnswersForWordTranslation(text);
            List<String> answersText = allWords.stream().limit(3)
                    .map(Word::getTranslation).collect(Collectors.toList());
            AnswersForWordTranslation right = new AnswersForWordTranslation(w.getTranslation());
            right.setCorrect(true);
            List<AnswersForWordTranslation> answers = answersText.stream().map(getAns).collect(Collectors.toList());
            answers.add(right);
            Collections.shuffle(answers);
            wordsWithAnswers.put(w, answers);
            allWords.add(w);
        }
        return wordsWithAnswers;
    }


}
