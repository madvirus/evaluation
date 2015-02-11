package net.madvirus.eval.web.user;

import net.madvirus.eval.api.personaleval.AllCompeEvals;
import net.madvirus.eval.api.personaleval.CompetencyEvalSet;
import net.madvirus.eval.api.personaleval.PerformanceItemAndAllEval;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
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

@Controller
public class FirstEvaluationController {
    private PersonalEvalDataLoader personalEvalDataLoader;
    private EvalSeasonDataLoader evalSeasonDataLoader;

    @Autowired
    public void setPersonalEvalDataLoader(PersonalEvalDataLoader personalEvalDataLoader) {
        this.personalEvalDataLoader = personalEvalDataLoader;
    }

    @Autowired
    public void setEvalSeasonDataLoader(EvalSeasonDataLoader evalSeasonDataLoader) {
        this.evalSeasonDataLoader = evalSeasonDataLoader;
    }

    @RequestMapping(value = "/main/evalseasons/{evalSeasonId}/firsteval/{rateeId}/performance")
    public String firstPerfEvalForm(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @AuthenticationPrincipal UserModel userModel,
            HttpServletResponse response,
            Model model) throws IOException {
        checkAndSetModelOrThrowEx(evalSeasonId, rateeId, userModel, model);
        return "main/personaleval/firstPerformanceEval";
    }

    @RequestMapping(value = "/main/evalseasons/{evalSeasonId}/firsteval/{rateeId}/competency")
    public String firstCompeEvalForm(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @AuthenticationPrincipal UserModel userModel,
            HttpServletResponse response,
            Model model) throws IOException {
        PersonalEvalData personalEvalData = checkAndSetModelOrThrowEx(evalSeasonId, rateeId, userModel, model);
        AllCompeEvals allCompeEvals = personalEvalData.getAllCompeEvals();
        CompetencyEvalSet firstEvalSet = allCompeEvals.getFirstEvalSet();
        if (firstEvalSet == null) {
            firstEvalSet = CompetencyEvalSetUtil.createEmptyEvalSet(personalEvalData.getRateeType());
        }
        model.addAttribute("firstEvalSet", firstEvalSet);
        return "main/personaleval/firstCompetencyEval";
    }

    private PersonalEvalData checkAndSetModelOrThrowEx(String evalSeasonId, String rateeId, UserModel userModel, Model model) throws IOException {
        PersonalEvalData personalEval = personalEvalDataLoader.getPersonalEval(evalSeasonId, rateeId);
        if (!personalEval.checkFirstRater(userModel.getId())) {
            throw new YouAreNotFirstRaterException();
        }
        model.addAttribute("personalEval", personalEval);
        return personalEval;
    }

    @RequestMapping(value="/main/evalseasons/{evalSeasonId}/firsttotaleval")
    public String totalEvalForm(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @AuthenticationPrincipal UserModel userModel,
            Model model) {
        model.addAttribute("evalSeasonId", evalSeasonId);
        FirstTotalEvalData firstTotalEvalData = personalEvalDataLoader.getFirstTotalEvalData(evalSeasonId, userModel.getId());
        model.addAttribute("firstTotalEvalData", firstTotalEvalData);
        return "main/personaleval/firstTotalEval";
    }
}
