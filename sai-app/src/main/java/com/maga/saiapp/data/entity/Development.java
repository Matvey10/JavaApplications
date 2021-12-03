package com.maga.saiapp.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Accessors(chain = true)
@Entity
@EqualsAndHashCode
public class Development {
    @Id
    @SequenceGenerator(name = "development_gen", sequenceName = "development_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "development_gen")
    private Long id;
    private String developmentName;
    private Integer year;
    @ManyToOne
    @JoinColumn(name="development_type_id")
    private DevelopmentType developmentType;
    @ManyToMany(mappedBy = "developments")
    private Set<Article> articles = new HashSet<>();
    @ManyToMany
    @JoinTable(
        name = "author_development",
        joinColumns = @JoinColumn(name = "development_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();
    @ManyToMany
    @JoinTable(
        name = "development_sai_property",
        joinColumns = @JoinColumn(name = "development_id"),
        inverseJoinColumns = @JoinColumn(name = "sai_property_id"))
    private Set<SaiProperty> saiProperties = new HashSet<>();
    @ManyToMany
    @JoinTable(
        name = "development_technology",
        joinColumns = @JoinColumn(name = "development_id"),
        inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private Set<Technology> technologies = new HashSet<>();

    public void addAuthors(List<Author> authors) {
        this.getAuthors().addAll(authors);
    }

    public void addTechnologies(List<Technology> technologies) {
        this.getTechnologies().addAll(technologies);
    }

    public void addSaiProperties(List<SaiProperty> saiProperties) {
        this.getSaiProperties().addAll(saiProperties);
    }

    public void addArticles(List<Article> articles) {
        this.getArticles().addAll(articles);
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
        Development other = (Development) obj;
        return Objects.equals(id, other.getId());
    }
}
