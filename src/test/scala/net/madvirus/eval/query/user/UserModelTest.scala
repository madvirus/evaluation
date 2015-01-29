package net.madvirus.eval.query.user

import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class UserModelTest {

  @Test
  def passwordMatch(): Unit = {
    var user = new UserModel("id", "name", "password")
    assertThat(user.mathPassword("password"), equalTo(true))
    assertThat(user.mathPassword("password1"), equalTo(false))
  }
}
