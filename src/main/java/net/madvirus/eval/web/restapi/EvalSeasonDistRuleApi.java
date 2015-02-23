package net.madvirus.eval.web.restapi;

import net.madvirus.eval.command.evalseason.UpdateDistributionRuleCommand;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EvalSeasonDistRuleApi {

    private CommandGateway gateway;
    private EvalSeasonDataLoader evalSeasonDataLoader;

    @RequestMapping(value = "/api/evalseasons/{id}/distrule", method = RequestMethod.GET)
    public ResponseEntity getSeason(@PathVariable("id") String evalSeasonId) {
        return ResponseEntity.ok().body(evalSeasonDataLoader.getDistributionRule(evalSeasonId));
    }

    @RequestMapping(value = "/api/evalseasons/{id}/distrule", method = RequestMethod.POST)
    public ResponseEntity updateDistRule(@PathVariable("id") String evalSeasonId,
                                         @RequestBody UpdateDistributionRuleCommand command) {
        command.setEvalSeasonId(evalSeasonId);
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
