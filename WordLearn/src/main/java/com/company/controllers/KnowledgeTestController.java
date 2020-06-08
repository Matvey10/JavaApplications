package com.company.controllers;

import com.company.Entities.Article;
import com.company.Entities.SpecialEntities.AnswersForWordTranslation;
import com.company.Entities.SpecialSqlResults.UserAndWordCount;
import com.company.Entities.Word;
import com.company.Entities.WordInTest;
import com.company.KnowledgeTest.TestGenerator;
import com.company.services.ArticleService;
import com.company.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class KnowledgeTestController {
    @Autowired
    TestGenerator testGenerator;
    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;
    @GetMapping("/knowledgeTest")
    public String getWordListForTest(Model model){
        List<UserAndWordCount> rating = userService.getUserRating();
        for(UserAndWordCount userAndWordCount : rating){
            System.out.println(userAndWordCount.getUserLogin() + ":"
                    + userAndWordCount.getCount());
        }
        List<Article> articles = articleService.findArticlesByRegex("rest");
        for (Article a : articles){
            System.out.println(a.getArticleTitle());
        }
        return "generateTest";
    }
    @PostMapping("/knowledgeTest/generate")
    public String generateTest(@RequestParam Integer wordCount, @RequestParam(required = false) String testName, Model model){
        List<Word> userWords = testGenerator.getWords();
        if (userWords.size() < wordCount) {
            model.addAttribute("message", "В вашем словаре не хватает слов");
            wordCount = userWords.size();
        }
        Map<Word, List<AnswersForWordTranslation>> wordsForTest = testGenerator.generateTest(testName, wordCount);
        wordsForTest.keySet().stream().forEach(System.out::println);
        model.addAttribute("wordsForTest", wordsForTest);
        return "wordTest";
    }

    @PostMapping("/knowledgeTest")
    public String getTestResults(@RequestParam Double result, Model model){
        System.out.println("result is " + result);
        return "redirect:/addWord";
    }
}
