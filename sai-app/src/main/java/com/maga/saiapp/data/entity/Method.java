package com.maga.saiapp.data.entity;

import lombok.Data;

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
@Entity
public class Method {
    @Id
    @SequenceGenerator(name = "method_gen", sequenceName = "method_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "method_gen")
    private Long id;
    private String methodName;
    private String scienceBranch;
    @ManyToMany(mappedBy = "methods")
    private Set<AiArea> aiAreas = new HashSet<>();

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
        Method other = (Method) obj;
        return Objects.equals(id, other.getId());
    }
}
