package com.company.Entities.SpecialEntities;

import java.util.HashSet;
import java.util.Set;

public class AnswersForWordTranslation {
    private String answer;
    private boolean correct;

    public AnswersForWordTranslation(String answer) {
        this.answer = answer;
        this.correct = false;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
