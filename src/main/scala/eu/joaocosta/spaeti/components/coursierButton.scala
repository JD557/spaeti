package eu.joaocosta.spaeti.components

import scala.concurrent.Future

import eu.joaocosta.interim.*
import eu.joaocosta.interim.InterIm.*
import eu.joaocosta.spaeti.*

/** Button that triggers an async coursier operation */
def coursierButton(
    id: ItemId,
    appId: AppId,
    opName: String,
    op: AppId => Future[?]
): DynamicComponentWithValue[MainState] =
  new DynamicComponentWithValue[MainState]:
    val opButton = button(id, opName)

    def allocateArea(using allocator: LayoutAllocator.AreaAllocator): Rect =
      opButton.allocateArea(using allocator)

    def render(area: Rect, appState: Ref[MainState]): Component[Unit] =
      appState.modifyIf(
        button(id, opName)(area)(true).getOrElse(false)
      )(_.runOperation(op(appId)))
