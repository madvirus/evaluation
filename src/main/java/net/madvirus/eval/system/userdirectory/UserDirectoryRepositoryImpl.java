package net.madvirus.eval.system.userdirectory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserDirectoryRepositoryImpl implements CustomUserDirectoryRepository, ApplicationContextAware {
    @PersistenceContext
    private EntityManager em;
    private ApplicationContext applicationContext;

    @Override
    public UserDirectory findOne(Long id) {
        UserDirectory userDirectory = em.find(UserDirectory.class, id);
        if (userDirectory == null) {
            return null;
        }
        autowire(userDirectory);
        return userDirectory;
    }

    private void autowire(UserDirectory userDirectory) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(userDirectory);
    }

    @Override
    public List<UserDirectory> findAll() {
        TypedQuery<UserDirectory> query = em.createQuery(
                "select u from UserDirectory u", UserDirectory.class);
        List<UserDirectory> resultList = query.getResultList();
        resultList.forEach(u -> autowire(u));
        return resultList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
