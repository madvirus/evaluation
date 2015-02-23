package net.madvirus.eval.web.restapi;

import net.madvirus.eval.command.personaleval.second.UpdateSecondCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.second.UpdateSecondPerformanceEvalCommand;
import net.madvirus.eval.command.personaleval.second.UpdateSecondTotalEvalCommand;
import net.madvirus.eval.query.user.UserModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonalEvalSecondApi {

    private CommandGateway gateway;

    @RequestMapping(
            value = "/api/evalseasons/{evalSeasonId}/ratees/{rateeId}/personaleval/secondPerfEval", method = RequestMethod.POST)
    public ResponseEntity updateFirstPerfEval(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @RequestBody UpdateSecondPerformanceEvalCommand command,
            @AuthenticationPrincipal UserModel userModel) {
        command.setEvalSeasonId(evalSeasonId);
        command.setRaterId(userModel.getId());
        command.setRateeId(rateeId);
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }


    @RequestMapping(
            value = "/api/evalseasons/{evalSeasonId}/ratees/{rateeId}/personaleval/secondCompeEval", method = RequestMethod.POST)
    public ResponseEntity updateFirstCompeEval(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @RequestBody UpdateSecondCompetencyEvalCommand command,
            @AuthenticationPrincipal UserModel userModel) {
        command.setEvalSeasonId(evalSeasonId);
        command.setRaterId(userModel.getId());
        command.setRateeId(rateeId);
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            value = "/api/evalseasons/{evalSeasonId}/secondrater/secondTotalEval",
            method = RequestMethod.POST)
    public ResponseEntity updateFirstTotal(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @AuthenticationPrincipal UserModel userModel,
            @RequestBody UpdateSecondTotalEvalCommand command) {
        command.setEvalSeasonId(evalSeasonId);
        command.setRaterId(userModel.getId());
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setGateway(CommandGateway gateway) {
        this.gateway = gateway;
    }
}
