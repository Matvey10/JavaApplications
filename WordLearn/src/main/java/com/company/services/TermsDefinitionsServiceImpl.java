package com.company.services;

import com.company.Entities.TermDefinition;
import com.company.Repositories.TermDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TermsDefinitionsServiceImpl implements TermsDefinitionsService {
    @Autowired
    TermDefinitionRepository termDefinitionRepository;
    @Override
    public boolean addTerm(TermDefinition termDefinition) {
        termDefinitionRepository.save(termDefinition);
        return true;
    }
}
