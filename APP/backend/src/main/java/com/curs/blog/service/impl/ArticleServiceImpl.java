package com.curs.blog.service.impl;

import com.curs.blog.entity.Article;
import com.curs.blog.entity.EArticleStatus;
import com.curs.blog.repository.ArticleRepository;
import com.curs.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Page<Article> getPublishedArticles(Pageable pageable) {
        return articleRepository.findByStatus(EArticleStatus.PUBLISHED, pageable);
    }

    @Override
    public Page<Article> getPublishedArticlesByCategory(Long categoryId, Pageable pageable) {
        return articleRepository.findByStatusAndCategoryId(EArticleStatus.PUBLISHED, categoryId, pageable);
    }

    @Override
    public Page<Article> getPublishedArticlesByTag(Long tagId, Pageable pageable) {
        return articleRepository.findByStatusAndTagsId(EArticleStatus.PUBLISHED, tagId, pageable);
    }

    @Override
    public List<Article> getArticlesByAuthor(Long authorId) {
        return articleRepository.findByAuthorId(authorId);
    }

    @Override
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }
}
