package eu.joaocosta.spaeti.components

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** App entry on a list */
def appEntry(id: ItemId, app: AppStatus): ComponentWithValue[MainState] =
  new ComponentWithValue[MainState]:
    val appName: String =
      app.appId.name + app.appId.id
        .filter(_ != app.appId.name)
        .map(id => s" ($id)")
        .getOrElse("")

    val installButton   = coursierButton(id |> "install", app.appId, "Install", CoursierApi.install)
    val updateButton    = coursierButton(id |> "update", app.appId, "Update", CoursierApi.update)
    val uninstallButton = coursierButton(id |> "uninstall", app.appId, "Uninstall", CoursierApi.uninstall)

    def render(area: Rect, appState: Ref[MainState]): Component[Unit] =
      columns(area.shrink(3), 4, 5): column ?=>
        text(column.nextCell() ++ column.nextCell(), Color(0, 0, 0), appName, Font.default, alignLeft, centerVertically)
        if (app.installed)
          updateButton(appState)
          uninstallButton(appState)
        else installButton(appState)
