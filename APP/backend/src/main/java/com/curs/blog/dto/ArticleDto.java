package com.curs.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String status;
    private String authorUsername;
    private Long authorId;
    private String categoryName;
    private Long categoryId;
    private Set<String> tags;
    private String imageUrl;
    private LocalDateTime createdAt;
}
