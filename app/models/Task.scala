package models
import play.api.libs.json._
import slick.lifted.Tag
import slick.jdbc.MySQLProfile.api._
case class Task(id: Long, title: String, description: String)

object Task {
  def tupled: ((Long, String, String)) => Task = (Task.apply _).tupled
  implicit val taskWrites: Writes[Task] = Json.writes[Task]
  implicit val taskReads: Reads[Task] = Json.reads[Task]
}
class TaskTable(tag: Tag) extends Table[Task](tag, "tasks") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def description = column[String]("description")

  override def * = (id, title, description).<>(Task.tupled, Task.unapply)
}