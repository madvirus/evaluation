package net.madvirus.eval.system.userdirectory;

import org.springframework.data.repository.Repository;

public interface UserDirectoryRepository
        extends Repository<UserDirectory, Long>, CustomUserDirectoryRepository {
}
