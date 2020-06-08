package com.company.controllers;

import com.company.Entities.TermDefinition;
import com.company.services.TermsDefinitionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TermsController {
    @Autowired
    TermsDefinitionsService termsDefinitionsService;
    @GetMapping("/addTerm")
    public String showAddTermForm(){
        return "termDefinitionForm";
    }
    @PostMapping("/addTerm")
    public String addTerm(@RequestParam String term, @RequestParam String definition){
        System.out.println(term + " " + definition);
        TermDefinition termDefinition = new TermDefinition(term, definition);
        termsDefinitionsService.addTerm(termDefinition);
        return "redirect:/addTerm";
    }

    @GetMapping("/showAllTerms")
    public String showAllTerms(Model model){
        return null;
    }

    @GetMapping("/showAllTerms/favourite")
    public String showFavouriteTerms(Model model){
        return null;
    }

    @GetMapping("/showAllTerms/{toic_id}")
    public String showTopicTerms(Model model){
        return null;
    }

}
