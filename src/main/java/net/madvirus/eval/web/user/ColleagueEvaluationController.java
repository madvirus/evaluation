package net.madvirus.eval.web.user;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.dataloader.CompeEvalData;
import net.madvirus.eval.web.dataloader.PersonalEvalData;
import net.madvirus.eval.web.dataloader.PersonalEvalDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class ColleagueEvaluationController {
    private PersonalEvalDataLoader personalEvalDataLoader;

    @Autowired
    public ColleagueEvaluationController(PersonalEvalDataLoader personalEvalDataLoader) {
        this.personalEvalDataLoader = personalEvalDataLoader;
    }

    @RequestMapping("/main/evalseasons/{evalSeasonId}/colleval/ratees/{rateeId}")
    public String colleagueEvalForm(
            @PathVariable("evalSeasonId") String evalSeasonId,
            @PathVariable("rateeId") String rateeId,
            @AuthenticationPrincipal UserModel user,
            Model model) throws IOException {
        CompeEvalData compeEvalData = personalEvalDataLoader.getColleagueCompeEvalDataForEvalForm(evalSeasonId, rateeId, user.getId());
        model.addAttribute("compeEvalData", compeEvalData);

        PersonalEvalData personalEval = personalEvalDataLoader.getPersonalEval(evalSeasonId, rateeId);
        model.addAttribute("personalEval", personalEval);
        return "main/personaleval/colleagueCompetencyEval";
    }
}
