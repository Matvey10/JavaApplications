package com.company.Entities;

import javax.persistence.*;

@Entity
public class TermDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String term;
    String definition;
    @OneToOne
    @JoinColumn(name = "topic_id")
    Topic topic; // добавил

    public TermDefinition() {
    }

    public TermDefinition(String term, String definition){
        this.term = term;
        this.definition = definition;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
