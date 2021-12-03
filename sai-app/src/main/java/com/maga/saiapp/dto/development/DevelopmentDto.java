package com.maga.saiapp.dto.development;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DevelopmentDto {
    private String developmentName;
    private Long developmentTypeId;
    private Integer developmentYear;

    private String saiPropertiesIds;
    private String technologiesIds;
    private String authorsIds;

    private String articleIds;
}
