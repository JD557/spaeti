package eu.joaocosta.spaeti.components

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** Loading screen */
def loading(area: Rect | LayoutAllocator.AreaAllocator, performingOperation: Boolean): Component[Unit] =
  val reservedArea = area match {
    case r: Rect                              => r
    case alloc: LayoutAllocator.AreaAllocator => alloc.fill()
  }
  val dots    = "...".take(((System.currentTimeMillis() / 500) % 4).toInt)
  val message =
    if (performingOperation) "Performing operation, please wait" + dots
    else "Loading data, please wait" + dots
  text(reservedArea, Color(0, 0, 0), message, Font.default, centerHorizontally, centerVertically)
