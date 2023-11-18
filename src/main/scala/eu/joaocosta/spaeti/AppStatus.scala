package eu.joaocosta.spaeti

final case class AppId(id: Option[String], name: String)

final case class AppStatus(appId: AppId, installed: Boolean)
