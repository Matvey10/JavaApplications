package com.maga.saiapp.repository;

import com.maga.saiapp.data.entity.Development;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DevelopmentRepository extends CrudRepository<Development, Long> {
    List<Development> findAllByIdIn(List<Long> ids);
}
