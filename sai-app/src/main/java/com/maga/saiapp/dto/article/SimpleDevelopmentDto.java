package com.maga.saiapp.dto.article;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SimpleDevelopmentDto {
    private Long id;
    private String developmentType;
    private String developmentName;
    private Integer developmentYear;
}
