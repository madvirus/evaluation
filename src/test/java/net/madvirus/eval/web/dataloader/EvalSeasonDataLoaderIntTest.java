package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.testhelper.AbstractRunReplayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class EvalSeasonDataLoaderIntTest extends AbstractRunReplayTest {
    @Autowired
    private EvalSeasonDataLoader loader;

    @Test
    public void givenEvalSeason_then_Load_Should_Return_Data() throws Exception {
        EvalSeasonData load = loader.load("EVAL-001");
        assertThat(load, notNullValue());
    }

    @Test(expected = EvalSeasonNotFoundException.class)
    public void givenNoEvalSeason_then_Load_Should_Return_Empty() throws Exception {
        loader.load("EVAL-003");
    }

    @Test
    public void loadAll_should_return_all() throws Exception {
        List<EvalSeasonSimpleData> allData = loader.loadAll();
        assertThat(allData.size(), greaterThan(0));
    }
}
