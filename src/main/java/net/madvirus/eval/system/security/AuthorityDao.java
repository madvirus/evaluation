package net.madvirus.eval.system.security;

import java.util.List;

public interface AuthorityDao {
    List<Authority> selectByUserId(String id);
}
