package eu.joaocosta.spaeti

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.minart.graphics.Canvas.Settings
import eu.joaocosta.spaeti.components.*

object MainApp:
  val uiContext = new UiContext()
  val fullArea  = Rect(0, 0, 600, 800)

  val appState = Ref(MainState())

  def application(inputState: InputState) =
    ui(inputState, uiContext):
      onTop(errorWindow(Rect(0, 0, 400, 200).centerAt(fullArea.centerX, fullArea.centerY), appState))
      dynamicRows(fullArea, padding = 0):
        header(appState)
        if (appState.get.isLoading || appState.get.isPerformingOperation)
          loading(summon, appState.get.isPerformingOperation)
        else appList(appState)

  @main def main() =
    MinartBackend.run(Settings(width = fullArea.w, height = fullArea.h, title = "Späti"))(application)
