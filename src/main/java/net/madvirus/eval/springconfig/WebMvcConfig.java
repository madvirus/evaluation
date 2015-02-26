package net.madvirus.eval.springconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("admin/adminMain");
        registry.addViewController("/admin/evalseasons").setViewName("admin/adminEvalSeasonApp");
        registry.addViewController("/").setViewName("redirect:/main");
        registry.addViewController("/loginForm").setViewName("auth/loginForm");
        registry.addViewController("/loggedOut").setViewName("auth/loggedOut");
        registry.addViewController("/error/403").setViewName("error/403");
        registry.addViewController("/error/404").setViewName("error/404");
        registry.addViewController("/error/404").setViewName("error/500");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/view/", ".jsp");
    }

}
