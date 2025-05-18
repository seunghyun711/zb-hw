package com.spring.jpa.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCrypt;

@UtilityClass
public class PasswordUtils {
    // 패스워드 암호화 메서드

    // 입력한 패스워드를 해시된 패스워드랑 비교하는 메서드
    public static boolean equalPassword(String password, String encryptedPassword) {
        return BCrypt.checkpw(password, encryptedPassword);
    }
}
