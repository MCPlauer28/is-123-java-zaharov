package com.curs.blog.facade;

import com.curs.blog.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IArticleFacade {
    Page<ArticleDto> getPublishedArticles(Pageable pageable);
    Page<ArticleDto> getArticlesByCategory(Long categoryId, Pageable pageable);
    Page<ArticleDto> getArticlesByTag(Long tagId, Pageable pageable);
    List<ArticleDto> getArticlesByAuthor(Long authorId);
    ArticleDto getArticleById(Long id);
    ArticleDto createArticle(ArticleDto dto, Long authorId);
    ArticleDto updateArticle(Long id, ArticleDto dto);
    void deleteArticle(Long id);
    List<ArticleDto> getAllArticles();
}
