package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.CreateEvalSeasonCommand;
import net.madvirus.eval.query.evalseason.EvalSeasonModel;
import net.madvirus.eval.query.evalseason.EvalSeasonModelRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/api/evalseasons", method = RequestMethod.POST)
    public ResponseEntity postNewSeasons(@RequestBody CreateEvalSeasonCommand command, HttpServletResponse response) {
        try {
            gateway.sendAndWait(command);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (DuplicateIdException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.CONFLICT);
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


}
