package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.query.evalseason.EvalSeasonModel;
import net.madvirus.eval.query.evalseason.EvalSeasonModelRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EvalSeasonApiTest {

    private MockMvc mockMvc;
    private CommandGateway mockCommandGateway;

    @Before
    public void setUp() throws Exception {
        EvalSeasonApi api = new EvalSeasonApi();
        EvalSeasonModelRepository mockEvalSeaonModelRepository = mock(EvalSeasonModelRepository.class);
        when(mockEvalSeaonModelRepository.findAll()).thenReturn(
                Arrays.asList(
                        new EvalSeasonModel("ID1", "이름1"),
                        new EvalSeasonModel("ID2", "이름2")
                )
        );
        api.setEvalSeasonModelRepository(mockEvalSeaonModelRepository);

        mockCommandGateway = mock(CommandGateway.class);
        api.setGateway(mockCommandGateway);
        mockMvc = MockMvcBuilders.standaloneSetup(api).build();
    }

    @Test
    public void get_ShouldResponseJsonFormattedList() throws Exception {
        mockMvc.perform(get("/api/evalseasons"))
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("ID1"))
                .andExpect(jsonPath("$[0].name").value("이름1"))
                .andExpect(jsonPath("$[0].opened").value(false))
        ;
    }

    @Test
    public void post_ShouldResponseCreated_When_Successful() throws Exception {
        mockMvc.perform(post("/api/evalseasons").contentType(MediaType.APPLICATION_JSON).content("{\"evalSeasonId\": \"ID1\", \"name\": \"이름1\"}"))
                .andExpect(status().isCreated())
        ;
    }
    @Test
    public void post_ShouldResponseConflict_When_Duplicated() throws Exception {
        when(mockCommandGateway.sendAndWait(any())).thenThrow(new DuplicateIdException(""));
        mockMvc.perform(post("/api/evalseasons").contentType(MediaType.APPLICATION_JSON).content("{\"evalSeasonId\": \"ID1\", \"name\": \"이름1\"}"))
                .andExpect(status().isConflict())
        ;
    }
}
