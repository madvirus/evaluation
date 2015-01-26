package net.madvirus.eval.web.restapi;

import java.util.List;

public interface UserFinder {
    List<UserFindResult> findUsersByName(String[] names);
}
