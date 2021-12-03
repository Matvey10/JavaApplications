package com.maga.saiapp.dto.article;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SimpleArticle {
    private Long id;
    private String articleTitle;
    private String magazine;
    private Integer issueNumber;
    private Integer year;
}
