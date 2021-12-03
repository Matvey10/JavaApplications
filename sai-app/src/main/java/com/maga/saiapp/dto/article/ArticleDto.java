package com.maga.saiapp.dto.article;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArticleDto {
    String articleTitle;
    String articleMagazine;
    Integer issuerNumber;
    Integer articleYear;

    String authorsIds;
    String keyWords;
    String aiAreasIds;
    String developmentsIds;
}
