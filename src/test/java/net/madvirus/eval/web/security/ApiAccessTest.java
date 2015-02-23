package net.madvirus.eval.web.security;

import net.madvirus.eval.testhelper.AbstractIntTest;
import net.madvirus.eval.testhelper.AuthCookieHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class ApiAccessTest extends AbstractIntTest {
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
    public void anonymousUser_Access_Api_then_Should_Response_Forbidden() throws Exception {
        mockMvc.perform(get("/api/evalseasons"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void noHrAdmin_Access_Evalseasons_POST_then_Should_Response_Forbidden() throws Exception {
        mockMvc.perform(post("/api/evalseasons").cookie(AuthCookieHelper.authCookie("ratee11")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void noHrAdmin_Access_Evalseasons_PUT_then_Should_Response_Forbidden() throws Exception {
        mockMvc.perform(put("/api/evalseasons/EVAL2014").cookie(AuthCookieHelper.authCookie("ratee11")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void noHrAdmin_Access_EvalseasonMapping_PUT_then_Should_Response_Forbidden() throws Exception {
        mockMvc.perform(put("/api/evalseasons/EVAL2014/mappings").cookie(AuthCookieHelper.authCookie("ratee11")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void noHrAdmin_Access_EvalseasonMapping_DELETE_then_Should_Response_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/evalseasons/EVAL2014/mappings").cookie(AuthCookieHelper.authCookie("ratee11")))
                .andExpect(status().isForbidden());
    }

}
