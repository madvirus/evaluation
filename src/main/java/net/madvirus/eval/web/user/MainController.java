package net.madvirus.eval.web.user;

import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import net.madvirus.eval.web.dataloader.EvalSeasonSimpleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private EvalSeasonDataLoader evalSeasonDataLoader;

    @RequestMapping("/main")
    public String main(Model model) {
        List<EvalSeasonSimpleData> datas = evalSeasonDataLoader.loadAll();
        List<EvalSeasonSimpleData> openedEvalSeasons = datas.stream().filter(data -> data.isOpened()).collect(Collectors.toList());
        model.addAttribute("evalSeasons", openedEvalSeasons);
        return "main/main";
    }

    @Autowired
    public void setEvalSeasonDataLoader(EvalSeasonDataLoader evalSeasonDataLoader) {
        this.evalSeasonDataLoader = evalSeasonDataLoader;
    }
}
