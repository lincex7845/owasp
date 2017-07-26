package models

import play.api.data.Form
import play.api.data.Forms._

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