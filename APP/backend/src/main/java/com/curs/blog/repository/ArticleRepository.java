package com.curs.blog.repository;

import com.curs.blog.entity.Article;
import com.curs.blog.entity.EArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByStatus(EArticleStatus status, Pageable pageable);
    Page<Article> findByStatusAndCategoryId(EArticleStatus status, Long categoryId, Pageable pageable);
    Page<Article> findByStatusAndTagsId(EArticleStatus status, Long tagId, Pageable pageable);
    List<Article> findByAuthorId(Long authorId);
}
