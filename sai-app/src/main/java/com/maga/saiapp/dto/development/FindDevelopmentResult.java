package com.maga.saiapp.dto.development;

import com.maga.saiapp.dto.OperationResult;
import com.maga.saiapp.dto.article.ArticleDto;
import com.maga.saiapp.dto.author.AuthorDto;
import lombok.Data;

import java.util.List;

@Data
public class FindDevelopmentResult extends OperationResult {
    private String developmentName;
    private Integer developmentYear;
    private String developmentType;

    List<AuthorDto> authors;
    List<ArticleDto> articles;
    List<SaiPropertyDto> saiProperties;
    List<TechnologyDto> technologies;
}
