package com.maga.saiapp.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class AiArea {
    @Id
    @SequenceGenerator(name = "ai_area_gen", sequenceName = "ai_area_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ai_area_gen")
    private Long id;
    private String areaName;
    private String areaBranch;
    @ManyToMany(mappedBy = "aiAreas")
    private Set<Article> articles = new HashSet<>();
    @ManyToMany
    @JoinTable(
        name = "ai_area_method",
        joinColumns = @JoinColumn(name = "ai_area_id"),
        inverseJoinColumns = @JoinColumn(name = "method_id"))
    private Set<Method> methods = new HashSet<>();

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
        AiArea other = (AiArea) obj;
        return Objects.equals(id, other.getId());
    }
}
