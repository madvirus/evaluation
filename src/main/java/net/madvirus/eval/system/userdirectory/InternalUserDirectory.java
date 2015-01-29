package net.madvirus.eval.system.userdirectory;

import net.madvirus.eval.query.user.UserModel;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("INTERNAL")
public class InternalUserDirectory extends UserDirectory {
    @Override
    public UserModel authenticate(String id, String password) {
        UserModel userModel = userModelRepository.findOne(id);
        if (userModel == null) return null;
        return userModel.mathPassword(password) && userModel.equalDirectoryId(getId()) ? userModel : null;
    }

}
