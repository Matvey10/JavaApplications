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
public class Keyword {
    @Id
    @SequenceGenerator(name = "keyword_gen", sequenceName = "keyword_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "keyword_gen")
    private Long id;
    private String value;
    @ManyToMany(mappedBy = "keywords") // мб она тут не нужна?
    private Set<Article> articles = new HashSet<>();

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
        Keyword other = (Keyword) obj;
        return Objects.equals(id, other.getId());
    }
}
