//> using target.scope test

package eu.joaocosta.spaeti

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class MainStateSpec extends munit.FunSuite:
  test("isLoading returns the app loading state"):
    assertEquals(MainState(Future.never, None, "", 0).isLoading, true)
    assertEquals(MainState(Future.successful(Nil), None, "", 0).isLoading, false)
    assertEquals(MainState(Future.failed(new Exception("")), None, "", 0).isLoading, false)

  test("isPerformingOperation returns the app loading state"):
    assertEquals(MainState(Future.never, None, "", 0).isPerformingOperation, false)
    assertEquals(MainState(Future.never, Some(Future.successful(())), "", 0).isPerformingOperation, false)
    assertEquals(MainState(Future.never, Some(Future.failed(new Exception(""))), "", 0).isPerformingOperation, false)
    assertEquals(MainState(Future.never, Some(Future.never), "", 0).isPerformingOperation, true)

  test("apps returns the filtered list of apps"):
    val apps = Future.successful(
      List(
        AppStatus(AppId(Some("id-a"), "a"), false),
        AppStatus(AppId(Some("b"), "b"), false),
        AppStatus(AppId(None, "ab"), false)
      )
    )
    assertEquals(
      MainState(apps, None, "", 0).apps,
      List(
        AppStatus(AppId(Some("id-a"), "a"), false),
        AppStatus(AppId(Some("b"), "b"), false),
        AppStatus(AppId(None, "ab"), false)
      )
    )
    assertEquals(
      MainState(apps, None, "a", 0).apps,
      List(
        AppStatus(AppId(Some("id-a"), "a"), false),
        AppStatus(AppId(None, "ab"), false)
      )
    )
    assertEquals(
      MainState(apps, None, "b", 0).apps,
      List(
        AppStatus(AppId(Some("b"), "b"), false),
        AppStatus(AppId(None, "ab"), false)
      )
    )
    assertEquals(
      MainState(apps, None, "id", 0).apps,
      List(
        AppStatus(AppId(Some("id-a"), "a"), false)
      )
    )

  test("errorMessage returns the current error"):
    assertEquals(MainState(Future.never, None, "", 0).errorMessage, None)
    assertEquals(MainState(Future.never, Some(Future.successful(())), "", 0).errorMessage, None)
    assertEquals(
      MainState(Future.never, Some(Future.failed(new Exception("error"))), "", 0).errorMessage,
      Some("error")
    )
    assertEquals(MainState(Future.never, Some(Future.never), "", 0).errorMessage, None)
