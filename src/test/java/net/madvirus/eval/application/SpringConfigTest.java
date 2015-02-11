package net.madvirus.eval.application;

import net.madvirus.eval.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class,
        locations = {"classpath:spring-dbconf-4-test.xml"})
@Sql("classpath:db-test-without-ad.sql")
public class SpringConfigTest {
    @Test
    public void done() {
    }
}
