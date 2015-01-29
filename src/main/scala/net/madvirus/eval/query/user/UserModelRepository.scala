package net.madvirus.eval.query.user

import org.springframework.data.jpa.repository.JpaRepository

trait UserModelRepository extends JpaRepository[UserModel, String] {

  def findByName(name: String): UserModel
}
