package models

import play.api.data.Form
import play.api.data.Forms._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
//import slick.jdbc.MySQLProfile.api._
import slick.lifted.ProvenShape
import scala.concurrent.ExecutionContext.Implicits.global


import scala.concurrent.Future

case class User(id: Option[Int], username: String, password: String)

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

trait DbConfiguration{

  lazy val config= DatabaseConfig.forConfig[JdbcProfile]("db")
}

trait UsersTable{ this : Db =>
  import config.profile.api._

  class Users(tag: Tag) extends Table[User](tag, "users"){

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def password = column[String]("password")

    override def * = (id.?, username, password) <>(User.tupled, User.unapply)
  }

  val users = TableQuery[Users]
}

class UserRepository(val config: DatabaseConfig[JdbcProfile]) extends Db with UsersTable {

  import config.profile.api._

  def insertUser(u: User): Future[User] = db
    .run(users returning users.map(_.id) += u)
    .map(i => u.copy(id = Some(i)))

  def find(id: Int): Future[Option[User]] = db.run(users.filter(_.id === id).result.headOption)

  def findUserName(username: String) = db.run(users.filter(_.username === username).result.headOption)
}
//
//object Main extends App with DbConfiguration{
//
//  import scala.concurrent.Await
//  import scala.concurrent.duration._
//
//  val repo = new UserRepository(config)
//
//  repo.insertUser(User(None, "abvd", "passwd"))
//
//  Await.result(repo.find(1).map(println), 10.seconds)
//
//}

