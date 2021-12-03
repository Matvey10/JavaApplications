package com.maga.saiapp.dto.author;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthorDto {
    Long id;
    String name;
    String surname;
    String patronymic;
    String country;
    String sex;
}
