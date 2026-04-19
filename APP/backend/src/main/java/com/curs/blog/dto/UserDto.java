package com.curs.blog.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private boolean blocked;
    private Set<String> roles;
}
