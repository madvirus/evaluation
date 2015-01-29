package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.testhelper.ESIntTestSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ESIntTestSetup
public class EvalSeasonDataLoaderIntTest {
    @Autowired
    private EvalSeasonDataLoader loader;

    @Test
    public void shouldLoad_exstingEvalSeason() throws Exception {
        Optional<EvalSeasonData> load = loader.load("EVAL-001");
        assertThat(load.isPresent(), equalTo(true));
    }

    @Test
    public void shouldGetEmpty_nonExstingEvalSeason() throws Exception {
        Optional<EvalSeasonData> load = loader.load("EVAL-002");
        assertThat(load.isPresent(), equalTo(false));
    }

    @Test
    public void loadAll_should_return_all() throws Exception {
        List<EvalSeasonSimpleData> allData = loader.loadAll();
        assertThat(allData.size(), greaterThan(0));
    }
}
