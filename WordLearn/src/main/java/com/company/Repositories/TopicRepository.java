package com.company.Repositories;

import com.company.Entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
    Topic findByTopicName(String name);
}
