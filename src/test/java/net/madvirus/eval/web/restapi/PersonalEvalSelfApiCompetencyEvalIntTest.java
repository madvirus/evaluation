package net.madvirus.eval.web.restapi;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.madvirus.eval.api.personaleval.UpdateSelfCompetencyEvalCommand;
import net.madvirus.eval.api.personaleval.UpdateSelfPerformanceEvalCommand;
import net.madvirus.eval.query.evalseason.EvanSeasonMappingModelInitializer;
import net.madvirus.eval.testhelper.AbstractRunReplayTest;
import net.madvirus.eval.testhelper.CreationHelper;
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
public class PersonalEvalSelfApiCompetencyEvalIntTest extends AbstractRunReplayTest {
    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private EvanSeasonMappingModelInitializer initializer;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(filterChainProxy).build();
        initializer.replay();
    }

    @Test
    public void when_RateeUser_updateSelfCompeEval_then_ResponseShouldBe_OK() throws Exception {
        UpdateSelfCompetencyEvalCommand command = CreationHelper.updateSelfCompeEvalCommand("EVAL-002-ratee11", "EVAL-002", "ratee11", false, true, false);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        String json = mapper.writeValueAsString(command);

        mockMvc.perform(post("/api/personalevals/EVAL-002-ratee11/selfCompeEval")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(CreationHelper.authCookie("ratee11")))
                .andExpect(status().isOk());
    }

    @Test
    public void when_NoRateeUser_UpdateSelfCompeEval_then_ResponseShouldBe_404() throws Exception {
        UpdateSelfCompetencyEvalCommand command = CreationHelper.updateSelfCompeEvalCommand("EVAL-002-ratee21", "EVAL-002", "ratee21", false, true, false);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        String json = mapper.writeValueAsString(command);

        mockMvc.perform(post("/api/personalevals/EVAL-002-ratee21/selfCompeEval")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(CreationHelper.authCookie("ratee21")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void when_User_UpdateSelfCompeEval_To_NoEvalSeason_then_ResponseShouldBe_404() throws Exception {
        UpdateSelfCompetencyEvalCommand command = CreationHelper.updateSelfCompeEvalCommand("EVAL-003-ratee11", "EVAL-003", "ratee11", false, true, false);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        String json = mapper.writeValueAsString(command);

        mockMvc.perform(post("/api/personalevals/EVAL-003-ratee11/selfCompeEval")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(CreationHelper.authCookie("ratee11")))
                .andExpect(status().isNotFound());
    }

}
