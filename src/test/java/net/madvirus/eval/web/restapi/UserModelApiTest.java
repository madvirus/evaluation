package net.madvirus.eval.web.restapi;

import net.madvirus.eval.web.MockMvcUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static net.madvirus.eval.web.restapi.UserFindResult.found;
import static net.madvirus.eval.web.restapi.UserFindResult.notFound;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserModelApiTest {
    private MockMvc mockMvc;
    private UserFinder mockUserFinder;

    @Before
    public void testCreate() throws Exception {
        UserModelApi api = new UserModelApi();
        mockUserFinder = mock(UserFinder.class);
        api.setUserFinder(mockUserFinder);

        mockMvc = MockMvcUtil.mockMvc(api);
    }

    @Test
    public void shouldResponse() throws Exception {
        when(mockUserFinder.findUsersByName(new String[]{"피평가자1", "평가자1", "계정없음", "피평가자2"}))
                .thenReturn(Arrays.asList(found("피평가자1", "ratee1"), found("평가자1", "rater1"), notFound("계정없음"), found("피평가자2", "ratee2")));

        mockMvc.perform(get("/api/users").param("op", "findId").param("name", "피평가자1,평가자1,계정없음,피평가자2"))
                .andExpect(jsonPath("$.[0].name").value("피평가자1"))
                .andExpect(jsonPath("$.[0].found").value(true))
                .andExpect(jsonPath("$.[0].id").value("ratee1"))
                .andExpect(jsonPath("$.[2].name").value("계정없음"))
                .andExpect(jsonPath("$.[2].found").value(false))
        ;
    }

}
