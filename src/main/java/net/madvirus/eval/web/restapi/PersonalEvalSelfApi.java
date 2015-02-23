package net.madvirus.eval.web.restapi;

import net.madvirus.eval.command.personaleval.self.UpdateSelfCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.self.UpdateSelfPerformanceEvalCommand;
import net.madvirus.eval.query.user.UserModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonalEvalSelfApi {

    private CommandGateway gateway;

    @RequestMapping(value = "/api/evalseasons/{evalSeasonId}/currentuser/personaleval/selfPerfEval", method = RequestMethod.POST)
    public ResponseEntity updateSelfPerfEval(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @RequestBody UpdateSelfPerformanceEvalCommand command,
            @AuthenticationPrincipal UserModel userModel) {
        command.setUserId(userModel.getId());
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/api/evalseasons/{evalSeasonId}/currentuser/personaleval/selfCompeEval", method = RequestMethod.POST)
    public ResponseEntity updateSelfCompeEval(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @RequestBody UpdateSelfCompetencyEvalCommand command,
            @AuthenticationPrincipal UserModel userModel) {
        command.setRateeId(userModel.getId());
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setGateway(CommandGateway gateway) {
        this.gateway = gateway;
    }
}
