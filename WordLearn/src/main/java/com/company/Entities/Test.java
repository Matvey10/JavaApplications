package com.company.Entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WordTests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "test_name")
    private String testName;
    @OneToMany(mappedBy = "wordTest")
    private Set<WordInTest> wordsInTest = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Set<WordInTest> getWordsInTest() {
        return wordsInTest;
    }

    public void setWordsInTest(Set<WordInTest> wordsInTest) {
        this.wordsInTest = wordsInTest;
    }
}
