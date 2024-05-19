package eu.joaocosta.spaeti.components

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** List with all apps */
val appList: DynamicComponentWithValue[MainState] =
  new DynamicComponentWithValue[MainState]:
    val rowSize    = 24
    val rowPadding = 1

    val evenColor                 = Color(240, 240, 240)
    val oddColor                  = Color(230, 230, 230)
    def rowColor(idx: Int): Color = if (idx % 2 == 0) evenColor else oddColor

    def allocateArea(using allocator: LayoutAllocator.AreaAllocator): Rect =
      allocator.fill()

    def render(area: Rect, appState: Ref[MainState]): Component[Unit] =
      val maxApps    = area.h / (rowSize + rowPadding)
      dynamicColumns(area, 3, alignRight): nextColumn ?=>
        val maxOffset = math.max(0, appState.get.apps.size - maxApps)
        appState.modifyRefs: (_, _, _, offset) =>
          slider("appList" |> "scroll", 0, maxOffset)(nextColumn(16), offset)
        val start = appState.get.offset
        val end   = start + maxApps
        rows(nextColumn(maxSize), maxApps, rowPadding): row ?=>
          appState.get.apps.zipWithIndex
            .slice(start, end)
            .zip(row)
            .foreach:
              case ((app, idx), appArea) =>
                rectangle(appArea, rowColor(idx))
                appEntry("appList" |> idx, app)(appArea, appState)
