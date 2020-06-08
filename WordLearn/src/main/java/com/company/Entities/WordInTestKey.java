package com.company.Entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WordInTestKey implements Serializable {
    @Column(name = "word_id")
    private int wordId;
    @Column(name = "test_id")
    private int testId;

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordInTestKey)) return false;
        WordInTestKey that = (WordInTestKey) o;
        return getWordId() == that.getWordId() &&
                getTestId() == that.getTestId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWordId(), getTestId());
    }
}
