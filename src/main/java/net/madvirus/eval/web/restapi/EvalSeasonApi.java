package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.CreateEvalSeasonCommand;
import net.madvirus.eval.api.evalseaon.OpenEvaluationCommand;
import net.madvirus.eval.command.evalseason.AleadyEvaluationOpenedException;
import net.madvirus.eval.web.dataloader.EvalSeasonData;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import net.madvirus.eval.web.dataloader.EvalSeasonSimpleData;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class EvalSeasonApi {

    private CommandGateway gateway;
    private EvalSeasonDataLoader evalSeasonDataLoader;

    @RequestMapping(value = "/api/evalseasons", method = RequestMethod.GET)
    public List<EvalSeasonSimpleData> getSeasons() {
        return evalSeasonDataLoader.loadAll();
    }

    @RequestMapping(value = "/api/evalseasons/{id}", method = RequestMethod.GET)
    public Object getSeason(@PathVariable("id") String id) {
        Optional<EvalSeasonData> data = loadEvalSeasonDto(id);
        return data.isPresent() ?
                data.get() : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Optional<EvalSeasonData> loadEvalSeasonDto(String id) {
        return evalSeasonDataLoader.load(id);
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
        } catch (AleadyEvaluationOpenedException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (AggregateNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Autowired
    public void setEvalSeasonDataLoader(EvalSeasonDataLoader evalSeasonDataLoader) {
        this.evalSeasonDataLoader = evalSeasonDataLoader;
    }

    @Autowired
    public void setGateway(CommandGateway gateway) {
        this.gateway = gateway;
    }


}
