package com.maga.saiapp.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AiAreaDto {
    private Long id;
    private String areaName;
    private String areaBranch;

    private List<MethodDto> methods;
}
