package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.Task
import play.api.libs.json.Json


@Singleton
class TaskController  @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  private var tasks: Seq[Task] = Seq(
    Task(1, "Task 1", "Description for Task 1"),
    Task(2, "Task 2", "Description for Task 2")
  )

  def getTasks: Action[AnyContent] = Action {
    Ok {
      Json.toJson(tasks)
    }
  }

  def getTask(id: Long): Action[AnyContent] = Action {
    tasks.find(_.id == id).map(task => Ok(Json.toJson(task))).getOrElse(NotFound)
  }

  def createTask: Action[AnyContent] = Action { implicit request =>
    val jsonBody = request.body.asJson
    jsonBody.flatMap(_.asOpt[Task]).map { task =>
      tasks = tasks :+ task.copy(id = tasks.length + 1)
      Created(Json.toJson(task.copy(id = tasks.length)))
    }.getOrElse(BadRequest("Invalid task format"))
  }

  def updateTask(id: Long): Action[AnyContent] = Action { implicit request =>
    val jsonBody = request.body.asJson
    jsonBody.flatMap(_.asOpt[Task]).map { newTask =>
      tasks.find(_.id == id).map { existingTask =>
        tasks = tasks.filterNot(_.id == id) :+ newTask.copy(id = existingTask.id)
        Ok(Json.toJson(newTask.copy(id = existingTask.id)))
      }.getOrElse(NotFound)
    }.getOrElse(BadRequest("Invalid task format"))
  }

  def deleteTask(id: Long): Action[AnyContent] = Action {
    tasks.find(_.id == id).map { task =>
      tasks = tasks.filterNot(_.id == id)
      Ok(s"Task $id deleted")
    }.getOrElse(NotFound)
  }
}
