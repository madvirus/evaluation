package net.madvirus.eval.springconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = ContainerJasperSetting.PREFIX)
public class ContainerJasperSetting {
    public static final String PREFIX = "jsp.jasper";

    private Integer checkInterval;
    private Boolean development;
    private Integer modificationTestInterval;

    public boolean hasCheckInterval() {
        return checkInterval != null;
    }

    public Integer getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(Integer checkInterval) {
        this.checkInterval = checkInterval;
    }

    public boolean hasDevelopment() {
        return development != null;
    }

    public Boolean getDevelopment() {
        return development;
    }

    public void setDevelopment(Boolean development) {
        this.development = development;
    }

    public boolean hasModificationTestInterval() {
        return modificationTestInterval != null;
    }

    public Integer getModificationTestInterval() {
        return modificationTestInterval;
    }

    public void setModificationTestInterval(Integer modificationTestInterval) {
        this.modificationTestInterval = modificationTestInterval;
    }

    @Override
    public String toString() {
        return "ContainerJasperSetting{" +
                "checkInterval=" + checkInterval +
                ", development=" + development +
                ", modificationTestInterval=" + modificationTestInterval +
                '}';
    }
}
