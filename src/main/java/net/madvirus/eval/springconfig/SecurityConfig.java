package net.madvirus.eval.springconfig;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import net.madvirus.eval.system.security.UserDirectoryAuthenticationProvider;
import net.madvirus.eval.system.userdirectory.UserDirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDirectoryAuthenticationProvider userDirectoryAuthenticationProvider(UserDirectoryRepository userDirRepo) {
        UserDirectoryAuthenticationProvider provider = new UserDirectoryAuthenticationProvider();
        provider.setUserDirectoryRepository(userDirRepo);
        return provider;
    }

    @Autowired
    private SecuritySetting securitySetting;

    public static final String AUTHCOOKIENAME = "SEAT";

    @Configuration
    public static class UserModelAuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
        @Autowired
        private UserDirectoryAuthenticationProvider udap;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(udap);
        }

    }

    @Bean
    public CookieValueEncryptor cookieValueEncryptor() {
        return new CookieValueEncryptor(securitySetting.getAuthcookieKey(), securitySetting.getAuthcookieSalt());
    }

    @Bean
    public SecurityContextRepository customContextRepository() {
        return new CustomSecurityContextRepository(cookieValueEncryptor());
    }

    @Bean
    public CustomAuthSuccessHandler customAuthSuccessHandler() {
        return new CustomAuthSuccessHandler(cookieValueEncryptor());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.requestCache()
                .requestCache(new NullRequestCache());

        http.securityContext()
                .securityContextRepository(customContextRepository());

        http.formLogin()
                .successHandler(customAuthSuccessHandler())
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .failureUrl("/loginForm?error");

        http.logout()
                .logoutUrl("/logout")
                .deleteCookies("SEAT")
                .logoutSuccessUrl("/loggedOut");

        http.authorizeRequests()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/loggedOut").permitAll()
                .antMatchers("/loginForm").permitAll()
                .antMatchers("/admin").hasAnyRole("SYSTEMADMIN", "HRADMIN")
                .antMatchers("/admin/system").hasAnyRole("SYSTEMADMIN")
                .antMatchers("/admin/evalseasons").hasAnyRole("HRADMIN")
                .anyRequest().authenticated();
    }

    public static class CookieValueEncryptor {

        private final String key;
        private final String salt;

        public CookieValueEncryptor(String key, String salt) {
            this.key = key;
            this.salt = salt;
        }

        public String encrypt(String plainText) {
            TextEncryptor encryptor = Encryptors.text(key, salt);
            return encryptor.encrypt(plainText);
        }

        public String decrypt(String encryptedText) {
            TextEncryptor encryptor = Encryptors.text(key, salt);
            return encryptor.decrypt(encryptedText);
        }
    }

    public static class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
        private CookieValueEncryptor cookieValueEncryptor;

        public CustomAuthSuccessHandler(CookieValueEncryptor cookieValueEncryptor) {
            this.cookieValueEncryptor = cookieValueEncryptor;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            UserModel user = (UserModel) authentication.getPrincipal();
            try {
                Cookie authCookie = new Cookie(SecurityConfig.AUTHCOOKIENAME, URLEncoder.encode(encryptId(user), "UTF-8"));
                authCookie.setPath("/");
                response.addCookie(authCookie);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
            response.sendRedirect("/main");
        }

        private String encryptId(UserModel user) {
            return cookieValueEncryptor.encrypt(user.getId());
        }
    }

    public static class CustomSecurityContextRepository implements SecurityContextRepository {

        private JdbcTemplate jdbcTemplate;
        private UserModelRepository userModelRepository;
        private CookieValueEncryptor cookieValueEncryptor;

        public CustomSecurityContextRepository(CookieValueEncryptor cookieValueEncryptor) {
            this.cookieValueEncryptor = cookieValueEncryptor;
        }

        @Autowired
        public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @Autowired
        public void setUserModelRepository(UserModelRepository userModelRepository) {
            this.userModelRepository = userModelRepository;
        }

        @Override
        public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
            SecurityContext sc = SecurityContextHolder.createEmptyContext();
            Cookie cookie = findAuthCookie(requestResponseHolder.getRequest());
            if (cookie != null) {
                String id = getUserId(cookie);
                if (id != null) {
                    populateAuthentication(sc, id);
                }
            }
            return sc;
        }

        private void populateAuthentication(SecurityContext sc, String id) {
            UserModel userModel = userModelRepository.findOne(id);
            if (userModel != null) {
                List<String> authorities = jdbcTemplate.queryForList("select role from Authority where user_id = ?", String.class, id);
                List<SimpleGrantedAuthority> grantedAuthorities =
                        authorities
                                .stream()
                                .map(authority -> new SimpleGrantedAuthority(authority))
                                .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userModel, "", grantedAuthorities);
                sc.setAuthentication(auth);
            }
        }

        private String getUserId(Cookie cookie) {
            try {
                return cookieValueEncryptor.decrypt(URLDecoder.decode(cookie.getValue(), "UTF-8"));
            } catch(Exception ex) {
                return null;
            }
        }

        private Cookie findAuthCookie(HttpServletRequest request) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null || cookies.length == 0) return null;
            for (Cookie c : cookies) {
                if (c.getName().equals(SecurityConfig.AUTHCOOKIENAME)) {
                    return c;
                }
            }
            return null;
        }

        @Override
        public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
            // DO NOTHING
        }

        @Override
        public boolean containsContext(HttpServletRequest request) {
            return false;
        }
    }
}
