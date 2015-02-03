package net.madvirus.eval.springconfig;

import net.madvirus.eval.axon.CustomEventSqlSchema;
import net.madvirus.eval.axon.ObjectMapperFactory;
import net.madvirus.eval.command.evalseason.EvalSeason;
import net.madvirus.eval.command.personaleval.PersonalEval;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.common.annotation.ClasspathParameterResolverFactory;
import org.axonframework.common.annotation.ParameterResolverFactory;
import org.axonframework.common.annotation.SpringBeanParameterResolverFactory;
import org.axonframework.common.jdbc.SpringDataSourceConnectionProvider;
import org.axonframework.common.jdbc.UnitOfWorkAwareConnectionProviderWrapper;
import org.axonframework.contextsupport.spring.ApplicationContextLookupParameterResolverFactory;
import org.axonframework.eventhandling.*;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventhandling.replay.BackloggingIncomingMessageHandler;
import org.axonframework.eventhandling.replay.ReplayingCluster;
import org.axonframework.eventsourcing.*;
import org.axonframework.eventstore.jdbc.DefaultEventEntryStore;
import org.axonframework.eventstore.jdbc.JdbcEventStore;
import org.axonframework.serializer.json.JacksonSerializer;
import org.axonframework.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Configuration
public class AxonInfraConfig implements BeanClassLoaderAware {
    private ClassLoader classLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
    }

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    @Bean(name = "__axon-annotation-command-handler-bean-post-processor")
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor(
            @Qualifier("axonParameterResolverFactory") ParameterResolverFactory parameterResolverFactory) {
        AnnotationCommandHandlerBeanPostProcessor postProcessor = new AnnotationCommandHandlerBeanPostProcessor();
        postProcessor.setParameterResolverFactory(parameterResolverFactory);
        postProcessor.setCommandBus(commandBus());
        return postProcessor;
    }

    @Bean(name = "__axon-annotation-event-listener-bean-post-processor")
    public AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor(
            @Qualifier("axonParameterResolverFactory") ParameterResolverFactory parameterResolverFactory) {
        AnnotationEventListenerBeanPostProcessor processor = new AnnotationEventListenerBeanPostProcessor();
        processor.setParameterResolverFactory(parameterResolverFactory);
        processor.setEventBus(eventBus());
        return processor;
    }

    @Bean
    public SpringTransactionManager axonTxMgr() {
        SpringTransactionManager txMgr = new SpringTransactionManager();
        txMgr.setTransactionManager(transactionManager);
        return txMgr;
    }

    @Bean
    public CommandBus commandBus() {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.setTransactionManager(axonTxMgr());
        return commandBus;
    }

    @Bean
    public CommandGateway commandGateway() {
        return new DefaultCommandGateway(commandBus());
    }

    @Bean
    @Qualifier(Constants.EVAL_SEASON_REPO_QUALIFIER)
    public EventSourcingRepository evalSeasonRepository() {
        EventSourcingRepository repository = new EventSourcingRepository(evalSeasonAggregateFactory(), jdbcEventStore());
        repository.setEventBus(eventBus());
        repository.setSnapshotterTrigger(snapshotterTrigger());
        return repository;
    }

    @Bean
    public GenericAggregateFactory evalSeasonAggregateFactory() {
        return new GenericAggregateFactory(EvalSeason.class);
    }

    private SnapshotterTrigger snapshotterTrigger() {
        EventCountSnapshotterTrigger trigger = new EventCountSnapshotterTrigger();
        trigger.setTrigger(2);
        trigger.setSnapshotter(aggregateSnapshotter());
        return trigger;
    }


    @Bean
    @Qualifier(Constants.PERSONAL_EVAL_REPO_QUALIFIER)
    public EventSourcingRepository personalEvalRepository() {
        EventSourcingRepository repository = new EventSourcingRepository(personalEvalAggregateFactory(), jdbcEventStore());
        repository.setEventBus(eventBus());
        return repository;
    }

    @Bean
    public GenericAggregateFactory personalEvalAggregateFactory() {
        return new GenericAggregateFactory(PersonalEval.class);
    }


    @Bean
    public SpringAggregateSnapshotter aggregateSnapshotter() {
        SpringAggregateSnapshotter snapshotter = new SpringAggregateSnapshotter();
        snapshotter.setExecutor(executor());
        snapshotter.setEventStore(jdbcEventStore());
        snapshotter.setTxManager(axonTxMgr());
        return snapshotter;
    }

    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        return executor;
    }

    @Bean
    public JdbcEventStore jdbcEventStore() {
        SpringDataSourceConnectionProvider connectionProvider =
                new SpringDataSourceConnectionProvider(dataSource);
        UnitOfWorkAwareConnectionProviderWrapper wrapper =
                new UnitOfWorkAwareConnectionProviderWrapper(connectionProvider);

        DefaultEventEntryStore eventEntryStore =
                new DefaultEventEntryStore(wrapper, sqlSchema());
        JdbcEventStore eventStore = new JdbcEventStore(eventEntryStore, jacksonEventSerializer());
        return eventStore;
    }

    @Bean
    public CustomEventSqlSchema sqlSchema() {
        CustomEventSqlSchema sqlSchema = new CustomEventSqlSchema(String.class);
        sqlSchema.setForceUtc(true);
        return sqlSchema;
    }

    @Bean
    public JacksonSerializer jacksonEventSerializer() {
        try {
            return new JacksonSerializer(new ObjectMapperFactory().getObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ClusteringEventBus eventBus() {
        ClusteringEventBus eventBus = new ClusteringEventBus(evalSeasonClusterSelector());
        return eventBus;
    }

    @Bean(name = Constants.EVAL_SEASON_CLUSTER_ID)
    public ReplayingCluster evalSeasonCluster() {
        ReplayingCluster cluster = new ReplayingCluster(
                evalSeasonTargetCluster(),
                jdbcEventStore(),
                axonTxMgr(),
                0,
                new BackloggingIncomingMessageHandler()
        );
        return cluster;
    }

    private Cluster evalSeasonTargetCluster() {
        SimpleCluster targetCluster = new SimpleCluster(Constants.EVAL_SEASON_CLUSTER_ID);
        return targetCluster;
    }

    @Bean(name = Constants.EVAL_SEASON_CLUSTER_ID + "$selector")
    public ClusterSelector evalSeasonClusterSelector() {
        List<ClusterSelector> selectors = new ArrayList<>();

        ClassNamePrefixClusterSelector selector1 = new ClassNamePrefixClusterSelector(
                "net.madvirus.eval.query.evalseason", evalSeasonCluster());
        selectors.add(selector1);

        OrderedClusterSelector selector = new OrderedClusterSelector(0, selectors);
        return selector;
    }


    private static final class OrderedClusterSelector implements Ordered, ClusterSelector {

        private final int order;
        private final List<ClusterSelector> selectors;

        private OrderedClusterSelector(int order, List<ClusterSelector> selectors) {
            this.order = order;
            this.selectors = new ArrayList<>(selectors);
        }

        @Override
        public int getOrder() {
            return order;
        }

        @Override
        public Cluster selectCluster(EventListener eventListener) {
            Cluster cluster = null;
            Iterator<ClusterSelector> selectorIterator = selectors.iterator();
            while (cluster == null && selectorIterator.hasNext()) {
                cluster = selectorIterator.next().selectCluster(eventListener);
            }
            return cluster;
        }
    }

    @Bean(name = "__axon-parameter-resolver-factory")
    @Qualifier("axonParameterResolverFactory")
    public ApplicationContextLookupParameterResolverFactory parameterResolverFactory() {
        return new ApplicationContextLookupParameterResolverFactory(
                Arrays.asList(
                        classpathParameterResolverFactory(),
                        springBeanParameterResolverFactory())
        );
    }

    @Bean
    public ParameterResolverFactory classpathParameterResolverFactory() {
        return ClasspathParameterResolverFactory.forClassLoader(classLoader);
    }

    @Bean
    public SpringBeanParameterResolverFactory springBeanParameterResolverFactory() {
        return new SpringBeanParameterResolverFactory();
    }
}
