package net.madvirus.eval.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan({"net.madvirus.eval.web.restapi"})
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("admin/adminApp");
        registry.addViewController("/admin/home").setViewName("admin/adminHome");
        registry.addViewController("/admin/evalseasons").setViewName("admin/evalseason/evalseasons");
        registry.addViewController("/").setViewName("redirect:/main");
    }
}
