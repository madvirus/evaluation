package net.madvirus.eval.springconfig;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ContainerConfig {

    @Autowired
    private ContainerJasperSetting jasperSetting;

    @Bean
    public ServerProperties serverProperties() {
        return new ServerProperties();
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new ErrorPageCustomizer(tomcatJspServletConfig());
    }

    @Bean
    public TomcatJspServletConfig tomcatJspServletConfig() {
        return new TomcatJspServletConfig(jasperSetting);
    }

    private static class ErrorPageCustomizer implements EmbeddedServletContainerCustomizer {

        private TomcatJspServletConfig tomcatJspServletConfig;

        public ErrorPageCustomizer(TomcatJspServletConfig tomcatJspServletConfig) {
            this.tomcatJspServletConfig = tomcatJspServletConfig;
        }

        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"));
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
            container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcatContainer = (TomcatEmbeddedServletContainerFactory) container;
                tomcatContainer.addContextCustomizers(tomcatJspServletConfig);
            }
        }

    }

    public static class TomcatJspServletConfig implements TomcatContextCustomizer {

        private ContainerJasperSetting jasperSetting;

        public TomcatJspServletConfig(ContainerJasperSetting jasperSetting) {
            this.jasperSetting = jasperSetting;
        }

        @Override
        public void customize(Context context) {
            Wrapper jsp = (Wrapper)context.findChild("jsp");
            if (jasperSetting.hasCheckInterval()) {
                jsp.addInitParameter("checkInterval", jasperSetting.getCheckInterval().toString());
            }
            if (jasperSetting.hasModificationTestInterval()) {
                jsp.addInitParameter("modificationTestInterval", jasperSetting.getModificationTestInterval().toString());
            }
            if (jasperSetting.hasDevelopment()) {
                jsp.addInitParameter("development", jasperSetting.getDevelopment().toString());
            }
        }
    }

}
