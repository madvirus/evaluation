package net.madvirus.eval.web.restapi;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.UpdateSelfCompetencyEvalCommand;
import net.madvirus.eval.api.personaleval.UpdateSelfPerformanceEvalCommand;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.dataloader.PersonalEvalDataLoader;
import net.madvirus.eval.web.dataloader.SelfCompeEvalData;
import net.madvirus.eval.web.dataloader.SelfPerfEvalData;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PersonalEvalSelfApi {

    private PersonalEvalDataLoader personalEvalDataLoader;
    private CommandGateway gateway;

    @RequestMapping(value="/api/personalevals/{personalEvalId}/selfPerfEval", method = RequestMethod.GET)
    public ResponseEntity getSelfPerfEval(@PathVariable("personalEvalId") String personalEvalId) {
        Optional<SelfPerfEvalData> dataOpt = personalEvalDataLoader.getSelfPerfEval(personalEvalId);
        if (!dataOpt.isPresent())
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        // TODO: 권한 검사, 본인 또는 HR 관리자만 조회 가능
        return new ResponseEntity(dataOpt.get(), HttpStatus.OK);
    }

    @RequestMapping(value="/api/personalevals/{personalEvalId}/selfPerfEval", method = RequestMethod.POST)
    public ResponseEntity updateSelfPerfEval(@PathVariable("personalEvalId") String personalEvalId,
                                             @RequestBody UpdateSelfPerformanceEvalCommand command,
                                             @AuthenticationPrincipal UserModel userModel) {
        // TODO: 권한 검사, 본인만 본인 성과 평가 업데이트 가능
        command.setUserId(userModel.getId());
        try {
            gateway.sendAndWait(command);
            return ResponseEntity.ok().build();
        } catch (RateeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (EvalSeasonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value="/api/personalevals/{personalEvalId}/selfCompeEval", method = RequestMethod.GET)
    public ResponseEntity getSelfCompeEval(@PathVariable("personalEvalId") String personalEvalId) {
        Optional<SelfCompeEvalData> dataOpt = personalEvalDataLoader.getSelfCompeEval(personalEvalId);
        if (!dataOpt.isPresent())
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        // TODO: 권한 검사, 본인 또는 HR 관리자만 조회 가능
        return new ResponseEntity(dataOpt.get(), HttpStatus.OK);
    }

    @RequestMapping(value="/api/personalevals/{personalEvalId}/selfCompeEval", method = RequestMethod.POST)
    public ResponseEntity updateSelfCompeEval(@PathVariable("personalEvalId") String personalEvalId,
                                             @RequestBody UpdateSelfCompetencyEvalCommand command,
                                             @AuthenticationPrincipal UserModel userModel) {
        // TODO: 권한 검사, 본인만 본인 성과 평가 업데이트 가능
        command.setUserId(userModel.getId());
        try {
            gateway.sendAndWait(command);
            return ResponseEntity.ok().build();
        } catch (RateeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (EvalSeasonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Autowired
    public void setPersonalEvalDataLoader(PersonalEvalDataLoader personalEvalDataLoader) {
        this.personalEvalDataLoader = personalEvalDataLoader;
    }

    @Autowired
    public void setGateway(CommandGateway gateway) {
        this.gateway = gateway;
    }
}
