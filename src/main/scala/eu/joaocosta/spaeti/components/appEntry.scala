package eu.joaocosta.spaeti.components

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** App entry on a list */
def appEntry(area: Rect, app: AppStatus): ComponentWithValue[MainState] =
  new ComponentWithValue[MainState]:
    val appName: String =
      app.appId.name + app.appId.id
        .filter(_ != app.appId.name)
        .map(id => s" ($id)")
        .getOrElse("")

    def render(appState: Ref[MainState]): Component[Unit] =
      columns(area.shrink(3), 4, 5): column =>
        text(column(0) ++ column(1), Color(0, 0, 0), appName, Font.default, alignLeft, centerVertically)
        if (app.installed)
          coursierButton(column(2), app.appId, "Update", CoursierApi.update)(appState)
          coursierButton(column(3), app.appId, "Uninstall", CoursierApi.uninstall)(appState)
        else coursierButton(column(2), app.appId, "Install", CoursierApi.install)(appState)
