package com.company.Entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "word")
    private String word;
    @Column(name = "translation")
    private String translation;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "word")
    private Set<WordInTest> wordInTests = new HashSet<>();

    public void setUser(User user) {
        this.user = user;
    }

    public String getWord() {
        return word;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word1 = (Word) o;
        return Objects.equals(getId(), word1.getId()) &&
                Objects.equals(getWord(), word1.getWord()) &&
                Objects.equals(getTranslation(), word1.getTranslation()) &&
                Objects.equals(getUser(), word1.getUser()) &&
                Objects.equals(wordInTests, word1.wordInTests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getWord(), getTranslation(), getUser(), wordInTests);
    }
}
