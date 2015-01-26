package net.madvirus.eval.web.restapi;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserFinderImplTest {

    private UserFinderImpl finder;
    private UserModelRepository mockUserModelRepository;

    @Before
    public void setUp() throws Exception {
        finder = new UserFinderImpl();
        mockUserModelRepository = mock(UserModelRepository.class);
        finder.setUserModelRepository(mockUserModelRepository);
    }

    @Test
    public void whenNoNames_thenShould_Return_EmptyResults() throws Exception {
        List<UserFindResult> results = finder.findUsersByName(new String[0]);
        assertThat(results, hasSize(0));
    }

    @Test
    public void whenAllNotFoundName_thenShould_Return_AllNotFoundResults() throws Exception {
        String[] names = {"계정없음1", "계정없음2"};
        List<UserFindResult> results = finder.findUsersByName(names);
        assertThat(results.get(0).isFound(), equalTo(false));
        assertThat(results.get(0).getName(), equalTo("계정없음1"));
        assertThat(results.get(0).getId(), nullValue());
    }

    @Test
    public void whenAllFoundName_thenShould_Return_AllFoundResults() throws Exception {
        when(mockUserModelRepository.findByName("계정1")).thenReturn(new UserModel("id1", "계정1", null));
        when(mockUserModelRepository.findByName("계정2")).thenReturn(new UserModel("id2", "계정2", null));

        String[] names = {"계정1", "계정2"};
        List<UserFindResult> results = finder.findUsersByName(names);
        assertThat(results.get(0).isFound(), equalTo(true));
        assertThat(results.get(0).getName(), equalTo("계정1"));
        assertThat(results.get(0).getId(), equalTo("id1"));
        assertThat(results.get(1).isFound(), equalTo(true));
        assertThat(results.get(1).getName(), equalTo("계정2"));
        assertThat(results.get(1).getId(), equalTo("id2"));
    }

}
