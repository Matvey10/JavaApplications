package com.example.authorization_server.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "consent_accounts")
@Data
@Accessors(chain = true)
public class ConsentAccounts {
    @Id
    @SequenceGenerator(name = "consent_accounts_gen", sequenceName = "consent_accounts_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consent_accounts_gen")
    private Long id;
    @Column(name = "consent_id")
    private Long consentId;
    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "status")
    private String status;
}
