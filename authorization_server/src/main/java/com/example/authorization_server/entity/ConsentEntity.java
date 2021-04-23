package com.example.authorization_server.entity;

import com.example.authorization_server.entity.enums.ConsentStatusType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "consent")
@Data
@Accessors(chain = true)
public class ConsentEntity {
    @Id
    @SequenceGenerator(name = "consent_gen", sequenceName = "consent_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consent_gen")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "consent_status")
    private ConsentStatusType consentStatus;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "consent_permission",
        joinColumns = @JoinColumn(name = "consent_id"),
        inverseJoinColumns = @JoinColumn(name = "client_permission_id"))
    private Set<ClientPermissionEntity> permissions;
    @Column(name = "expiration_date_time")
    private Instant expirationDateTime;
    @Column(name = "creation_date_time")
    private Instant creationDateTime;
}
