package com.maga.saiapp.dto.article;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FindArticlesSearchFilter {
    private String articleTitle;
    private String magazine;
    private String aiAreas;
    private String keywords;
    private String orderBy;
}
