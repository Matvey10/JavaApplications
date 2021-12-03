package com.maga.saiapp.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Technology {
    @Id
    @SequenceGenerator(name = "technology_gen", sequenceName = "technology_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "technology_gen")
    private Long id;
    private String technologyName;
    @ManyToMany(mappedBy = "technologies", fetch = FetchType.LAZY)
    private Set<Development> developments = new HashSet<>();

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
        Technology other = (Technology) obj;
        return Objects.equals(id, other.getId());
    }
}
