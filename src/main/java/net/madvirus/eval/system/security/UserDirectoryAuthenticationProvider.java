package net.madvirus.eval.system.security;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.system.userdirectory.UserDirectory;
import net.madvirus.eval.system.userdirectory.UserDirectoryRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;
import java.util.List;

public class UserDirectoryAuthenticationProvider implements AuthenticationProvider {
    private UserDirectoryRepository userDirectoryRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        List<UserDirectory> userDirs = userDirectoryRepository.findAll();
        if (userDirs.isEmpty())
            throw new AuthenticationServiceException("No User Directory");

        UserModel authenticatedUser = null;
        AuthenticationServiceException thrownEx = null;
        for (UserDirectory userDir : userDirs) {
            try {
                authenticatedUser = userDir.authenticate(authentication.getName(), authentication.getCredentials().toString());
                if (authenticatedUser != null) {
                    thrownEx = null;
                    break;
                }
            } catch (Exception e) {
                thrownEx = new AuthenticationServiceException(
                        String.format("User Directory %s[%d] Service error: %s", userDir.getName(), userDir.getId(), e.getMessage()), thrownEx);;
            }
        }
        if (thrownEx != null) throw thrownEx;

        if (authenticatedUser == null) {
            throw new InsufficientAuthenticationException("Bad id or password");
        }
        return new UsernamePasswordAuthenticationToken(authenticatedUser, "", Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    public void setUserDirectoryRepository(UserDirectoryRepository userDirectoryRepository) {
        this.userDirectoryRepository = userDirectoryRepository;
    }

}
