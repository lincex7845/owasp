package controllers

import javax.inject._
import play.api._
import models.UserLoginForm
import play.api.libs.json.Json.toJson
import play.api.libs.json.{JsSuccess, Json, Reads}
import play.api.mvc._

@Singleton
class ApplicationController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def index() =  Action{
    Ok(views.html.login()).withNewSession
  }

  def login() = Action(parse.json){request =>

    implicit  val loginRequest: Reads[UserLoginForm] = Json.reads[UserLoginForm]

    request.body.validate[UserLoginForm] match {
      case s: JsSuccess[UserLoginForm] if true => {
        Ok(toJson(Map("valid" -> true))).withSession("user" -> s.get.username)
      }
      case _ => Forbidden(toJson(Map("valid" -> false)))
    }
  }

}
