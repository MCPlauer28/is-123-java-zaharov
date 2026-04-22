package com.curs.blog.controller;

import com.curs.blog.dto.ArticleDto;
import com.curs.blog.dto.CategoryDto;
import com.curs.blog.dto.MessageResponse;
import com.curs.blog.dto.TagDto;
import com.curs.blog.entity.Category;
import com.curs.blog.entity.Tag;
import com.curs.blog.facade.IArticleFacade;
import com.curs.blog.security.UserDetailsImpl;
import com.curs.blog.service.CategoryService;
import com.curs.blog.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/editor")
@PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
public class EditorController {

    @Autowired
    private IArticleFacade articleFacade;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @GetMapping("/articles")
    public List<ArticleDto> getMyArticles(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return articleFacade.getArticlesByAuthor(userDetails.getId());
    }

    @PostMapping("/articles")
    public ArticleDto createArticle(@Valid @RequestBody ArticleDto articleDto, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return articleFacade.createArticle(articleDto, userDetails.getId());
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDto articleDto, Authentication authentication) {
        ArticleDto existing = articleFacade.getArticleById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Check ownership or admin
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!existing.getAuthorId().equals(userDetails.getId()) && !isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(articleFacade.updateArticle(id, articleDto));
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id, Authentication authentication) {
        ArticleDto existing = articleFacade.getArticleById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!existing.getAuthorId().equals(userDetails.getId()) && !isAdmin) {
            return ResponseEntity.status(403).build();
        }

        articleFacade.deleteArticle(id);
        return ResponseEntity.ok(new MessageResponse("Article deleted successfully"));
    }

    @PostMapping("/categories")
    public CategoryDto createCategory(@RequestBody CategoryDto dto) {
        Category category = categoryService.saveCategory(new Category(dto.getName()));
        return new CategoryDto(category.getId(), category.getName());
    }

    @PostMapping("/tags")
    public TagDto createTag(@RequestBody TagDto dto) {
        Tag tag = tagService.saveTag(new Tag(dto.getName()));
        return new TagDto(tag.getId(), tag.getName());
    }
}
