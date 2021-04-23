package com.example.authorization_server.entity;

import com.example.authorization_server.entity.enums.PermissionType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "client_permission")
@Data
@Accessors(chain = true)
public class ClientPermissionEntity {
    @Id
    @SequenceGenerator(name = "client_permission_gen", sequenceName = "client_permission_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_permission_gen")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    PermissionType permissionType;
    @ManyToMany(mappedBy = "permissions")
    Set<ConsentEntity> consents;
}
