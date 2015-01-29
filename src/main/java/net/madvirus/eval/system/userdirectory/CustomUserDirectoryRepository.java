package net.madvirus.eval.system.userdirectory;

import java.util.List;

public interface CustomUserDirectoryRepository {

    public UserDirectory findOne(Long id);

    public List<UserDirectory> findAll();
}
