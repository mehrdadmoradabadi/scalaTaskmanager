package repositories
import javax.inject.{Inject, Singleton}
import models.{Task, TaskTable}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaskRepository @Inject()(implicit ec: ExecutionContext, db: Database) {
  val tasks = TableQuery[TaskTable]

  def create(task: Task): Future[Task] = {
    db.run(tasks returning tasks.map(_.id) into ((task, id) => task.copy(id = id)) += task)
  }

  // Implement other CRUD operations similar to the above method
}