package com.maga.saiapp.repository;

import com.maga.saiapp.data.entity.SaiProperty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SaiPropertyRepository extends CrudRepository<SaiProperty, Long> {
    List<SaiProperty> findAllByIdIn(List<Long> ids);
    List<SaiProperty> findAllByPropertyNameIn(List<String> names);
}
