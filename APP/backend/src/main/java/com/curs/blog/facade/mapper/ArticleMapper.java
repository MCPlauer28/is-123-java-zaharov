package com.curs.blog.facade.mapper;

import com.curs.blog.dto.ArticleDto;
import com.curs.blog.entity.Article;
import com.curs.blog.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    public ArticleDto toDto(Article article) {
        if (article == null) return null;

        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setStatus(article.getStatus().name());
        dto.setAuthorUsername(article.getAuthor().getUsername());
        dto.setAuthorId(article.getAuthor().getId());
        dto.setCategoryName(article.getCategory() != null ? article.getCategory().getName() : null);
        dto.setCategoryId(article.getCategory() != null ? article.getCategory().getId() : null);
        dto.setTags(article.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        dto.setImageUrl(article.getImageUrl());
        dto.setCreatedAt(article.getCreatedAt());
        return dto;
    }
}
