package com.company.Repositories;

import com.company.Entities.TermDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermDefinitionRepository extends JpaRepository<TermDefinition, Long> {
     List<TermDefinition> findByTermIsLike(String regex);
}
