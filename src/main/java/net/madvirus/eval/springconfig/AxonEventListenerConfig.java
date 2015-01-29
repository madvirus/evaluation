package net.madvirus.eval.springconfig;

import net.madvirus.eval.query.evalseason.EvalSeasonMappingEventListener;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.evalseason.EvanSeasonMappingModelInitializer;
import net.madvirus.eval.query.user.UserModelRepository;
import org.axonframework.eventhandling.replay.ReplayingCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonEventListenerConfig {

    @Autowired
    private EvalSeasonMappingModelRepository mappingModelRepository;
    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    @Qualifier(Constants.EVAL_SEASON_CLUSTER_ID)
    private ReplayingCluster evalSeasonCluster;

    @Bean
    public EvalSeasonMappingEventListener evalSeasonMappingEventListener() {
        return new EvalSeasonMappingEventListener(
                mappingModelRepository,
                userModelRepository
        );
    }

    @Bean
    public EvanSeasonMappingModelInitializer mappingModelInitializer() {
        return new EvanSeasonMappingModelInitializer(evalSeasonCluster);
    }
}
