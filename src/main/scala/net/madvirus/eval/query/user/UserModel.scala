package net.madvirus.eval.query.user

import javax.persistence.{Column, Entity, Id, Table}

@Entity
@Table(name="User")
class UserModel(userId: String, userName: String, pwd: String) {

    @Id
    @Column(name="USER_ID")
    private var id: String = userId

    @Column(name="NAME")
    private var name: String = userName

    @Column(name="PASSWORD")
    private var password: String = pwd

    def this() = this(null, null, null)

    def getId() = id
    def getName() = name
    def mathPassword(pwd: String) = password.equals(pwd)
}
