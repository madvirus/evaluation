package net.madvirus.eval.springconfig;

import net.madvirus.eval.command.evalseason.CreateEvalSeasonCommandHandler;
import net.madvirus.eval.command.evalseason.DeleteMappingCommandHandler;
import net.madvirus.eval.command.evalseason.UpdateDistributionRuleCommandHandler;
import net.madvirus.eval.command.evalseason.UpdateMappingCommandHandler;
import net.madvirus.eval.command.personaleval.*;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.user.UserModelRepository;
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
    private EvalSeasonMappingModelRepository mappingModelRepository;

    @Autowired
    private CommandBus commandBus;

    @Bean
    public CreateEvalSeasonCommandHandler createEvalSeasonCommandHandler() {
        return new CreateEvalSeasonCommandHandler(evalSeasonRepository);
    }

    @Bean
    public UpdateMappingCommandHandler updateMappingCommandHandler() {
        return new UpdateMappingCommandHandler(
                evalSeasonRepository, personalEvalRepository, userModelRepository, mappingModelRepository
        );
    }

    @Bean
    public DeleteMappingCommandHandler deleteMappingCommandHandler() {
        return new DeleteMappingCommandHandler(evalSeasonRepository, personalEvalRepository);
    }

    @Bean
    public UpdateDistributionRuleCommandHandler updateDistributionRuleCommandHandler() {
        return new UpdateDistributionRuleCommandHandler(evalSeasonRepository, personalEvalRepository);
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

    @Bean
    public UpdateSecondEvalCommandHandler updateSecondEvalCommandHandler() {
        return new UpdateSecondEvalCommandHandler(personalEvalRepository);
    }
}
