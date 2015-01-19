package net.madvirus.eval.application;

import net.madvirus.eval.web.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({
        "classpath:spring-axon-infra.xml",
        "classpath:spring-command-handler.xml",
        "classpath:spring-event-listener.xml",
        "classpath:spring-query-repository.xml"
})
@Import({WebMvcConfig.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
