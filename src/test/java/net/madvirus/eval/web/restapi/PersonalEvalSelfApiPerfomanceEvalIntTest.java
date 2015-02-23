package net.madvirus.eval.web.restapi;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.madvirus.eval.command.personaleval.self.UpdateSelfPerformanceEvalCommand;
import net.madvirus.eval.testhelper.AbstractRunReplayTest;
import net.madvirus.eval.testhelper.AuthCookieHelper;
import net.madvirus.eval.testhelper.CommandHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class PersonalEvalSelfApiPerfomanceEvalIntTest extends AbstractRunReplayTest {
    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(filterChainProxy).build();
    }

    @Test
    public void when_RateeUser_updateSelfPerfEval_then_ResponseShouldBe_OK() throws Exception {
        UpdateSelfPerformanceEvalCommand command = CommandHelper.updateSelfPerfEvalCommand(
                "EVAL-002", "ratee11", false, 10, 20);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        String json = mapper.writeValueAsString(command);

        mockMvc.perform(post("/api/evalseasons/EVAL-002/currentuser/personaleval/selfPerfEval")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(AuthCookieHelper.authCookie("ratee11")))
                .andExpect(status().isOk());
    }

    @Test
    public void when_NoRateeUser_UpdateSelfPerfEval_then_ResponseShouldBe_404() throws Exception {
        UpdateSelfPerformanceEvalCommand command = CommandHelper.updateSelfPerfEvalCommand(
                "EVAL-002", "ratee21", false, 10, 20);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        String json = mapper.writeValueAsString(command);

        mockMvc.perform(post("/api/evalseasons/EVAL-002/currentuser/personaleval/selfPerfEval")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(AuthCookieHelper.authCookie("ratee21")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void when_User_UpdateSelfPerfEval_To_NoEvalSeason_then_ResponseShouldBe_404() throws Exception {
        UpdateSelfPerformanceEvalCommand command = CommandHelper.updateSelfPerfEvalCommand(
                "EVAL-003", "ratee11", false, 10, 20);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        String json = mapper.writeValueAsString(command);

        mockMvc.perform(post("/api/evalseasons/EVAL-003/currentuser/personaleval/selfPerfEval")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(AuthCookieHelper.authCookie("ratee11")))
                .andExpect(status().isNotFound());
    }

}
