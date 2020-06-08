package com.company.services;

import com.company.Entities.Topic;
import com.company.Repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicsService {
    @Autowired
    TopicRepository topicRepository;
    public void saveTopic(Topic topic){
        topicRepository.save(topic);
    }
    public Topic findByTopicName(String name){
        return topicRepository.findByTopicName(name);
    }
}
