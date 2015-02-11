package net.madvirus.eval.springconfig;

import net.madvirus.eval.api.evalseaon.EvalSeason;
import net.madvirus.eval.api.personaleval.PersonalEval;
import net.madvirus.eval.command.evalseason.CreateEvalSeasonCommandHandler;
import net.madvirus.eval.command.evalseason.UpdateMappingCommandHandler;
import net.madvirus.eval.command.personaleval.RejectEvalCommandHandler;
import net.madvirus.eval.command.personaleval.UpdateColleagueEvalCommandHandler;
import net.madvirus.eval.command.personaleval.UpdateFirstEvalCommandHandler;
import net.madvirus.eval.command.personaleval.UpdateSelfEvalCommandHandler;
import net.madvirus.eval.query.user.UserModelRepository;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandlerFactoryBean;
import org.axonframework.common.annotation.ParameterResolverFactory;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonCommandHandlerConfig {
    @Autowired
    @Qualifier(Constants.EVAL_SEASON_REPO_QUALIFIER)
    private Repository<EvalSeason> evalSeasonRepository;

    @Autowired
    @Qualifier(Constants.PERSONAL_EVAL_REPO_QUALIFIER)
    private Repository<PersonalEval> personalEvalRepository;

    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private EvalSeasonDataLoader evalSeasonDataLoader;

    @Bean
    public CreateEvalSeasonCommandHandler createEvalSeasonCommandHandler() {
        return new CreateEvalSeasonCommandHandler(evalSeasonRepository);
    }

    @Bean
    public UpdateMappingCommandHandler updateMappingCommandHandler() {
        return new UpdateMappingCommandHandler(
                evalSeasonRepository, userModelRepository
        );
    }

    @Bean
    public AggregateAnnotationCommandHandlerFactoryBean evalSeasonAggreagteCommandHandler(
            @Qualifier("axonParameterResolverFactory") ParameterResolverFactory parameterResolverFactory) {
        AggregateAnnotationCommandHandlerFactoryBean factory = new AggregateAnnotationCommandHandlerFactoryBean();
        factory.setAggregateType(EvalSeason.class);
        factory.setRepository(evalSeasonRepository);
        factory.setCommandBus(commandBus);
        factory.setParameterResolverFactory(parameterResolverFactory);
        return factory;
    }

    @Bean
    public UpdateSelfEvalCommandHandler updateSelfEvalCommandHandler() {
        return new UpdateSelfEvalCommandHandler(personalEvalRepository, evalSeasonRepository);
    }

    @Bean
    public AggregateAnnotationCommandHandlerFactoryBean personalEvalAggreateCommandHandler(
            @Qualifier("axonParameterResolverFactory") ParameterResolverFactory parameterResolverFactory) {
        AggregateAnnotationCommandHandlerFactoryBean factory = new AggregateAnnotationCommandHandlerFactoryBean();
        factory.setAggregateType(PersonalEval.class);
        factory.setRepository(evalSeasonRepository);
        factory.setCommandBus(commandBus);
        factory.setParameterResolverFactory(parameterResolverFactory);
        return factory;
    }

    @Bean
    public UpdateFirstEvalCommandHandler updateFirstEvalCommandHandler() {
        return new UpdateFirstEvalCommandHandler(personalEvalRepository);
    }

    @Bean
    public RejectEvalCommandHandler rejectEvalCommandHandler() {
        return new RejectEvalCommandHandler(personalEvalRepository);
    }

    @Bean
    public UpdateColleagueEvalCommandHandler updateColleagueEvalCommandHandler() {
        return new UpdateColleagueEvalCommandHandler(personalEvalRepository, evalSeasonRepository);
    }
}
