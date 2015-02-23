package net.madvirus.eval.axon;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.command.evalseason.UpdateMappingCommand;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.springconfig.Constants;
import net.madvirus.eval.testhelper.AbstractRunReplayTest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.replay.ReplayingCluster;
import org.axonframework.eventsourcing.AggregateSnapshotter;
import org.axonframework.eventstore.management.Criteria;
import org.axonframework.repository.Repository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SnapshotterTest extends AbstractRunReplayTest {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private AggregateSnapshotter snapshotter;

    @Autowired
    @Qualifier(Constants.EVAL_SEASON_REPO_QUALIFIER)
    private Repository<EvalSeason> evalSeasonRepository;

    @Autowired
    private ReplayingCluster replayingCluster;

    @Test
    public void aggregateSnapshot() throws Exception {
        commandGateway.sendAndWait(createUpdateMappingsCommand());

        snapshotter.scheduleSnapshot(EvalSeason.class.getSimpleName(), "EVAL-001");

        Thread.sleep(2000);

        commandGateway.sendAndWait(createUpdateMappingsCommand2());

        snapshotter.scheduleSnapshot(EvalSeason.class.getSimpleName(), "EVAL-001");

        Thread.sleep(2000);

        runInUOW(() -> {
            EvalSeason season = evalSeasonRepository.load("EVAL-001");
            assertThat(season, notNullValue());
            assertThat(season.getVersion(), equalTo(2L));
        });

        Criteria criteria = replayingCluster.newCriteriaBuilder()
                .property("type").is("EvalSeason");
        replayingCluster.startReplay(criteria);

        Thread.sleep(2000);
    }

    private UpdateMappingCommand createUpdateMappingsCommand() {
        List<RateeMapping> rateeMappings = new ArrayList<>();

        rateeMappings.add(createRateeMapping11());
        rateeMappings.add(createRateeMapping12());
        UpdateMappingCommand cmd = new UpdateMappingCommand("EVAL-001", rateeMappings);
        return cmd;
    }

    private RateeMapping createRateeMapping11() {
        return new RateeMapping("ratee11", RateeType.TEAM_LEADER, "rater11", "rater2", "ratee12", "ratee21");
    }

    private RateeMapping createRateeMapping12() {
        return new RateeMapping("ratee12", RateeType.TEAM_LEADER, "rater11", "rater2", "ratee11");
    }

    private UpdateMappingCommand createUpdateMappingsCommand2() {
        List<RateeMapping> rateeMappings = new ArrayList<>();
        rateeMappings.add(createRateeMapping21());
        rateeMappings.add(createRateeMapping22());
        UpdateMappingCommand cmd = new UpdateMappingCommand("EVAL-001", rateeMappings);
        return cmd;
    }

    private RateeMapping createRateeMapping21() {
        return new RateeMapping("ratee21", RateeType.TEAM_LEADER, "rater12", "rater2", "ratee22");
    }

    private RateeMapping createRateeMapping22() {
        return new RateeMapping("ratee22", RateeType.TEAM_LEADER, "rater12", "rater2", "ratee21");
    }

}
