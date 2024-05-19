package eu.joaocosta.spaeti.components

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** Error window */
val errorWindow: ComponentWithValue[MainState] =
  new ComponentWithValue[MainState]:
    def render(area: Rect, appState: Ref[MainState]): Component[Unit] =
      appState.get.errorMessage.foreach: message =>
        val (nextState, _) =
          window("error", "Error", closable = true)(area): windowArea =>
            text(windowArea.shrink(4), Color(0, 0, 0), message)
        appState.modifyIf(nextState.isEmpty)(_.copy(runningOperation = None))
