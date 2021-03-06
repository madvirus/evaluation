package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.evalseaon.AlreadyEvaluationOpenedException;
import net.madvirus.eval.api.evalseaon.ColleagueEvalAlreadyStartedException;
import net.madvirus.eval.command.evalseason.CreateEvalSeasonCommand;
import net.madvirus.eval.command.evalseason.OpenEvaluationCommand;
import net.madvirus.eval.command.evalseason.StartColleagueEvalCommand;
import net.madvirus.eval.command.evalseason.StartFirstEvalCommand;
import net.madvirus.eval.command.personaleval.ReturnFirstEvalDraftCommand;
import net.madvirus.eval.domain.evalseason.FirstEvalAlreadyStartedException;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import net.madvirus.eval.web.dataloader.EvalSeasonSimpleData;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        return evalSeasonDataLoader.load(id);
    }

    @RequestMapping(value = "/api/evalseasons", method = RequestMethod.POST)
    public ResponseEntity postNewSeasons(@RequestBody CreateEvalSeasonCommand command, HttpServletResponse response) {
        gateway.sendAndWait(command);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/evalseasons/{id}", method = RequestMethod.PUT, params = "action=open")
    public ResponseEntity putOpen(@PathVariable("id") String evalSeasonId) {
        try {
            OpenEvaluationCommand command = new OpenEvaluationCommand(evalSeasonId);
            gateway.sendAndWait(command);
            return ResponseEntity.ok().build();
        } catch (AlreadyEvaluationOpenedException ex) {
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = "/api/evalseasons/{id}", method = RequestMethod.PUT, params = "action=startColleagueEval")
    public ResponseEntity putStartColleagueEval(@PathVariable("id") String evalSeasonId) {
        try {
            StartColleagueEvalCommand command = new StartColleagueEvalCommand(evalSeasonId);
            gateway.sendAndWait(command);
            return ResponseEntity.ok().build();
        } catch (ColleagueEvalAlreadyStartedException ex) {
            return ResponseEntity.ok().build();
        }
    }


    @RequestMapping(value = "/api/evalseasons/{id}", method = RequestMethod.PUT, params = "action=startFirstEval")
    public ResponseEntity putStartFirstEval(@PathVariable("id") String evalSeasonId) {
        try {
            StartFirstEvalCommand command = new StartFirstEvalCommand(evalSeasonId);
            gateway.sendAndWait(command);
            return ResponseEntity.ok().build();
        } catch (FirstEvalAlreadyStartedException ex) {
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = "/api/evalseasons/{id}/evalStates")
    public ResponseEntity getEvalStates(@PathVariable("id") String evalSeasonId) {
        return ResponseEntity.ok(evalSeasonDataLoader.loadEvalStates(evalSeasonId));
    }

    @RequestMapping(value = "/api/evalseasons/{id}/evalStates/firstRaters/{firstRaterId}", method = RequestMethod.PUT, params = "action=returnDraft")
    public ResponseEntity returnFirstStateToDraft(@PathVariable("id") String evalSeasonId,
                                                  @PathVariable("firstRaterId") String firstRaterId) {
        ReturnFirstEvalDraftCommand command = new ReturnFirstEvalDraftCommand(evalSeasonId, firstRaterId);
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
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
