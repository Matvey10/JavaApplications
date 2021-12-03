package com.maga.saiapp.dto.development;

import com.maga.saiapp.dto.OperationResult;
import lombok.Data;

import java.util.List;

@Data
public class SearchDevelopmentsResult extends OperationResult {
    private List<SimpleDevelopment> developments;
}
