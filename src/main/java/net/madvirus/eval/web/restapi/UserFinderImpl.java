package net.madvirus.eval.web.restapi;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFinderImpl implements UserFinder {
    private UserModelRepository userModelRepository;

    @Override
    public List<UserFindResult> findUsersByName(String[] names) {
        List<UserFindResult> results = new ArrayList<>();
        for (String name : names) {
            UserModel userModel = userModelRepository.findByName(name);
            if (userModel == null)
                results.add(UserFindResult.notFound(name));
            else
                results.add(UserFindResult.found(userModel.getName(), userModel.getId()));
        }
        return results;
    }

    @Autowired
    public void setUserModelRepository(UserModelRepository userModelRepository) {
        this.userModelRepository = userModelRepository;
    }
}
