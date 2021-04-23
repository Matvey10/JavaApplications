package com.example.authorization_server.repository;

import com.example.authorization_server.entity.AccountEntity;
import com.example.authorization_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findAllByUser(UserEntity userEntity);
}
