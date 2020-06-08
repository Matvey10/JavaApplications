package com.company.Entities.SpecialSqlResults;

public class WordsAndCount {
    private String word;
    private Long count;

    public WordsAndCount(String word, Long count) {
        this.word = word;
        this.count = count;
    }

    public WordsAndCount() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "WordsAndCount{" +
                "word='" + word + '\'' +
                ", count=" + count +
                '}';
    }
}
