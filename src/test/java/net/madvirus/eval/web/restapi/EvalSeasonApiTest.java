package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.*;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.MockMvcUtil;
import net.madvirus.eval.web.dataloader.EvalSeasonData;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import net.madvirus.eval.web.dataloader.EvalSeasonSimpleData;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.AggregateNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import scala.collection.immutable.Set;

import java.util.Arrays;

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
        mockMvc = MockMvcUtil.mockMvc(api);
    }

    @Test
    public void getEvalseasons_Should_Response_JsonFormattedList() throws Exception {
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
    public void postEvalseasons_Should_Response_Created_When_success() throws Exception {
        mockMvc.perform(post("/api/evalseasons").contentType(MediaType.APPLICATION_JSON).content("{\"evalSeasonId\": \"ID1\", \"name\": \"이름1\"}"))
                .andExpect(status().isCreated())
        ;
    }

    @Test
    public void postEvalSeasons_Should_Response_Conflict_When_dup() throws Exception {
        when(mockCommandGateway.sendAndWait(any())).thenThrow(new DuplicateIdException(""));

        mockMvc.perform(post("/api/evalseasons").contentType(MediaType.APPLICATION_JSON).content("{\"evalSeasonId\": \"ID1\", \"name\": \"이름1\"}"))
                .andExpect(status().isConflict())
        ;
    }

    @Test
    public void getDetail_Should_Response_404_When_EvalSeasonNotFound() throws Exception {
        when(mockEvalSeaonModelDataLoader.load("EVAL2014")).thenThrow(new EvalSeasonNotFoundException());

        mockMvc.perform(get("/api/evalseasons/EVAL2014"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDetail_Should_Response_Json_when_EvalSeasonFound() throws Exception {
        EvalSeasonMappingModel mappingModel = new EvalSeasonMappingModel("EVAL2014", 0L)
                .updateMapping(new RateeMappingModel(
                        userModel("ratee2", "피평가자2"), RateeType.MEMBER, userModel("rater1", "평가자1"), userModel("rater2", "평가자2"),
                        new Set.Set1<UserModel>(userModel("colleague1", "동료1"))), 1L)
                .updateMapping(new RateeMappingModel(
                        userModel("ratee1", "피평가자1"), RateeType.MEMBER, userModel("rater1", "평가자1"), userModel("rater2", "평가자2"),
                        new Set.Set2<UserModel>(userModel("colleague2", "동료2"), userModel("colleague1", "동료1"))), 2L);

        EvalSeasonData value = new EvalSeasonData(evalSeason("EVAL2014", "이름", false), mappingModel);
        when(mockEvalSeaonModelDataLoader.load("EVAL2014")).thenReturn(value);

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
    public void putOpen_Should_Return_404_when_NotFound() throws Exception {
        when(mockCommandGateway.sendAndWait(any())).thenThrow(new AggregateNotFoundException("NOID", "NOT FOUND"));

        mockMvc.perform(put("/api/evalseasons/EVAL2014?action=open"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void putOpen_should_Return_Ok_when_AlreadyOpened() throws Exception {
        when(mockCommandGateway.sendAndWait(any())).thenThrow(new AlreadyEvaluationOpenedException());

        mockMvc.perform(put("/api/evalseasons/EVAL2014?action=open"))
                .andExpect(status().isOk());
    }

    @Test
    public void putOpen_should_Return_Ok_when_NotYetOpened() throws Exception {
        mockMvc.perform(put("/api/evalseasons/EVAL2014?action=open"))
                .andExpect(status().isOk());
    }

    @Test
    public void putStartColleagueEval_should_Return_Ok_when_Success() throws Exception {
        mockMvc.perform(put("/api/evalseasons/EVAL2014?action=startColleagueEval"))
                .andExpect(status().isOk());
    }

    @Test
    public void putStartColleagueEval_should_Return_Conflict_when_NotYetOpened() throws Exception {
        when(mockCommandGateway.sendAndWait(any())).thenThrow(new EvalSeasonNotYetOpenedException());
        mockMvc.perform(put("/api/evalseasons/EVAL2014?action=startColleagueEval"))
                .andExpect(status().isConflict());
    }

    @Test
    public void putStartColleagueEval_should_Return_Conflict_when_AlreadyStarted() throws Exception {
        when(mockCommandGateway.sendAndWait(any())).thenThrow(new ColleagueEvalAlreadyStartedException());
        mockMvc.perform(put("/api/evalseasons/EVAL2014?action=startColleagueEval"))
                .andExpect(status().isOk());
    }
}
