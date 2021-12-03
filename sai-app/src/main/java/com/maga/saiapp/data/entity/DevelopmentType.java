package com.maga.saiapp.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class DevelopmentType {
    @Id
    @SequenceGenerator(name = "development_type_gen", sequenceName = "development_type_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "development_type_gen")
    private Long id;
    private String typeName;
    /*тут не храню ссылку на набор Development*/
}
