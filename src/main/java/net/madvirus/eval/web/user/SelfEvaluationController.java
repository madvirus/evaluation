package net.madvirus.eval.web.user;

import net.madvirus.eval.command.personaleval.PersonalEval;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.dataloader.EvalSeasonData;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class SelfEvaluationController {
    private EvalSeasonDataLoader evalSeasonDataLoader;

    @Autowired
    public void setEvalSeasonDataLoader(EvalSeasonDataLoader evalSeasonDataLoader) {
        this.evalSeasonDataLoader = evalSeasonDataLoader;
    }

    @RequestMapping("/main/evalseasons/{evalSeasonId}/selfeval/performance")
    public String performanceEvalForm(@PathVariable("evalSeasonId") String evalSeasonId,
                                      @AuthenticationPrincipal UserModel userModel,
                                      Model model,
                                      HttpServletResponse response) throws IOException {
        Optional<EvalSeasonData> dataOpt = evalSeasonDataLoader.load(evalSeasonId);
        if (!dataOpt.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        if (!dataOpt.get().getMappingModel().containsRatee(userModel.getId())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        model.addAttribute("evalSeasonId", evalSeasonId);
        model.addAttribute("personalEvalId", PersonalEval.createId(evalSeasonId, userModel.getId()));
        return "main/personaleval/selfPerformanceEval";
    }
}
