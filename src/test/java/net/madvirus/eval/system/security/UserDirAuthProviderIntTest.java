package net.madvirus.eval.system.security;

import net.madvirus.eval.testhelper.AbstractIntTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserDirAuthProviderIntTest extends AbstractIntTest {
    @Autowired
    private UserDirectoryAuthenticationProvider provider;

    @Test
    public void authenticate() throws Exception {
        provider.authenticate(new UsernamePasswordAuthenticationToken("ratee11", "1234"));

    }
}
