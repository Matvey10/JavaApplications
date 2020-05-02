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
}
