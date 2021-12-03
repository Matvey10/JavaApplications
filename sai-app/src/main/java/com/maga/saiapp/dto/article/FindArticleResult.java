package com.maga.saiapp.dto.article;

import com.maga.saiapp.data.entity.Keyword;
import com.maga.saiapp.dto.OperationResult;
import com.maga.saiapp.dto.author.AuthorDto;
import lombok.Data;

import java.util.List;

@Data
public class FindArticleResult extends OperationResult {
    String articleTitle;
    String articleMagazine;
    Integer issuerNumber;
    Integer articleYear;

    List<AuthorDto> authors;
    List<Keyword> keywords;
    List<SimpleDevelopmentDto> developments;
    List<AiAreaDto> aiAreas;
}
