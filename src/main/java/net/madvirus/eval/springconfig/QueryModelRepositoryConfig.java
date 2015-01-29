package net.madvirus.eval.springconfig;

import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryModelRepositoryConfig {

    @Bean
    public EvalSeasonMappingModelRepository evalSeasonMappingModelRepository() {
        return new EvalSeasonMappingModelRepositoryImpl();
    }

}
