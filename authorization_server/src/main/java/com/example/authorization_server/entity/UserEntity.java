package com.example.authorization_server.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@Accessors(chain = true)
public class UserEntity {
    @Id
    @SequenceGenerator(name = "user_gen", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    private Long id;
    @Column(name = "name")
    private String username;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "user")
    private Set<AccountEntity> accounts;
}
