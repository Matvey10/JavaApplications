package com.example.authorization_server.repository;

import com.example.authorization_server.entity.ClientPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientPermissionsRepository extends JpaRepository<ClientPermissionEntity, Long> {
}
