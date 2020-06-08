package com.company.controllers;

import com.company.Entities.Topic;
import com.company.services.TopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TopicsController {
    @Autowired
    TopicsService topicsService;
    @GetMapping("/addTopic")
    public String getTopicForm(Model model){
        return "addTopicForm";
    }
    @PostMapping("/addTopic")
    public String addTopic(@RequestParam String topic, @RequestParam String parentTopic){
        Topic parent = topicsService.findByTopicName(parentTopic);
        System.out.println(parent.getTopicName());
        Topic newTopic = new Topic();
        newTopic.setTopicName(topic);
        newTopic.setParentTopic(parent);
        topicsService.saveTopic(newTopic);
        return "redirect:/addTopic";
    }
    @PostMapping("/addCourse")
    public String addCourse(@RequestParam String course){
        Topic courseTopic = new Topic();
        courseTopic.setTopicName(course);
        courseTopic.setParentTopic(null);
        topicsService.saveTopic(courseTopic);
        return "redirect:/addTopic";
    }
}
