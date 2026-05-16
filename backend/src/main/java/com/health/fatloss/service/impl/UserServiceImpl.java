package com.health.fatloss.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.fatloss.common.BusinessException;
import com.health.fatloss.dto.LoginDTO;
import com.health.fatloss.dto.RegisterDTO;
import com.health.fatloss.dto.UserDTO;
import com.health.fatloss.entity.User;
import com.health.fatloss.mapper.UserMapper;
import com.health.fatloss.service.UserService;
import com.health.fatloss.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    @Transactional
    public void register(RegisterDTO registerDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        this.save(user);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(queryWrapper);

        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        return jwtUtils.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public User getCurrentUser(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    @Transactional
    public void updateUserInfo(Long userId, UserDTO userDTO) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        BeanUtil.copyProperties(userDTO, user);
        this.updateById(user);
    }
}
