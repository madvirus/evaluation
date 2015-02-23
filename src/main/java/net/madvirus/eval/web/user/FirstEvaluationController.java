package net.madvirus.eval.web.user;

import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.command.personaleval.first.UpdateFirstPerformanceEvalCommand;
import net.madvirus.eval.domain.personaleval.AllCompeEvals;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;
import net.madvirus.eval.domain.personaleval.ItemEval;
import net.madvirus.eval.domain.personaleval.PerformanceItemAndAllEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.dataloader.CompetencyEvalSetUtil;
import net.madvirus.eval.web.dataloader.FirstTotalEvalData;
import net.madvirus.eval.web.dataloader.PersonalEvalData;
import net.madvirus.eval.web.dataloader.PersonalEvalDataLoader;
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
public class FirstEvaluationController {
    private PersonalEvalDataLoader personalEvalDataLoader;
    private EvalSeasonMappingModelRepository evalSeasonMappingModelRepository;

    @Autowired
    public void setPersonalEvalDataLoader(PersonalEvalDataLoader personalEvalDataLoader) {
        this.personalEvalDataLoader = personalEvalDataLoader;
    }

    @Autowired
    public void setEvalSeasonMappingModelRepository(EvalSeasonMappingModelRepository evalSeasonMappingModelRepository) {
        this.evalSeasonMappingModelRepository = evalSeasonMappingModelRepository;
    }

    @RequestMapping(value = "/main/evalseasons/{evalSeasonId}/firsteval/{rateeId}/performance")
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
                    ItemEval itemEval = ie.getFirstEval();
                    return itemEval != null ? itemEval : ItemEval.empty();
                })
                .collect(Collectors.toList());
        ItemEval totalEval = personalEvalData.getFirstPerfTotalEval();
        model.addAttribute("evalData",
                new UpdateFirstPerformanceEvalCommand(null, null, null,
                        firstItemEvals,
                        totalEval != null ? totalEval : ItemEval.empty()));
        return "main/personaleval/firstPerformanceEval";
    }

    @RequestMapping(value = "/main/evalseasons/{evalSeasonId}/firsteval/{rateeId}/competency")
    public String firstCompeEvalForm(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @AuthenticationPrincipal UserModel userModel,
            Model model) throws IOException {
        PersonalEvalData personalEvalData = checkAndSetModelOrThrowEx(evalSeasonId, rateeId, userModel, model);
        AllCompeEvals allCompeEvals = personalEvalData.getAllCompeEvals();
        CompetencyEvalSet firstEvalSet = allCompeEvals.getFirstEvalSet();
        if (firstEvalSet == null) {
            firstEvalSet = CompetencyEvalSetUtil.createEmptyEvalSet(personalEvalData.getRateeType());
        }
        RateeMappingModel rateeMappingModel = evalSeasonMappingModelRepository.findById(evalSeasonId).get().getRateeMappingOf(rateeId).get();
        List<UserModel> colleagueRaters = rateeMappingModel.getColleagueRaters();
        boolean allColleagueEvalDone = colleagueRaters.stream().allMatch(collUser -> personalEvalData.isColleagueCompeEvalDone(collUser.getId()));

        model.addAttribute("firstEvalSet", firstEvalSet);
        model.addAttribute("allColleagueEvalDone", allColleagueEvalDone);
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

    @RequestMapping(value = "/main/evalseasons/{evalSeasonId}/firsttotaleval")
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
