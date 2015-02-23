package net.madvirus.eval.web.restapi;

import net.madvirus.eval.command.personaleval.colleague.UpdateColleagueCompetencyEvalCommand;
import net.madvirus.eval.query.user.UserModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonalEvalColleagueApi {

    private CommandGateway gateway;

    @RequestMapping(value = "/api/evalseasons/{evalSeasonId}/ratees/{rateeId}/colleagueevals/currentuser/compeEvalSet", method = RequestMethod.POST)
    public ResponseEntity postColleagueCompeEval(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @AuthenticationPrincipal UserModel userModel,
            @RequestBody UpdateColleagueCompetencyEvalCommand command) {
        command.setEvalSeasonId(evalSeasonId);
        command.setRateeId(rateeId);
        command.setRaterId(userModel.getId());
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setGateway(CommandGateway gateway) {
        this.gateway = gateway;
    }

}
