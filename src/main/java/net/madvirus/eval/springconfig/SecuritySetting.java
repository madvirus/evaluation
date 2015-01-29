package net.madvirus.eval.springconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = SecuritySetting.PREFIX)
public class SecuritySetting {
    public static final String PREFIX = "eval.security";

    private String authcookieKey;
    private String authcookieSalt;

    public String getAuthcookieKey() {
        return authcookieKey;
    }

    public void setAuthcookieKey(String authcookieKey) {
        this.authcookieKey = authcookieKey;
    }

    public String getAuthcookieSalt() {
        return authcookieSalt;
    }

    public void setAuthcookieSalt(String authcookieSalt) {
        this.authcookieSalt = authcookieSalt;
    }

}
