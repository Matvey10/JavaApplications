package com.company.controllers;

import com.company.Entities.Role;
import com.company.Entities.User;
import com.company.Entities.Word;
import com.company.services.UserService;
import com.company.services.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    public Logger logger;
    @Autowired
    UserService userService;
    @Autowired
    WordService wordService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/")
    public String redirectToStartPage(){
        return "javaArticles";
    }

    @GetMapping("/addWord")
    public String showNewWordForm(Model model){
        model.addAttribute("newWord", new Word());
        return "wordForm";
    }

    @PostMapping("/addWord")
    public String addNewWord(@RequestParam String word, @RequestParam String transl , Model model){
        Word newWord= new Word();
        newWord.setWord(word);
        newWord.setTranslation(transl);
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newWord.setUser(user);
        user.addWord(newWord);
        logger.info("User have added new word:" + word);
        wordService.addWord(newWord);
        return "redirect:/addWord";
    }

    @GetMapping("/signUp")
    public String showRegistrationForm(Model model){
        model.addAttribute("newUser", new User());
        return "registration";
    }

    @PostMapping("/signUp")
    public String addNewUser(@ModelAttribute User newUser, Model model){
        System.out.println(newUser.getFirstName() + " " + newUser.getSecondName());
        User user = new User();
        user.setFirstName(newUser.getFirstName());
        user.setSecondName(newUser.getSecondName());
        user.setAge(newUser.getAge());
        user.setLogin(newUser.getLogin());
        user.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        user.setRoles(Collections.singleton(new Role("USER")));
        if (!userService.addUser(user)) {
            model.addAttribute("loginError", "Пользователь с таким именем уже существует");
            model.addAttribute("newUser", new User());
            return "registration.html";
        }
        return "redirect:/login";
    }

    @GetMapping("/allUserWords")
    public String showAllUserWords(Model model){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Word> allUserWords = userService.findAllUserWords(user.getId());
        for (Word w : allUserWords){
            System.out.println(w.getWord() + " : " + w.getTranslation());
        }
        model.addAttribute("allWords", allUserWords);
        return "allUserWords";
    }

}
