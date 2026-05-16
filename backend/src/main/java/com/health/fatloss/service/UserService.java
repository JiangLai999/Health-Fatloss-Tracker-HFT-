package com.health.fatloss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.health.fatloss.dto.LoginDTO;
import com.health.fatloss.dto.RegisterDTO;
import com.health.fatloss.dto.UserDTO;
import com.health.fatloss.entity.User;

public interface UserService extends IService<User> {
    void register(RegisterDTO registerDTO);
    String login(LoginDTO loginDTO);
    User getCurrentUser(Long userId);
    void updateUserInfo(Long userId, UserDTO userDTO);
}
