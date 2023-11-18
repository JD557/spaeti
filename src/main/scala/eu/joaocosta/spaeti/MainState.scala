package eu.joaocosta.spaeti

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/** Main application state
  *
  * @param allApps list of all known apps and their status
  * @param runningOperation future with the state of any currently running operation
  * @param query search query
  * @param offset scroll offset
  */
final case class MainState(
    allApps: Future[List[AppStatus]] = CoursierApi.fetchStatus(),
    runningOperation: Option[Future[_]] = None,
    query: String = "",
    offset: Int = 0
):
  /** If true, the list of known apps are being loaded */
  def isLoading: Boolean = !allApps.isCompleted

  /** If true, an operation is currently being performed */
  def isPerformingOperation: Boolean = runningOperation.exists(!_.isCompleted)

  /** List of apps filtered by the query string */
  lazy val apps: List[AppStatus] = Await
    .result(allApps, 30.seconds)
    .filter(app => app.appId.name.contains(query) || app.appId.id.getOrElse("").contains(query))

  /** Creates a new MainState with a running operation
    *
    *  Once the operation is completed, it also update the app status
    */
  def runOperation(op: Future[_]): MainState =
    copy(
      allApps = op.transformWith(_ => CoursierApi.fetchStatus()),
      runningOperation = Some(op)
    )

  /** Error message, populated if the running operation failed */
  def errorMessage: Option[String] =
    runningOperation
      .flatMap(_.value)
      .flatMap(_.failed.toOption)
      .map(_.getMessage)
