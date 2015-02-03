package net.madvirus.eval.testhelper;

import net.madvirus.eval.query.evalseason.EvanSeasonMappingModelInitializer;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRunReplayTest extends AbstractIntTest {
    @Autowired
    private EvanSeasonMappingModelInitializer initializer;

    @Before
    public void replayEvalSeasonEvent() throws Exception {
        initializer.replay();
    }
}
