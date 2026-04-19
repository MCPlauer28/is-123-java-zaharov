package com.curs.blog.facade;

import com.curs.blog.dto.UserDto;
import com.curs.blog.entity.Role;
import com.curs.blog.entity.User;
import com.curs.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFacade {

    @Autowired
    private UserService userService;

    public UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setBlocked(user.isBlocked());
        dto.setRoles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
        return dto;
    }

    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public UserDto toggleBlock(Long id) {
        User user = userService.getUserById(id).orElseThrow();
        user.setBlocked(!user.isBlocked());
        return convertToDto(userService.saveUser(user));
    }
}
