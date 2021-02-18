package com.company.Entities;

import javax.persistence.*;

@Entity
public class WordTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Double score;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public WordTestResult(Double score, User user) {
        this.score = score;
        this.user = user;
    }

    public WordTestResult() {
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
