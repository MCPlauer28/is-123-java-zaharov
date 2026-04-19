package com.curs.blog.facade;

import com.curs.blog.dto.ArticleDto;
import com.curs.blog.entity.Article;
import com.curs.blog.entity.EArticleStatus;
import com.curs.blog.entity.Tag;
import com.curs.blog.service.ArticleService;
import com.curs.blog.service.CategoryService;
import com.curs.blog.service.TagService;
import com.curs.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ArticleFacade {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    public ArticleDto convertToDto(Article article) {
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

    public Page<ArticleDto> getPublishedArticles(Pageable pageable) {
        return articleService.getPublishedArticles(pageable).map(this::convertToDto);
    }

    public Page<ArticleDto> getArticlesByCategory(Long categoryId, Pageable pageable) {
        return articleService.getPublishedArticlesByCategory(categoryId, pageable).map(this::convertToDto);
    }

    public Page<ArticleDto> getArticlesByTag(Long tagId, Pageable pageable) {
        return articleService.getPublishedArticlesByTag(tagId, pageable).map(this::convertToDto);
    }

    public List<ArticleDto> getArticlesByAuthor(Long authorId) {
        return articleService.getArticlesByAuthor(authorId).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ArticleDto getArticleById(Long id) {
        return articleService.getArticleById(id).map(this::convertToDto).orElse(null);
    }

    public ArticleDto createArticle(ArticleDto dto, Long authorId) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setStatus(EArticleStatus.valueOf(dto.getStatus()));
        article.setImageUrl(dto.getImageUrl());
        article.setAuthor(userService.getUserById(authorId).orElseThrow());
        
        if (dto.getCategoryId() != null) {
            article.setCategory(categoryService.getCategoryById(dto.getCategoryId()).orElse(null));
        }

        if (dto.getTags() != null) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : dto.getTags()) {
                Tag tag = tagService.getTagByName(tagName).orElseGet(() -> tagService.saveTag(new Tag(tagName)));
                tags.add(tag);
            }
            article.setTags(tags);
        }

        return convertToDto(articleService.saveArticle(article));
    }

    public ArticleDto updateArticle(Long id, ArticleDto dto) {
        Article article = articleService.getArticleById(id).orElseThrow();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setStatus(EArticleStatus.valueOf(dto.getStatus()));
        article.setImageUrl(dto.getImageUrl());

        if (dto.getCategoryId() != null) {
            article.setCategory(categoryService.getCategoryById(dto.getCategoryId()).orElse(null));
        }

        if (dto.getTags() != null) {
            Set<Tag> tags = new HashSet<>();
            for (String tagName : dto.getTags()) {
                Tag tag = tagService.getTagByName(tagName).orElseGet(() -> tagService.saveTag(new Tag(tagName)));
                tags.add(tag);
            }
            article.setTags(tags);
        }

        return convertToDto(articleService.saveArticle(article));
    }

    public void deleteArticle(Long id) {
        articleService.deleteArticle(id);
    }

    public List<ArticleDto> getAllArticles() {
        return articleService.getAllArticles().stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
