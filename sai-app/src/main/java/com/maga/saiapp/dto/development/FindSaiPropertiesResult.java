package com.maga.saiapp.dto.development;

import com.maga.saiapp.dto.OperationResult;
import lombok.Data;

import java.util.List;

@Data
public class FindSaiPropertiesResult extends OperationResult {
    private List<SaiPropertyDto> saiPropertyDtos;
}
