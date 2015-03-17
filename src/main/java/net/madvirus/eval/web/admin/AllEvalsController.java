package net.madvirus.eval.web.admin;

import net.madvirus.eval.web.dataloader.EvalSeasonData;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import net.madvirus.eval.web.dataloader.PersonalEvalData;
import net.madvirus.eval.web.dataloader.PersonalEvalDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class AllEvalsController {

    private EvalSeasonDataLoader evalSeasonDataLoader;
    private PersonalEvalDataLoader personalEvalDataLoader;

    @RequestMapping("/admin/evalseasons/{seasonId}/evals")
    public String allPersonalEvals(@PathVariable("seasonId") String evalSeasonId, Model model) throws IOException {
        EvalSeasonData evalSeason = evalSeasonDataLoader.load(evalSeasonId);
        model.addAttribute("evalSeason", evalSeason);
        List<PersonalEvalData> allEvals = personalEvalDataLoader.getAllPersonalEvalData(evalSeasonId);
        model.addAttribute("allEvals", allEvals);
        return "admin/evalseasonAllEvals";
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
