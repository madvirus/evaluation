package net.madvirus.eval.system.userdirectory;

import net.madvirus.eval.query.user.UserModel;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AD")
public class ActiveDirectoryUserDirectory extends UserDirectory {

    @Override
    public UserModel authenticate(String userId, String userPassword) {
        boolean authenticated = authenticateUsingAD(userId, userPassword);
        if (!authenticated) return null;

        UserModel userModel = userModelRepository.findOne(userId);
        if (userModel == null) return null;
        return userModel.equalDirectoryId(getId()) ? userModel : null;
    }

    private boolean authenticateUsingAD(String userId, String userPassword) {
        String host = config.get("host");
        String port = config.get("port");
        String user = config.get("user");
        String password = config.get("password");
        String baseDN = config.get("baseDN");
        String userNameAttr = config.get("userNameAttr");

        LdapContextSource ctxSrc = new LdapContextSource();
        ctxSrc.setUrl("ldap://" + host + ":" + port);
        ctxSrc.setBase(baseDN);
        ctxSrc.setUserDn(user);
        ctxSrc.setPassword(password);
        try {
            ctxSrc.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LdapTemplate ldapTemplate = new LdapTemplate(ctxSrc);
        ldapTemplate.setIgnorePartialResultException(true);

        Filter filter = new EqualsFilter(userNameAttr, userId);
        return ldapTemplate.authenticate(DistinguishedName.EMPTY_PATH, filter.toString(), new String(userPassword));
    }
}
