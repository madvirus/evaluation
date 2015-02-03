package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.command.evalseason.EvalSeason;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import scala.Option;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;

public class EvalSeasonDataLoaderImpl implements EvalSeasonDataLoader {
    private Repository<EvalSeason> evalSeasonRepository;
    private EvalSeasonMappingModelRepository evalSeasonMappingModelRepository;

    public EvalSeasonDataLoaderImpl(Repository<EvalSeason> evalSeasonRepository,
                                    EvalSeasonMappingModelRepository evalSeasonMappingModelRepository) {
        this.evalSeasonRepository = evalSeasonRepository;
        this.evalSeasonMappingModelRepository = evalSeasonMappingModelRepository;
    }

    @Override
    public Optional<EvalSeasonData> load(String id) {
        return runInUOW(() -> {
            try {
                EvalSeason evalSeason = evalSeasonRepository.load(id);
                Option<EvalSeasonMappingModel> model = evalSeasonMappingModelRepository.findById(id);
                return Optional.of(new EvalSeasonData(evalSeason, model.get()));
            } catch (AggregateNotFoundException ex) {
                return Optional.empty();
            }
        });
    }

    @Override
    public List<EvalSeasonSimpleData> loadAll() {
        return runInUOW(() -> {
            List<EvalSeasonMappingModel> all = evalSeasonMappingModelRepository.findAll();
            List<EvalSeasonSimpleData> dataList = all.stream()
                    .map(x -> evalSeasonRepository.load(x.getId()))
                    .map(ev -> new EvalSeasonSimpleData(ev))
                    .collect(Collectors.toList());
            return dataList;
        });
    }
}
