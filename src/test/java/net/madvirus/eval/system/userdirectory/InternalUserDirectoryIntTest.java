package net.madvirus.eval.system.userdirectory;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.testhelper.ESIntTestSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@ESIntTestSetup
@RunWith(SpringJUnit4ClassRunner.class)
public class InternalUserDirectoryIntTest {
    @Autowired
    private UserDirectoryRepository userDirectoryRepository;

    @Test
    public void should_Exists() throws Exception {
        UserDirectory userDirectory = userDirectoryRepository.findOne(1L);
        assertThat(userDirectory, notNullValue());
        assertThat(userDirectory, instanceOf(InternalUserDirectory.class));
        assertThat(userDirectory.isInternal(), equalTo(true));
    }

    @Test
    public void authenticate() throws Exception {
        InternalUserDirectory userDirectory = (InternalUserDirectory) userDirectoryRepository.findOne(1L);

        UserModel userModel = userDirectory.authenticate("ratee11", "1234");
        assertThat(userModel, notNullValue());

        assertThat(userDirectory.authenticate("ratee11", "12345"), nullValue());
    }

}
