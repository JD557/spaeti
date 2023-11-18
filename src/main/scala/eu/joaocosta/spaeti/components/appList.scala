package eu.joaocosta.spaeti.components

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** List with all apps */
def appList(area: Rect): ComponentWithValue[MainState] =
  new ComponentWithValue[MainState]:
    val rowSize    = 24
    val rowPadding = 1
    val maxApps    = area.h / (rowSize + rowPadding)
    val sliderSize = 16

    val evenColor                 = Color(240, 240, 240)
    val oddColor                  = Color(230, 230, 230)
    def rowColor(idx: Int): Color = if (idx % 2 == 0) evenColor else oddColor

    def render(appState: Ref[MainState]): Component[Unit] =
      dynamicColumns(area, 3): nextColumn =>
        val maxOffset = math.max(0, appState.get.apps.size - maxApps)
        appState.modifyRefs: (_, _, _, offset) =>
          slider("appScroll", nextColumn(-sliderSize), 0, maxOffset)(offset)
        val start = appState.get.offset
        val end   = start + maxApps
        rows(nextColumn(maxSize), maxApps, rowPadding): row =>
          appState.get.apps.zipWithIndex
            .slice(start, end)
            .zip(row)
            .foreach:
              case ((app, idx), appArea) =>
                rectangle(appArea, rowColor(idx))
                appEntry(appArea, app)(appState)
