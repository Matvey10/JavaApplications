package com.maga.saiapp.data.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Accessors(chain = true)
@Entity
public class Article {
    @Id
    @SequenceGenerator(name = "article_gen", sequenceName = "article_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_gen")
    private Long id;
    private String articleTitle;
    private String magazine;
    private Integer issueNumber;
    private Integer year;
    @ManyToMany
    @JoinTable(
        name = "article_author",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();
    @ManyToMany
    @JoinTable(
        name = "article_keyword",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "keyword_id"))
    private Set<Keyword> keywords = new HashSet<>();
    @ManyToMany
    @JoinTable(
        name = "article_development",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "development_id"))
    private Set<Development> developments = new HashSet<>();
    @ManyToMany
    @JoinTable(
        name = "article_ai_area",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "ai_area_id"))
    private Set<AiArea> aiAreas = new HashSet<>();

    public void addAuthors(List<Author> authors) {
        this.getAuthors().addAll(authors);
    }

    public void addKeywords(List<Keyword> keywords) {
        this.getKeywords().addAll(keywords);
    }

    public void addAiAreas(List<AiArea> aiAreas) {
        this.getAiAreas().addAll(aiAreas);
    }

    public void addDevelopments(List<Development> developments) {
        this.getDevelopments().addAll(developments);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Article other = (Article) obj;
        return Objects.equals(id, other.getId());
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + id +
            ", articleTitle='" + articleTitle + '\'' +
            ", magazine='" + magazine + '\'' +
            ", issueNumber=" + issueNumber +
            ", year=" + year +
            '}';
    }
}
