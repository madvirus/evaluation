package net.madvirus.eval.web.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserModelApi {
    private UserFinder userFinder;

    @RequestMapping(value = "/api/users", method = RequestMethod.GET, params = "op=findId")
    public List<UserFindResult> users(@RequestParam("name") String nameValue) {
        return userFinder.findUsersByName(nameValue.split(","));
    }

    @Autowired
    public void setUserFinder(UserFinder userFinder) {
        this.userFinder = userFinder;
    }

}
