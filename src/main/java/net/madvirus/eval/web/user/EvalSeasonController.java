package net.madvirus.eval.web.user;

import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.dataloader.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class EvalSeasonController {

    private EvalSeasonDataLoader evalSeasonDataLoader;
    private PersonalEvalDataLoader personalEvalDataLoader;

    @RequestMapping("/main/evalseasons/{seasonId}")
    public String main(@PathVariable("seasonId") String evalSeasonId, Model model,
                       @AuthenticationPrincipal UserModel currentUser,
                       HttpServletResponse response) throws IOException {
        EvalSeasonData evalSeasonData = evalSeasonDataLoader.load(evalSeasonId);
        model.addAttribute("evalSeason", evalSeasonData);

        EvalSeasonMappingModel evalSeasonMappingModel = evalSeasonData.getMappingModel();

        EvalSeasonUserRole evalUserRole = new EvalSeasonUserRole();

        if (evalSeasonMappingModel.containsRatee(currentUser.getId())) {
            evalUserRole.addRateeRole();
            populateUserPersonalEvalState(evalSeasonData.getId(), currentUser, model);
        }
        if (evalSeasonMappingModel.containsFirstRater(currentUser.getId())) {
            evalUserRole.addFirstRaterRole();
            populateFirstRatersRateePersonalEvalState(evalSeasonId, evalSeasonMappingModel.getRateesOfFirstRater(currentUser.getId()), model);
        }
        if (evalSeasonMappingModel.containsSecondRater(currentUser.getId())) {
            // 2차 평가자에 알맞은 모델을 추가
            // 피평가자들의 평가 상태 목록 구함
            evalUserRole.addSecondRaterRole();
        }

        if (evalSeasonData.isColleagueEvalutionStarted() && evalSeasonMappingModel.containsColleaguRater(currentUser.getId())) {
            evalUserRole.addColleagueRaterRole();
            populateColleagueEvalState(
                    evalSeasonData.getId(), currentUser,
                    evalSeasonMappingModel.getRateesOfColleague(currentUser.getId()),
                    model);

        }
        model.addAttribute("evalUserRole", evalUserRole);
        return "main/evalseason/evalseasonMain";
    }

    private void populateUserPersonalEvalState(String evalSeasonId, UserModel currentUser, Model model) {
        PersonalEvalState personalEval = personalEvalDataLoader.getPersonalEvalStateOf(evalSeasonId, currentUser.getId());
        model.addAttribute("myPersonalEval", personalEval);
    }

    private void populateColleagueEvalState(String evalSeasonDataId, UserModel currentUser, Set<UserModel> rateesOfColleague, Model model) {
        List<ColleagueEvalState> states = personalEvalDataLoader.getColleagueEvalStates(
                evalSeasonDataId,
                currentUser.getId(),
                rateesOfColleague.stream().map(x -> x.getId()).collect(Collectors.toSet()));
        model.addAttribute("colleagueEvalStates", states);

    }

    private void populateFirstRatersRateePersonalEvalState(String evalSeasonId, Set<UserModel> rateesOfFirstRater, Model model) {
        List<UserEvalState> userEvalStates = rateesOfFirstRater.stream()
                .map(userModel -> {
                    PersonalEvalState evalState = personalEvalDataLoader.getPersonalEvalStateOf(evalSeasonId, userModel.getId());
                    return new UserEvalState(userModel, evalState);
                })
                .collect(Collectors.toList());
        model.addAttribute("firstRateeEvalStates", userEvalStates);
    }

    @Autowired
    public void setEvalSeasonDataLoader(EvalSeasonDataLoader evalSeasonDataLoader) {
        this.evalSeasonDataLoader = evalSeasonDataLoader;
    }

    @Autowired
    public void setPersonalEvalDataLoader(PersonalEvalDataLoader personalEvalDataLoader) {
        this.personalEvalDataLoader = personalEvalDataLoader;
    }

}
