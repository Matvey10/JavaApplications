package com.example.authorization_server.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "account")
@Data
@Accessors(chain = true)
public class AccountEntity {
    @Id
    @SequenceGenerator(name = "account_gen", sequenceName = "account_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_gen")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
