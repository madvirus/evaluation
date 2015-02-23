package net.madvirus.eval.springconfig;

import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.user.UserModelRepository;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoaderImpl;
import net.madvirus.eval.web.dataloader.PersonalEvalDataLoader;
import net.madvirus.eval.web.dataloader.PersonalEvalDataLoaderImpl;
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

    @Autowired
    @Qualifier(Constants.PERSONAL_EVAL_REPO_QUALIFIER)
    private Repository<PersonalEval> personalEvalRepository;

    @Autowired
    private UserModelRepository userModelRepository;

    @Bean
    public EvalSeasonDataLoader evalSeasonDataLoader() {
        return new EvalSeasonDataLoaderImpl(
                evalSeasonRepository,
                evalSeasonMappingModelRepository,
                userModelRepository,
                personalEvalRepository);
    }

    @Bean
    public PersonalEvalDataLoader personalEvalDataLoader() {
        return new PersonalEvalDataLoaderImpl(
                personalEvalRepository,
                evalSeasonRepository,
                userModelRepository,
                evalSeasonMappingModelRepository);
    }
}
