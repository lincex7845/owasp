package controllers

import javax.inject.Inject

import play.api.mvc._

@Singleton
class SessionController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def login() = Action{ implicit request: Request[AnyContent] =>

  }

}
