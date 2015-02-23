package net.madvirus.eval.query.user

import javax.persistence.{Column, Entity, Id, Table}

import org.springframework.security.crypto.password.StandardPasswordEncoder

@Entity
@Table(name = "user")
class UserModel(userId: String, userName: String, pwd: String, dirId: java.lang.Long) {

  @Id
  @Column(name = "user_id")
  private var id: String = userId

  @Column(name = "name")
  private var name: String = userName

  @Column(name = "password")
  private var password: String = encrypt(pwd)

  @Column(name = "user_directory_id")
  private var directoryId: java.lang.Long = dirId

  def this(userId: String, userName: String, pwd: String) = this(userId, userName, pwd, 0L)

  def this() = this(null, null, null, null)

  def getId() = id

  def getName() = name

  def getDirectoryId() = directoryId

  def mathPassword(pwd: String) = new StandardPasswordEncoder().matches(pwd, password)

  private def encrypt(pwd: String): String = if (pwd == null) null else new StandardPasswordEncoder().encode(pwd)

  def equalDirectoryId(dirId: Long): Boolean = directoryId == dirId


  def canEqual(other: Any): Boolean = other.isInstanceOf[UserModel]

  override def equals(other: Any): Boolean = other match {
    case that: UserModel =>
      (that canEqual this) &&
        id == that.id &&
        name == that.name &&
        password == that.password &&
        directoryId == that.directoryId
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(id, name, password, directoryId)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
