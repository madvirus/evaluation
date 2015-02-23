package net.madvirus.eval.web.user;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.dataloader.CompeEvalData;
import net.madvirus.eval.web.dataloader.PersonalEvalDataLoader;
import net.madvirus.eval.web.dataloader.SelfPerfEvalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class SelfEvaluationController {
    private PersonalEvalDataLoader personalEvalDataLoader;

    @Autowired
    public void setPersonalEvalDataLoader(PersonalEvalDataLoader personalEvalDataLoader) {
        this.personalEvalDataLoader = personalEvalDataLoader;
    }

    @RequestMapping("/main/evalseasons/{evalSeasonId}/selfeval/performance")
    public String performanceEvalForm(@PathVariable("evalSeasonId") String evalSeasonId,
                                      @AuthenticationPrincipal UserModel userModel,
                                      Model model) throws IOException {
        SelfPerfEvalData selfPerfEvalData = personalEvalDataLoader.getSelfPerfEvalDataForSelfEvalForm(evalSeasonId, userModel.getId());
        model.addAttribute("selfPerfEvalData", selfPerfEvalData);
        return "main/personaleval/selfPerformanceEval";
    }

    @RequestMapping("/main/evalseasons/{evalSeasonId}/selfeval/competency")
    public String competencyEvalForm(@PathVariable("evalSeasonId") String evalSeasonId,
                                      @AuthenticationPrincipal UserModel userModel,
                                      Model model) throws IOException {
        CompeEvalData compeEvalData = personalEvalDataLoader.getSelfCompeEvalDataForSelfEvalForm(evalSeasonId, userModel.getId());
        model.addAttribute("compeEvalData", compeEvalData);
        return "main/personaleval/selfCompetencyEval";
    }

}
