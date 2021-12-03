package com.maga.saiapp.repository;

import com.maga.saiapp.data.entity.AiArea;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AiAreaRepository extends CrudRepository<AiArea, Long> {
    List<AiArea> findAllByIdIn(List<Long> ids);
}
