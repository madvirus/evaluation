package net.madvirus.eval.web.user;

import net.madvirus.eval.api.personaleval.second.YouAreNotSecondRaterException;
import net.madvirus.eval.command.personaleval.second.UpdateSecondPerformanceEvalCommand;
import net.madvirus.eval.domain.personaleval.AllCompeEvals;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;
import net.madvirus.eval.domain.personaleval.ItemEval;
import net.madvirus.eval.domain.personaleval.PerformanceItemAndAllEval;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.dataloader.CompetencyEvalSetUtil;
import net.madvirus.eval.web.dataloader.PersonalEvalData;
import net.madvirus.eval.web.dataloader.PersonalEvalDataLoader;
import net.madvirus.eval.web.dataloader.SecondTotalEvalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SecondEvaluationController {
    private PersonalEvalDataLoader personalEvalDataLoader;

    @Autowired
    public void setPersonalEvalDataLoader(PersonalEvalDataLoader personalEvalDataLoader) {
        this.personalEvalDataLoader = personalEvalDataLoader;
    }

    @RequestMapping(value = "/main/evalseasons/{evalSeasonId}/secondeval/{rateeId}/performance")
    public String firstPerfEvalForm(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @AuthenticationPrincipal UserModel userModel,
            Model model) throws IOException {
        PersonalEvalData personalEvalData = checkAndSetModelOrThrowEx(evalSeasonId, rateeId, userModel, model);
        List<PerformanceItemAndAllEval> perfItemAndEvals = personalEvalData.getPerfItemAndAllEvals();
        List<ItemEval> firstItemEvals = perfItemAndEvals
                .stream()
                .map(ie -> {
                    ItemEval itemEval = ie.getSecondEval();
                    return itemEval != null ? itemEval : ItemEval.empty();
                })
                .collect(Collectors.toList());
        ItemEval totalEval = personalEvalData.getSecondPerfTotalEval();
        model.addAttribute("evalData",
                new UpdateSecondPerformanceEvalCommand(null, null, null,
                        firstItemEvals,
                        totalEval != null ? totalEval : ItemEval.empty()));
        return "main/personaleval/secondPerformanceEval";
    }

    @RequestMapping(value = "/main/evalseasons/{evalSeasonId}/secondeval/{rateeId}/competency")
    public String firstCompeEvalForm(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @AuthenticationPrincipal UserModel userModel,
            Model model) throws IOException {
        PersonalEvalData personalEvalData = checkAndSetModelOrThrowEx(evalSeasonId, rateeId, userModel, model);
        AllCompeEvals allCompeEvals = personalEvalData.getAllCompeEvals();
        CompetencyEvalSet secondEvalSet = allCompeEvals.getSecondEvalSet();
        if (secondEvalSet == null) {
            secondEvalSet = CompetencyEvalSetUtil.createEmptyEvalSet(personalEvalData.getRateeType());
        }
        model.addAttribute("secondEvalSet", secondEvalSet);
        return "main/personaleval/secondCompetencyEval";
    }

    private PersonalEvalData checkAndSetModelOrThrowEx(String evalSeasonId, String rateeId, UserModel userModel, Model model) throws IOException {
        PersonalEvalData personalEval = personalEvalDataLoader.getPersonalEval(evalSeasonId, rateeId);
        if (!personalEval.checkSecondRater(userModel.getId())) {
            throw new YouAreNotSecondRaterException();
        }
        model.addAttribute("personalEval", personalEval);
        return personalEval;
    }

    @RequestMapping(value="/main/evalseasons/{evalSeasonId}/secondtotaleval")
    public String totalEvalForm(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @AuthenticationPrincipal UserModel userModel,
            Model model) {
        model.addAttribute("evalSeasonId", evalSeasonId);
        SecondTotalEvalData secondTotalEvalData = personalEvalDataLoader.getSecondTotalEvalData(evalSeasonId, userModel.getId());
        model.addAttribute("secondTotalEvalData", secondTotalEvalData);
        return "main/personaleval/secondTotalEval";
    }
}
