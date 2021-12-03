package com.maga.saiapp.dto.article;

import com.maga.saiapp.dto.OperationResult;
import lombok.Data;

import java.util.List;

@Data
public class FindAiAreasResult extends OperationResult {
    private List<AiAreaDto> aiAreaDtos;
}
