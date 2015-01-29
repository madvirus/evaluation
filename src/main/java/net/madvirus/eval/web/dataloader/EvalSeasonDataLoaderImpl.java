package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.command.evalseason.EvalSeason;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;
import scala.Option;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EvalSeasonDataLoaderImpl implements EvalSeasonDataLoader {
    private Repository<EvalSeason> evalSeasonRepository;
    private EvalSeasonMappingModelRepository evalSeasonMappingModelRepository;

    public EvalSeasonDataLoaderImpl(Repository<EvalSeason> evalSeasonRepository,
                                    EvalSeasonMappingModelRepository evalSeasonMappingModelRepository) {
        this.evalSeasonRepository = evalSeasonRepository;
        this.evalSeasonMappingModelRepository = evalSeasonMappingModelRepository;
    }

    @Override
    public Optional<EvalSeasonData> load(String id1) {
        UnitOfWork uow = DefaultUnitOfWork.startAndGet();
        try {
            EvalSeason evalSeason = evalSeasonRepository.load(id1);
            Option<EvalSeasonMappingModel> model = evalSeasonMappingModelRepository.findById(id1);
            uow.commit();
            return Optional.of(new EvalSeasonData(evalSeason, model.get()));
        } catch (AggregateNotFoundException ex) {
            uow.rollback(ex);
            return Optional.empty();
        } catch (Exception ex) {
            uow.rollback(ex);
            throw ex;
        }
    }

    @Override
    public List<EvalSeasonSimpleData> loadAll() {
        UnitOfWork uow = DefaultUnitOfWork.startAndGet();
        try {
            List<EvalSeasonMappingModel> all = evalSeasonMappingModelRepository.findAll();
            List<EvalSeasonSimpleData> dataList = all.stream()
                    .map(x -> evalSeasonRepository.load(x.getId()))
                    .map(ev -> new EvalSeasonSimpleData(ev))
                    .collect(Collectors.toList());
            uow.commit();
            return dataList;
        } catch (AggregateNotFoundException ex) {
            uow.rollback(ex);
            throw ex;
        } catch (Exception ex) {
            uow.rollback(ex);
            throw ex;
        }
    }
}
