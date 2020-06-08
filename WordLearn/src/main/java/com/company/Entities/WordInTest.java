package com.company.Entities;

import javax.persistence.*;

@Entity
public class WordInTest {
    @EmbeddedId
    WordInTestKey id;

    @ManyToOne
    @MapsId("word_id")
    @JoinColumn(name = "word_id")
    Word word;

    @ManyToOne
    @MapsId("test_id")
    @JoinColumn(name = "test_id")
    Test wordTest;

    public WordInTestKey getId() {
        return id;
    }

    public void setId(WordInTestKey id) {
        this.id = id;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Test getWordTest() {
        return wordTest;
    }

    public void setWordTest(Test wordTest) {
        this.wordTest = wordTest;
    }

    public double getKnow() {
        return know;
    }

    public void setKnow(double know) {
        this.know = know;
    }

    double know;

    @Override
    public String toString() {
        return "WordInTest{" +
                "id=" + id +
                ", word=" + word.getWord() +
                ", wordTest=" + wordTest.getTestName() +
                ", know=" + know +
                '}';
    }
}
