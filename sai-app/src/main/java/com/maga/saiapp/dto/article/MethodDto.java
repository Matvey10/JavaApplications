package com.maga.saiapp.dto.article;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MethodDto {
    private Long id;
    private String name;
    private String scienceBranch;
}
