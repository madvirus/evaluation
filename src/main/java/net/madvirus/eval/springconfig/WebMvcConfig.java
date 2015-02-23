package net.madvirus.eval.springconfig;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new ErrorPageCustomizer();
    }

    private static class ErrorPageCustomizer implements EmbeddedServletContainerCustomizer {

        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"));
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
            container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
        }

    }
}
