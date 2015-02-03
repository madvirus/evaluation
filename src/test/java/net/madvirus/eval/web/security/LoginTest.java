package net.madvirus.eval.web.security;

import net.madvirus.eval.testhelper.AbstractIntTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
public class LoginTest extends AbstractIntTest {
    // http://stackoverflow.com/questions/23335200/spring-boot-setup-security-for-testing
    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(filterChainProxy).build();
    }

    @Test
    public void getLogin_shouldResponseLoginForm() throws Exception {
        mockMvc.perform(get("/loginForm"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getLogout_shouldRedirectLoggedOut_And_DeleteAuthCookie() throws Exception {
        mockMvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(redirectedUrl("/loggedOut"))
                .andExpect(cookie().exists("SEAT"))
                .andExpect(cookie().path("SEAT", "/"))
                .andExpect(cookie().maxAge("SEAT", 0))
        ;
    }

    @Test
    public void postValidAuthToLogin_ShouldRedirectMain_And_CreateAuthCookie() throws Exception {
        mockMvc.perform(post("/login").param("username", "ratee11").param("password", "1234"))
                .andDo(print())
                .andExpect(redirectedUrl("/main"))
                .andExpect(cookie().exists("SEAT"))
                .andExpect(cookie().path("SEAT", "/"))
        ;
    }

    @Test
    public void postBadAuthToLogin_ShouldRedirectMain_And_CreateAuthCookie() throws Exception {
        mockMvc.perform(post("/login").param("username", "ratee11").param("password", "invalid"))
                .andDo(print())
                .andExpect(redirectedUrl("/loginForm?error"))
        ;
    }
}
