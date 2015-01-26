package net.madvirus.eval.query.user

import org.springframework.data.repository.CrudRepository

trait UserModelRepository extends CrudRepository[UserModel, String] {

  def findByName(name: String): UserModel
}
