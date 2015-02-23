package net.madvirus.eval.testhelper;

import net.madvirus.eval.springconfig.SecurityConfig;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class AuthCookieHelper {

    private static SecurityConfig.CookieValueEncryptor encryptor = new SecurityConfig.CookieValueEncryptor("secretkey", "01234567890abcde");


    public static Cookie authCookie(String userId) throws UnsupportedEncodingException {
        return new Cookie(SecurityConfig.AUTHCOOKIENAME,
                encryptor.encrypt(URLEncoder.encode(userId, "UTF-8")));
    }

}
