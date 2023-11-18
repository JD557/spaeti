package eu.joaocosta.spaeti.components

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** Error window */
def errorWindow(area: Rect): ComponentWithValue[MainState] =
  new ComponentWithValue[MainState]:
    def render(appState: Ref[MainState]): Component[Unit] =
      appState.get.errorMessage.foreach: message =>
        val (nextState, _) =
          window("error", area, "Error", closable = true): windowArea =>
            text(windowArea.shrink(4), Color(0, 0, 0), message)
        appState.modifyIf(nextState.isEmpty)(_.copy(runningOperation = None))
