package com.curs.blog.facade;

import com.curs.blog.dto.UserDto;
import com.curs.blog.entity.User;
import com.curs.blog.facade.mapper.UserMapper;
import com.curs.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserFacade implements IUserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users for admin");
        return userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto toggleBlock(Long id) {
        log.info("Toggling block for user {}", id);
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBlocked(!user.isBlocked());
        User updated = userService.saveUser(user);
        log.info("User {} block status toggled to {}", id, updated.isBlocked());
        return userMapper.toDto(updated);
    }
}
