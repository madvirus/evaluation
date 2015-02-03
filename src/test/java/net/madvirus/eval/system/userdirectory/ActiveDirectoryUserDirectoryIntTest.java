package net.madvirus.eval.system.userdirectory;

import net.madvirus.eval.Application;
import net.madvirus.eval.query.user.UserModel;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@SpringApplicationConfiguration(classes = Application.class, locations = {"classpath:spring-dbconf-4-test.xml", "classpath:spring-axon-listener-4-test.xml"})
@Sql("classpath:db-test.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class ActiveDirectoryUserDirectoryIntTest {
    @Autowired
    private UserDirectoryRepository userDirectoryRepository;

    @Test
    public void authenticate() throws Exception {
        UserDirectory adUserDir = userDirectoryRepository.findOne(2L);
        UserModel userModel = adUserDir.authenticate("someId", "somePass");
        assertThat(userModel, notNullValue());

        assertThat(adUserDir.authenticate("someId", "someBadPass"), nullValue());
    }

}
