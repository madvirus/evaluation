package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.evalseaon.DeleteMappingCommand;
import net.madvirus.eval.api.evalseaon.UpdateMappingCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/api/evalseasons/{evalSeasonId}/mappings")
public class EvalSeasonMappingApi {
    private CommandGateway gateway;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity updateMapping(@PathVariable("evalSeasonId") String evalSeasonId, @RequestBody UpdateMappingCommand command) {
        command.setEvalSeasonId(evalSeasonId);
        gateway.sendAndWait(command);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteMappings(@PathVariable("evalSeasonId") String evalSeasonId,
                                         @RequestParam("ids") String[] ids) {
        DeleteMappingCommand command = new DeleteMappingCommand(evalSeasonId, Arrays.asList(ids));
        gateway.sendAndWait(command);
        return new ResponseEntity(HttpStatus.OK);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    @Autowired
    public void setGateway(CommandGateway gateway) {
        this.gateway = gateway;
    }

}
