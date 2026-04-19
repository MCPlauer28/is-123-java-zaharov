package com.curs.blog.controller;

import com.curs.blog.dto.ArticleDto;
import com.curs.blog.dto.UserDto;
import com.curs.blog.facade.ArticleFacade;
import com.curs.blog.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private ArticleFacade articleFacade;

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userFacade.getAllUsers();
    }

    @PostMapping("/users/{id}/toggle-block")
    public UserDto toggleBlock(@PathVariable Long id) {
        return userFacade.toggleBlock(id);
    }

    @GetMapping("/articles")
    public List<ArticleDto> getAllArticles() {
        return articleFacade.getAllArticles();
    }
}
