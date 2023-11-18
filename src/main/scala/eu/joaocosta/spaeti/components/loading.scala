package eu.joaocosta.spaeti.components

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** Loading screen */
def loading(area: Rect, performingOperation: Boolean): Component[Unit] =
  val dots = "...".take(((System.currentTimeMillis() / 500) % 4).toInt)
  val message =
    if (performingOperation) "Performing operation, please wait" + dots
    else "Loading data, please wait" + dots
  text(area, Color(0, 0, 0), message, Font.default, centerHorizontally, centerVertically)
