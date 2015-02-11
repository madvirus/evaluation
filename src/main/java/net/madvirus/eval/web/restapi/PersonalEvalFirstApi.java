package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.personaleval.first.*;
import net.madvirus.eval.query.user.UserModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonalEvalFirstApi {

    private CommandGateway gateway;

    @RequestMapping(
            value = "/api/evalseasons/{evalSeasonId}/ratees/{rateeId}/personaleval/firstPerfEval", method = RequestMethod.POST)
    public ResponseEntity updateFirstPerfEval(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @RequestBody UpdateFirstPerformanceEvalCommand command,
            @AuthenticationPrincipal UserModel userModel) {
        command.setEvalSeasonId(evalSeasonId);
        command.setFirstRaterId(userModel.getId());
        command.setRateeId(rateeId);
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            value = "/api/evalseasons/{evalSeasonId}/ratees/{rateeId}/personaleval/selfPerfEval",
            method = RequestMethod.POST, params = "op=reject")
    public ResponseEntity rejectSelfPerfEval(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @AuthenticationPrincipal UserModel userModel) {
        RejectSelfPerformanceEvalCommand command = new RejectSelfPerformanceEvalCommand(evalSeasonId, rateeId, userModel.getId());
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            value = "/api/evalseasons/{evalSeasonId}/ratees/{rateeId}/personaleval/firstCompeEval", method = RequestMethod.POST)
    public ResponseEntity updateFirstCompeEval(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @RequestBody UpdateFirstCompetencyEvalCommand command,
            @AuthenticationPrincipal UserModel userModel) {
        command.setEvalSeasonId(evalSeasonId);
        command.setFirstRaterId(userModel.getId());
        command.setRateeId(rateeId);
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            value = "/api/evalseasons/{evalSeasonId}/ratees/{rateeId}/personaleval/selfCompeEval",
            method = RequestMethod.POST, params = "op=reject")
    public ResponseEntity rejectSelfCompeEval(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @AuthenticationPrincipal UserModel userModel) {
        RejectSelfCompetencyEvalCommand command = new RejectSelfCompetencyEvalCommand(evalSeasonId, rateeId, userModel.getId());
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            value = "/api/evalseasons/{evalSeasonId}/firstrater/firstTotalEval",
            method = RequestMethod.POST)
    public ResponseEntity updateFirstTotal(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @AuthenticationPrincipal UserModel userModel,
            @RequestBody UpdateFirstTotalEvalCommand command) {
        command.setEvalSeasonId(evalSeasonId);
        command.setFirstRaterId(userModel.getId());
        gateway.sendAndWait(command);
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setGateway(CommandGateway gateway) {
        this.gateway = gateway;
    }
}
