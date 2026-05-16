package com.health.fatloss.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.health.fatloss.common.Result;
import com.health.fatloss.dto.*;
import com.health.fatloss.entity.User;
import com.health.fatloss.mapper.UserMapper;
import com.health.fatloss.service.UserService;
import com.health.fatloss.utils.JwtUtils;
import com.health.fatloss.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SmsUtils smsUtils;

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        if (loginDTO.getUsername().matches("^1[3-9]\\d{9}$")) {
            queryWrapper.eq(User::getPhone, loginDTO.getUsername());
        } else {
            queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        }
        
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return Result.error("账号或密码错误");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("账号或密码错误");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("nickname", user.getNickname());
        
        return Result.success("登录成功", data);
    }

    @PostMapping("/send-code")
    public Result<Map<String, Object>> sendCode(@Valid @RequestBody SendCodeDTO sendCodeDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, sendCodeDTO.getPhone());
        User user = userMapper.selectOne(queryWrapper);

        if ("register".equals(sendCodeDTO.getType())) {
            if (user != null) {
                return Result.error("该手机号已注册");
            }
        } else if ("reset".equals(sendCodeDTO.getType())) {
            if (user == null) {
                return Result.error("该手机号未注册");
            }
        }

        String code = smsUtils.generateCode(sendCodeDTO.getPhone());
        smsUtils.sendSms(sendCodeDTO.getPhone(), code);
        
        Map<String, Object> data = new HashMap<>();
        data.put("phone", sendCodeDTO.getPhone());
        data.put("code", code);
        
        return Result.success("验证码已发送", data);
    }

    @PostMapping("/reset-password")
    public Result<String> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        if (!smsUtils.verifyCode(resetPasswordDTO.getPhone(), resetPasswordDTO.getVerifyCode())) {
            return Result.error("验证码错误或已过期");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, resetPasswordDTO.getPhone());
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return Result.error("用户不存在");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userMapper.updateById(user);

        return Result.success("密码重置成功");
    }

    @GetMapping("/check-phone")
    public Result<Map<String, Object>> checkPhone(@RequestParam String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(queryWrapper);
        
        Map<String, Object> data = new HashMap<>();
        data.put("exists", user != null);
        
        return Result.success(data);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo() {
        Long userId = getCurrentUserId();
        User user = userService.getCurrentUser(userId);
        return Result.success(user);
    }

    @PutMapping("/info")
    public Result<String> updateUserInfo(@Valid @RequestBody UserDTO userDTO) {
        Long userId = getCurrentUserId();
        userService.updateUserInfo(userId, userDTO);
        return Result.success("更新成功");
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
