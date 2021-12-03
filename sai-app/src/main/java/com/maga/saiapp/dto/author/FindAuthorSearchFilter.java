package com.maga.saiapp.dto.author;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FindAuthorSearchFilter {
    String name;
    String surname;
    String country;
}
