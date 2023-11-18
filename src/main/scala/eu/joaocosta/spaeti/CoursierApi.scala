package eu.joaocosta.spaeti

import java.time.Instant

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future, blocking}
import scala.util.Try

import coursier.*
import coursier.install.*

object CoursierApi:
  private val channels = Channels().withChannels(
    Channels.defaultChannels ++ Channels.contribChannels
  )

  private lazy val cachedAllApps = search("")

  /** Search all apps matching a query */
  def search(query: String): Future[List[AppId]] =
    channels.searchAppName(List(query)).future.map { results =>
      results
        .map { id =>
          val name = Try(
            channels.appDescriptor(id).unsafeRun().appDescriptor.nameOpt
          ).toOption.flatten
          AppId(Some(id), name.getOrElse(id))
        }
    }

  /** List all installed apps */
  def list(): List[AppId] =
    InstallDir().list().toList.map { name =>
      val source = List(name, name + ".bat").flatMap { file =>
        Try(
          InfoFile.readSource(InstallDir().baseDir.resolve(file)).map(_._1)
        ).toOption.flatten
      }.headOption
      AppId(source.map(_.id), name)
    }

  /** Fetches the status of all known apps
    *
    *  This method caches the result of the first request to avoid calls to the server.
    */
  def fetchStatus(): Future[List[AppStatus]] = cachedAllApps.map { allApps =>
    val installedApps = list().toSet
    (allApps.toSet ++ installedApps)
      .map(appId => AppStatus(appId, installedApps(appId)))
      .toList
      .sortBy(app => (!app.installed, app.appId.name, app.appId.id))
  }

  /** Installs an app */
  def install(appId: AppId): Future[Option[Boolean]] =
    appId.id
      .map(id =>
        channels
          .appDescriptor(id)
          .map(appInfo => InstallDir().createOrUpdate(appInfo))
          .future
      )
      .getOrElse(
        Future.failed(new Exception("Cannot install app with unknown id."))
      )

  /** Uninstalls an app */
  def uninstall(appId: AppId): Future[Unit] =
    Future(blocking(InstallDir().delete(appId.name)))

  /** Updates an app */
  def update(appId: AppId): Future[Option[Boolean]] =
    appId.id
      .map(id =>
        channels
          .appDescriptor(id)
          .map(appInfo => InstallDir().createOrUpdate(appInfo, Instant.now(), force = true))
          .future
      )
      .getOrElse(
        Future.failed(new Exception("Cannot update app with unknown id."))
      )
