package com.spring.jpa.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.spring.jpa.user.entity.User;
import com.spring.jpa.user.model.UserLoginToken;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Date;

@UtilityClass
public class JwtUtils {

    private static final String KEY = "hong";
    private static final String CLAIM_USER_ID = "user_id";

    public static String getIssuer(String token) {
        String issuer = JWT.require(Algorithm.HMAC512("hong".getBytes()))
                .build()
                .verify(token)
                .getIssuer();

        return issuer;
    }

    public static UserLoginToken createToken(User user) {
        if (user == null) {
            return null;
        }
        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expireDate = java.sql.Timestamp.valueOf(expiredDateTime);

        // 토큰 발행 시점
        String token = JWT.create()
                .withExpiresAt(expireDate)
                .withClaim(CLAIM_USER_ID, user.getId())
                .withSubject(user.getUsername())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512(KEY.getBytes()));

        return UserLoginToken.builder()
                .token(token)
                .build();
    }
}
