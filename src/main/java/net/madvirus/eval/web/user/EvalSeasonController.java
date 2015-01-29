package net.madvirus.eval.web.user;

import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
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
public class EvalSeasonController {

    private EvalSeasonDataLoader evalSeasonDataLoader;

    @RequestMapping("/main/evalseasons/{seasonId}")
    public String main(@PathVariable("seasonId") String evalSeasonId, Model model,
                       @AuthenticationPrincipal UserModel currentUser,
                       HttpServletResponse response) throws IOException {
        Optional<EvalSeasonData> evalSeasonDataOptional = evalSeasonDataLoader.load(evalSeasonId);
        if (!evalSeasonDataOptional.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        EvalSeasonData evalSeasonData = evalSeasonDataOptional.get();

        model.addAttribute("evalSeason", evalSeasonData);

        EvalSeasonMappingModel evalSeasonMappingModel = evalSeasonData.getMappingModel();

        if (evalSeasonMappingModel.containsRatee(currentUser.getId())) {
            // 피평가자에 알맞은 모델을 추가
            // 본인 평가 상태 구함

        }
        if (evalSeasonMappingModel.containsFirstRater(currentUser.getId())) {
            // 1차 평가자에 알맞은 모델을 추가
            // 피평가자들의 평가 상태 목록 구함
        }
        if (evalSeasonMappingModel.containsFirstRater(currentUser.getId())) {
            // 2차 평가자에 알맞은 모델을 추가
            // 피평가자들의 평가 상태 목록 구함
        }
        if (evalSeasonMappingModel.containsColleaguRater(currentUser.getId())) {
            // 동료 평가자에 알맞은 모델을 추가
        }
        return "main/evalseason/evalseasonMain";
    }

    @Autowired
    public void setEvalSeasonDataLoader(EvalSeasonDataLoader evalSeasonDataLoader) {
        this.evalSeasonDataLoader = evalSeasonDataLoader;
    }

}
