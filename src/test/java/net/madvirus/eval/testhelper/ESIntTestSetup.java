package net.madvirus.eval.testhelper;

import net.madvirus.eval.Application;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SpringApplicationConfiguration(classes = Application.class, locations = "classpath:spring-dbconf-4-test.xml")
@Sql("classpath:db-test.sql")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ESIntTestSetup {
}
