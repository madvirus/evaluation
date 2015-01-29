package net.madvirus.eval.system.userdirectory;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import net.madvirus.eval.testhelper.ESIntTestSetup;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@ESIntTestSetup
@RunWith(SpringJUnit4ClassRunner.class)
public class ActiveDirectoryUserDirectoryIntTest {
    @Autowired
    private UserDirectoryRepository userDirectoryRepository;

    @Test
    @Ignore
    public void authenticate() throws Exception {
        UserDirectory adUserDir = userDirectoryRepository.findOne(2L);
        UserModel userModel = adUserDir.authenticate("someId", "somePass");
        assertThat(userModel, notNullValue());

        assertThat(adUserDir.authenticate("someId", "someBadPass"), nullValue());
    }

}
