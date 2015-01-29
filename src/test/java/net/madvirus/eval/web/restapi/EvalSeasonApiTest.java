package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.command.evalseason.AleadyEvaluationOpenedException;
import net.madvirus.eval.command.evalseason.EvalSeason;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.dataloader.EvalSeasonData;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import net.madvirus.eval.web.dataloader.EvalSeasonSimpleData;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.AggregateNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import scala.collection.immutable.Set;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EvalSeasonApiTest {

    private MockMvc mockMvc;
    private CommandGateway mockCommandGateway;
    private EvalSeasonDataLoader mockEvalSeaonModelDataLoader;

    @Before
    public void setUp() throws Exception {
        EvalSeasonApi api = new EvalSeasonApi();

        mockEvalSeaonModelDataLoader = mock(EvalSeasonDataLoader.class);
        mockCommandGateway = mock(CommandGateway.class);

        api.setEvalSeasonDataLoader(mockEvalSeaonModelDataLoader);
        api.setGateway(mockCommandGateway);
        mockMvc = MockMvcBuilders.standaloneSetup(api).build();
    }

    @Test
    public void get_ShouldResponseJsonFormattedList() throws Exception {
        when(mockEvalSeaonModelDataLoader.loadAll()).thenReturn(
                Arrays.asList(
                        dto("ID1", "이름1", false),
                        dto("ID2", "이름2", false)
                )
        );

        mockMvc.perform(get("/api/evalseasons"))
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("ID1"))
                .andExpect(jsonPath("$[0].name").value("이름1"))
                .andExpect(jsonPath("$[0].opened").value(false))
        ;
    }

    private EvalSeasonSimpleData dto(String id, String name, boolean opened) {
        return new EvalSeasonSimpleData(evalSeason(id, name, opened));
    }

    private EvalSeason evalSeason(String id, String name, boolean opened) {
        return new EvalSeason() {
            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean isOpened() {
                return opened;
            }
        };
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

    @Test
    public void getDetail_ShouldResponse404_whenEvalSeasonNotFound() throws Exception {
        when(mockEvalSeaonModelDataLoader.load("EVAL2014")).thenReturn(Optional.<EvalSeasonData>empty());

        mockMvc.perform(get("/api/evalseasons/EVAL2014"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDetail_ShouldResponseJson_whenEvalSeasonFound() throws Exception {
        EvalSeasonMappingModel mappingModel = new EvalSeasonMappingModel("EVAL2014", 0L)
                .updateMapping(new RateeMappingModel(
                        userModel("ratee2", "피평가자2"), RateeType.MEMBER, userModel("rater1", "평가자1"), userModel("rater2", "평가자2"),
                        new Set.Set1<UserModel>(userModel("colleague1", "동료1"))), 1L)
                .updateMapping(new RateeMappingModel(
                        userModel("ratee1", "피평가자1"), RateeType.MEMBER, userModel("rater1", "평가자1"), userModel("rater2", "평가자2"),
                        new Set.Set2<UserModel>(userModel("colleague2", "동료2"), userModel("colleague1", "동료1"))), 2L);

        EvalSeasonData value = new EvalSeasonData(evalSeason("EVAL2014", "이름", false), mappingModel);
        when(mockEvalSeaonModelDataLoader.load("EVAL2014")).thenReturn(Optional.of(value));

        mockMvc.perform(get("/api/evalseasons/EVAL2014"))
                .andExpect(jsonPath("$.id").value("EVAL2014"))
                .andExpect(jsonPath("$.name").value("이름"))
                .andExpect(jsonPath("$.opened").value(false))
                .andExpect(jsonPath("$.mappings").value(hasSize(2)))
                .andExpect(jsonPath("$.mappings[0].ratee.id").value("ratee1"))
                .andExpect(jsonPath("$.mappings[0].ratee.name").value("피평가자1"))
                .andExpect(jsonPath("$.mappings[0].colleagueRaters[0].name").value("동료1"))
                .andExpect(jsonPath("$.mappings[0].colleagueRaters[1].name").value("동료2"))
                .andExpect(jsonPath("$.mappings[1].ratee.id").value("ratee2"))
                .andExpect(jsonPath("$.mappings[1].ratee.name").value("피평가자2"))
                .andExpect(jsonPath("$.mappings[1].colleagueRaters[0].name").value("동료1"))
        ;
    }

    private UserModel userModel(String userId, String userName) {
        return new UserModel(userId, userName, "password");
    }

    @Test
    public void putOpen_shouldReturn404_whenNotFound() throws Exception {
        when(mockCommandGateway.sendAndWait(any())).thenThrow(new AggregateNotFoundException("NOID", "NOT FOUND"));

        mockMvc.perform(put("/api/evalseasons/EVAL2014?action=open"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void putOpen_shouldReturn404_whenAleadyOpened() throws Exception {
        when(mockCommandGateway.sendAndWait(any())).thenThrow(new AleadyEvaluationOpenedException());

        mockMvc.perform(put("/api/evalseasons/EVAL2014?action=open"))
                .andExpect(status().isConflict());
    }

    @Test
    public void putOpen_shouldReturn200_whenNotYetOpened() throws Exception {
        mockMvc.perform(put("/api/evalseasons/EVAL2014?action=open"))
                .andExpect(status().isOk());
    }
}
