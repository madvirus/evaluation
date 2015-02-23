package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.user.MappingUserIdNotFoundException;
import net.madvirus.eval.web.MockMvcUtil;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.AggregateNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EvalSeasonMappingApiTest {

    private MockMvc mockMvc;
    private CommandGateway mockGateway;

    @Before
    public void setUp() throws Exception {
        EvalSeasonMappingApi api = new EvalSeasonMappingApi();
        mockGateway = mock(CommandGateway.class);
        api.setGateway(mockGateway);
        mockMvc = MockMvcUtil.mockMvc(api);
    }

    @Test
    public void post_shouldReturn404_whenNotFoundSeasonId() throws Exception {
        when(mockGateway.sendAndWait(any())).thenThrow(new AggregateNotFoundException("NOID", "NOT FOUND"));
        mockMvc.perform(post("/api/evalseasons/NOID/mappings").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void post_shouldReturn400_whenFoundSeason_butNotFoundUser() throws Exception {
        when(mockGateway.sendAndWait(any()))
                .thenThrow(new MappingUserIdNotFoundException("noRatee"));
        String content = "{\"rateeMappings\": [" +
                "{\"rateeId\": \"noRatee\", \"type\":\"MEMBER\", \"firstRaterId\":\"rater1\", \"secondRaterId\":\"rater2\"}" +
                "]}";
        mockMvc.perform(post("/api/evalseasons/ID/mappings").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ids").value(hasSize(1)))
                .andExpect(jsonPath("$.ids[0]").value("noRatee"))
        ;
    }

    @Test
    public void post_shouldReturn200_whenUpdateSuccess() throws Exception {
        String content = "{\"rateeMappings\":[{\"rateeId\":\"retee11\",\"type\":\"MEMBER\",\"secondRaterId\":\"rater2\",\"firstRaterId\":\"rater11\",\"colleagueRaterIds\":[\"ratee12\"]}]}";
        mockMvc.perform(post("/api/evalseasons/ID/mappings").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void delete_shouldReturn404_whenNotFoundSeason() throws Exception {
        when(mockGateway.sendAndWait(any())).thenThrow(new AggregateNotFoundException("NOID", "NOT FOUND"));
        mockMvc.perform(delete("/api/evalseasons/NOID/mappings?ids=ratee1,ratee2"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void delete_shouldReturn200_whenDeleteSuccess() throws Exception {
        mockMvc.perform(delete("/api/evalseasons/ID/mappings?ids=ratee1,ratee2"))
                .andExpect(status().isOk())
        ;
    }
}
