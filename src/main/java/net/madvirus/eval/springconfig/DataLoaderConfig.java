package net.madvirus.eval.springconfig;

import net.madvirus.eval.command.evalseason.EvalSeason;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoaderImpl;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    @Autowired
    @Qualifier(Constants.EVAL_SEASON_REPO_QUALIFIER)
    private Repository<EvalSeason> evalSeasonRepository;

    @Autowired
    private EvalSeasonMappingModelRepository evalSeasonMappingModelRepository;

    @Bean
    public EvalSeasonDataLoader evalSeasonDataLoader() {
        return new EvalSeasonDataLoaderImpl(evalSeasonRepository, evalSeasonMappingModelRepository);
    }
}
