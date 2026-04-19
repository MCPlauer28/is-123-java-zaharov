package com.curs.blog.service;

import com.curs.blog.entity.Article;
import com.curs.blog.entity.EArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Page<Article> getPublishedArticles(Pageable pageable);
    Page<Article> getPublishedArticlesByCategory(Long categoryId, Pageable pageable);
    Page<Article> getPublishedArticlesByTag(Long tagId, Pageable pageable);
    List<Article> getArticlesByAuthor(Long authorId);
    Optional<Article> getArticleById(Long id);
    Article saveArticle(Article article);
    void deleteArticle(Long id);
    List<Article> getAllArticles(); // For Admin
}
