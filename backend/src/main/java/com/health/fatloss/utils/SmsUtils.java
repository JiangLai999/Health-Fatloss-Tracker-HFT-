package com.health.fatloss.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class SmsUtils {
    private static final Map<String, String> codeMap = new HashMap<>();
    private static final Map<String, Long> expireMap = new HashMap<>();
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000;

    public String generateCode(String phone) {
        String code = String.format("%06d", new Random().nextInt(999999));
        codeMap.put(phone, code);
        expireMap.put(phone, System.currentTimeMillis() + CODE_EXPIRE_TIME);
        return code;
    }

    public boolean verifyCode(String phone, String code) {
        String storedCode = codeMap.get(phone);
        Long expireTime = expireMap.get(phone);

        if (storedCode == null || expireTime == null) {
            return false;
        }

        if (System.currentTimeMillis() > expireTime) {
            codeMap.remove(phone);
            expireMap.remove(phone);
            return false;
        }

        boolean result = storedCode.equals(code);
        if (result) {
            codeMap.remove(phone);
            expireMap.remove(phone);
        }
        return result;
    }

    public void sendSms(String phone, String code) {
        System.out.println("========================================");
        System.out.println("发送短信到: " + phone);
        System.out.println("验证码: " + code);
        System.out.println("有效期: 5分钟");
        System.out.println("========================================");
    }
}
