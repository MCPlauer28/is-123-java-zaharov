package com.curs.blog.service;

import com.curs.blog.entity.Article;
import com.curs.blog.entity.EArticleStatus;
import com.curs.blog.repository.ArticleRepository;
import com.curs.blog.service.impl.ArticleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    public void testGetPublishedArticles() {
        Pageable pageable = PageRequest.of(0, 10);
        Article article = new Article();
        article.setStatus(EArticleStatus.PUBLISHED);
        Page<Article> page = new PageImpl<>(Collections.singletonList(article));

        when(articleRepository.findByStatus(EArticleStatus.PUBLISHED, pageable)).thenReturn(page);

        Page<Article> result = articleService.getPublishedArticles(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(articleRepository, times(1)).findByStatus(EArticleStatus.PUBLISHED, pageable);
    }

    @Test
    public void testGetArticleById() {
        Article article = new Article();
        article.setId(1L);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        Optional<Article> result = articleService.getArticleById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }
}
