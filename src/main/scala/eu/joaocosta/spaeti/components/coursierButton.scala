package eu.joaocosta.spaeti.components

import scala.concurrent.Future

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** Button that triggers an async coursier operation coun */
def coursierButton(area: Rect, appId: AppId, opName: String, op: AppId => Future[_]): ComponentWithValue[MainState] =
  new ComponentWithValue[MainState]:
    def render(appState: Ref[MainState]): Component[Unit] =
      appState.modifyIf(
        button(s"${appId.name}_${opName}", area, opName)
      )(_.runOperation(op(appId)))
