package com.example.springsecuritychapter11.user;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenerateUtil {


    public static String generateCode() {
        String code;

        try {
            SecureRandom random = SecureRandom.getInstanceStrong(); // 임의의 int 값을 생성하는 SecureRandom 의 인스턴스를 만든다.

            int c = random.nextInt(9000) + 1000; // 0~8,999 사이의 값을 생성하고 1,000을 더해서 1,000 ~ 9,999(4자리) 사이의 값을 얻는다.

            code = String.valueOf(c); // int 를 String으로 변환하고 반환한다.
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating the random code. ");
        }

        return code;
    }
}
