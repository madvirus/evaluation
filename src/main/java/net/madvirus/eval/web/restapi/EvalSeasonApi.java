package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.CreateEvalSeasonCommand;
import net.madvirus.eval.api.evalseaon.OpenEvaluationCommand;
import net.madvirus.eval.command.evalseason.AleadyEvaluationOpenedException;
import net.madvirus.eval.query.evalseason.EvalSeasonModel;
import net.madvirus.eval.query.evalseason.EvalSeasonModelRepository;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scala.Option;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class EvalSeasonApi {

    private EvalSeasonModelRepository evalSeasonModelRepository;
    private CommandGateway gateway;

    @RequestMapping(value = "/api/evalseasons", method = RequestMethod.GET)
    public List<EvalSeasonModel> getSeasons() {
        return evalSeasonModelRepository.findAll();
    }

    @RequestMapping(value = "/api/evalseasons/{id}", method = RequestMethod.GET)
    public Object getSeason(@PathVariable("id") String id) {
        Option<EvalSeasonModel> model = evalSeasonModelRepository.findById(id);
        return model.isEmpty() ?
                new ResponseEntity(HttpStatus.NOT_FOUND) :
                toEvalSeasonModelDto(model.get());
    }

    private EvalSeasonModelDto toEvalSeasonModelDto(EvalSeasonModel evalSeasonModel) {
        return new EvalSeasonModelDto(evalSeasonModel);
    }

    @RequestMapping(value = "/api/evalseasons", method = RequestMethod.POST)
    public ResponseEntity postNewSeasons(@RequestBody CreateEvalSeasonCommand command, HttpServletResponse response) {
        try {
            gateway.sendAndWait(command);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (DuplicateIdException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/api/evalseasons/{id}", method = RequestMethod.PUT, params = "action=open")
    public ResponseEntity putOpen(@PathVariable("id") String evalSeasonId) {
        try {
            OpenEvaluationCommand command = new OpenEvaluationCommand(evalSeasonId);
            gateway.sendAndWait(command);
            return new ResponseEntity(HttpStatus.OK);
        } catch(AleadyEvaluationOpenedException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch(AggregateNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Autowired
    public void setEvalSeasonModelRepository(EvalSeasonModelRepository evalSeasonModelRepository) {
        this.evalSeasonModelRepository = evalSeasonModelRepository;
    }

    @Autowired
    public void setGateway(CommandGateway gateway) {
        this.gateway = gateway;
    }


    private class EvalSeasonModelDto {
        private EvalSeasonModel evalSeasonModel;

        public EvalSeasonModelDto(EvalSeasonModel evalSeasonModel) {
            this.evalSeasonModel = evalSeasonModel;
        }

        public String getId() {
            return evalSeasonModel.getId();
        }

        public String getName() {
            return evalSeasonModel.getName();
        }

        public boolean isOpened() {
            return evalSeasonModel.getOpened();
        }

        public List<RateeMappingModel> getMappings() {
            return evalSeasonModel.getRateeMappingModels();
        }
    }
}
