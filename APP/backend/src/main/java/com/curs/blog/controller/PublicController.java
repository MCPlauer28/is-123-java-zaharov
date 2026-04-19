package com.curs.blog.controller;

import com.curs.blog.dto.ArticleDto;
import com.curs.blog.dto.CategoryDto;
import com.curs.blog.dto.TagDto;
import com.curs.blog.facade.ArticleFacade;
import com.curs.blog.service.CategoryService;
import com.curs.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private ArticleFacade articleFacade;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @GetMapping("/articles")
    public ResponseEntity<Page<ArticleDto>> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        if (categoryId != null) {
            return ResponseEntity.ok(articleFacade.getArticlesByCategory(categoryId, pageable));
        } else if (tagId != null) {
            return ResponseEntity.ok(articleFacade.getArticlesByTag(tagId, pageable));
        } else {
            return ResponseEntity.ok(articleFacade.getPublishedArticles(pageable));
        }
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
        ArticleDto article = articleFacade.getArticleById(id);
        if (article == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(article);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return categoryService.getAllCategories().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/tags")
    public List<TagDto> getTags() {
        return tagService.getAllTags().stream()
                .map(t -> new TagDto(t.getId(), t.getName()))
                .collect(Collectors.toList());
    }
}
