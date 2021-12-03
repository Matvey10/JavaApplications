package com.maga.saiapp.data.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Accessors(chain = true)
@Entity
public class Author {
    @Id
    @SequenceGenerator(name = "author_gen", sequenceName = "author_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_gen")
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String sex;
    private String country;
    @ManyToMany(mappedBy = "authors")
    private Set<Article> articles = new HashSet<>();
    @ManyToMany(mappedBy = "authors")
    private Set<Development> developments = new HashSet<>();

    public void addArticle(Article article) {
        this.getArticles().add(article);
    }

    public void addDevelopment(Development development) {
        this.getDevelopments().add(development);
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
        Author other = (Author) obj;
        return Objects.equals(id, other.getId());
    }

    @Override
    public String toString() {
        return "Author{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", patronymic='" + patronymic + '\'' +
            ", sex='" + sex + '\'' +
            ", country='" + country + '\'' +
            '}';
    }
}
