package eu.joaocosta.spaeti.components

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** Header component with the search bar */
def header(area: Rect): ComponentWithValue[MainState] =
  new ComponentWithValue[MainState]:
    val headerColor = Color(200, 200, 220)
    val textColor   = Color(0, 0, 0)

    def render(appState: Ref[MainState]): Component[Unit] =
      appState.modifyRefs: (_, _, query, offset) =>
        rectangle(area, headerColor)
        columns(area.shrink(8), 4, 5): column =>
          text(column(0), textColor, "Sp\u00A4ti", Font("unscii", 16, 8), alignLeft, centerVertically)
          text(column(2), textColor, "Search:", Font.default, alignRight, centerVertically)
          textInput("search", column(3))(query)
          if (query.get != appState.get.query) offset := 0
