package net.madvirus.eval.system.security;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.system.userdirectory.UserDirectory;
import net.madvirus.eval.system.userdirectory.UserDirectoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class UserDirectoryAuthenticationProviderTest {

    private UserDirectoryAuthenticationProvider authProvider;
    UserDirectoryRepository mockUserDirRepo = mock(UserDirectoryRepository.class);

    private UserDirectory userDir1;
    private UserDirectory userDir2;
    private List<UserDirectory> userDirs;

    @Before
    public void setUp() throws Exception {
        authProvider = new UserDirectoryAuthenticationProvider();
        authProvider.setUserDirectoryRepository(mockUserDirRepo);

        userDir1 = mock(UserDirectory.class);
        when(userDir1.getId()).thenReturn(1L);
        when(userDir1.getName()).thenReturn("UD1");
        userDir2 = mock(UserDirectory.class);
        when(userDir2.getId()).thenReturn(2L);
        when(userDir2.getName()).thenReturn("UD2");
        userDirs = Arrays.asList(userDir1, userDir2);
    }

    @Test
    public void givenNoUserDirectories_thenThrowAuthenticationServiceException() throws Exception {
        try {
            authProvider.authenticate(authToken());
            fail();
        } catch (AuthenticationServiceException e) {
        }
    }

    private UsernamePasswordAuthenticationToken authToken() {
        return new UsernamePasswordAuthenticationToken("id", "password");
    }

    @Test
    public void givenUserDirectoriesFound_thenUseAuthenticateOfAllUserDirs() throws Exception {
        when(mockUserDirRepo.findAll()).thenReturn(userDirs);
        try {
            authProvider.authenticate(authToken());
        } catch (AuthenticationException e) {
        }
        verify(userDir1).authenticate("id", "password");
        verify(userDir2).authenticate("id", "password");
    }

    @Test
    public void givenAllUserDirsCantAuth_thenThrowInsufficientAuthenticationException() throws Exception {
        when(mockUserDirRepo.findAll()).thenReturn(userDirs);
        when(userDir1.authenticate("id", "password")).thenReturn(null);
        when(userDir2.authenticate("id", "password")).thenReturn(null);
        try {
            authProvider.authenticate(authToken());
            fail();
        } catch (InsufficientAuthenticationException e) {
        }
    }

    @Test
    public void givenOneUserDirAuth_thenReturnAuthentication() throws Exception {
        when(mockUserDirRepo.findAll()).thenReturn(userDirs);
        when(userDir1.authenticate("id", "password")).thenReturn(null);
        when(userDir2.authenticate("id", "password")).thenReturn(new UserModel("id", "name", "", 1L));

        Authentication authenticate = authProvider.authenticate(authToken());

        assertThat(authenticate.isAuthenticated(), equalTo(true));
        assertThat(authenticate.getPrincipal(), instanceOf(UserModel.class));
    }

    @Test
    public void givenSomeUserDirThrowException_thenThrowAuthenticationServiceException() throws Exception {
        when(mockUserDirRepo.findAll()).thenReturn(userDirs);
        when(userDir1.authenticate("id", "password")).thenThrow(new RuntimeException("force"));

        try {
            authProvider.authenticate(authToken());
            fail();
        } catch (AuthenticationServiceException e) {
        }
    }

    @Test
    public void given_SomeUserDirThrowException_ButOtherUserDirSucceed_thenReturnAuth() throws Exception {
        when(mockUserDirRepo.findAll()).thenReturn(userDirs);
        when(userDir1.authenticate("id", "password")).thenThrow(new RuntimeException("force"));
        when(userDir2.authenticate("id", "password")).thenReturn(new UserModel("id", "name", "", 2L));

        Authentication authenticate = authProvider.authenticate(authToken());

        assertThat(authenticate.isAuthenticated(), equalTo(true));
    }
}
