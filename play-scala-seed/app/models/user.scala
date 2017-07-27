package models

import play.api.data.Form
import play.api.data.Forms._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

case class User(id: Int, username: String, password: String)

case class UserLoginForm(username: String, password: String)

object LoginForm{
  val form = Form(
    mapping(
    "username" -> nonEmptyText(minLength = 4, maxLength = 6).verifying("", _.matches("")),
    "password" -> nonEmptyText(minLength = 8, maxLength = 20)
  )(UserLoginForm.apply)(UserLoginForm.unapply))
}

case class UserRegisterForm(username: String, password: String, repeatedPassword: String)

object RegisterForm{
  val form = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "repeatedPassword" -> nonEmptyText
    )(UserRegisterForm.apply)(UserRegisterForm.unapply)
  )
}

trait Db {
  val config: DatabaseConfig[JdbcProfile]
  val db : JdbcProfile#Backend#Database = config.db
}

trait UsersTable{ this : Db =>
  import config.profile.api._

  class Users(tag: Tag) extends Table[User](tag, "users"){

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def password = column[String]("password")

    override def * = (id, username, password) <>(User.tupled, User.unapply)
  }

  val users = TableQuery[Users]
}

