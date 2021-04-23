package com.example.authorization_server.repository;

import com.example.authorization_server.entity.ConsentEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConsentRepository extends CrudRepository<ConsentEntity, Long> {
}
