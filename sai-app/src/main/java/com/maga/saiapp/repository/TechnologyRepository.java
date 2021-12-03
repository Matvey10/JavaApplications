package com.maga.saiapp.repository;

import com.maga.saiapp.data.entity.Technology;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TechnologyRepository extends CrudRepository<Technology, Long> {
    List<Technology> findAllByIdIn(List<Long> ids);
    List<Technology> findAllByTechnologyNameIn(List<String> technologies);
}
