package com.curs.blog.facade;

import com.curs.blog.dto.ArticleDto;
import com.curs.blog.entity.Article;
import com.curs.blog.entity.EArticleStatus;
import com.curs.blog.entity.Tag;
import com.curs.blog.facade.mapper.ArticleMapper;
import com.curs.blog.service.ArticleService;
import com.curs.blog.service.CategoryService;
import com.curs.blog.service.TagService;
import com.curs.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ArticleFacade implements IArticleFacade {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Page<ArticleDto> getPublishedArticles(Pageable pageable) {
        log.info("Fetching published articles with pageable: {}", pageable);
        return articleService.getPublishedArticles(pageable).map(articleMapper::toDto);
    }

    @Override
    public Page<ArticleDto> getArticlesByCategory(Long categoryId, Pageable pageable) {
        log.info("Fetching articles for category {} with pageable: {}", categoryId, pageable);
        return articleService.getPublishedArticlesByCategory(categoryId, pageable).map(articleMapper::toDto);
    }

    @Override
    public Page<ArticleDto> getArticlesByTag(Long tagId, Pageable pageable) {
        log.info("Fetching articles for tag {} with pageable: {}", tagId, pageable);
        return articleService.getPublishedArticlesByTag(tagId, pageable).map(articleMapper::toDto);
    }

    @Override
    public List<ArticleDto> getArticlesByAuthor(Long authorId) {
        log.info("Fetching articles for author {}", authorId);
        return articleService.getArticlesByAuthor(authorId).stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDto getArticleById(Long id) {
        log.info("Fetching article by id {}", id);
        return articleService.getArticleById(id).map(articleMapper::toDto).orElse(null);
    }

    @Override
    @Transactional
    public ArticleDto createArticle(ArticleDto dto, Long authorId) {
        log.info("Creating new article for author {}", authorId);
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setStatus(EArticleStatus.valueOf(dto.getStatus()));
        article.setImageUrl(dto.getImageUrl());
        article.setAuthor(userService.getUserById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found")));
        
        updateArticleRelations(article, dto);

        Article saved = articleService.saveArticle(article);
        log.info("Article created with id {}", saved.getId());
        return articleMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ArticleDto updateArticle(Long id, ArticleDto dto) {
        log.info("Updating article with id {}", id);
        Article article = articleService.getArticleById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setStatus(EArticleStatus.valueOf(dto.getStatus()));
        article.setImageUrl(dto.getImageUrl());

        updateArticleRelations(article, dto);

        Article updated = articleService.saveArticle(article);
        log.info("Article {} updated", id);
        return articleMapper.toDto(updated);
    }

    private void updateArticleRelations(Article article, ArticleDto dto) {
        if (dto.getCategoryId() != null) {
            article.setCategory(categoryService.getCategoryById(dto.getCategoryId()).orElse(null));
        }

        if (dto.getTags() != null) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : dto.getTags()) {
                Tag tag = tagService.getTagByName(tagName)
                        .orElseGet(() -> tagService.saveTag(new Tag(tagName)));
                tags.add(tag);
            }
            article.setTags(tags);
        }
    }

    @Override
    public void deleteArticle(Long id) {
        log.info("Deleting article with id {}", id);
        articleService.deleteArticle(id);
    }

    @Override
    public List<ArticleDto> getAllArticles() {
        log.info("Fetching all articles for admin");
        return articleService.getAllArticles().stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }
}
