package com.curs.blog.facade;

import com.curs.blog.dto.UserDto;
import java.util.List;

public interface IUserFacade {
    List<UserDto> getAllUsers();
    UserDto toggleBlock(Long id);
}
