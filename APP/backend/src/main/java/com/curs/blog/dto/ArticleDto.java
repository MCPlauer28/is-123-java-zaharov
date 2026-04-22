package com.curs.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ArticleDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
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
