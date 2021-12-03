package com.maga.saiapp.dto.development;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SimpleDevelopment {
    private Long id;
    private String developmentName;
    private Integer developmentYear;
    private String developmentType;
}
