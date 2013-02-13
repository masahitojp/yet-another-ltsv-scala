package me.masahito.ltsv

import org.scalatest.FunSuite

class LTSVSuite extends FunSuite {

  test("Parse line.") {

    val ltsvString = "hoge:foo\tbar:baz"
    LTSV().parseLine(ltsvString).right.map(
      result => {
        assert(result.size === 2)
        assert( result("hoge") === "foo")
        assert( result("bar") === "baz")
      }
    )
  }

  test("It is satisfactory even if there is an empty value. ") {
    val ltsvString = "test:\tbar:baz"
    LTSV().parseLine(ltsvString).right.map(
      result => {
        assert(result.size === 2)
        assert( result("test") === "")
      }
    )
  }
  test("line formatter") {
    val ltsvString = "hoge:foo\tbar:baz"

    val test = Map("hoge" -> "foo",
                "bar" -> "baz")
    assert(LTSV().formatLine(test) === ltsvString)

  }
  test("Parse line　with a colon."){
    val ltsvString = "time:28/Feb/2013:12:00:00 +0900\thost:192.168.0.1\treq:GET /list HTTP/1.1\tstatus:200"
    LTSV().parseLine(ltsvString).right.map(
      result => {
        assert(result("time") === "28/Feb/2013:12:00:00 +0900")
        assert(result("host") === "192.168.0.1")
        assert(result("req") === "GET /list HTTP/1.1")
        assert(result("status") === "200")
      }
    )
  }


  test("Parse line with wants.") {

    val ltsvString = "hoge:foo\tbar:baz"
    LTSV().wants("bar").parseLine(ltsvString).right.map(
      result => {
        assert(result.size === 1)
        assert( result("bar") === "baz")
        assert( result.get("hoge") === None)
      }
    )
  }

  test("Parse line with ignores.") {
    val ltsvString = "hoge:foo\tbar:baz"
    LTSV().ignores("bar").parseLine(ltsvString).right.map(
      result => {
        assert(result.size === 1)
        assert( result("hoge") === "foo")
        assert( result.get("bar") === None)
      }
    )
  }

  test("Exception is returned when there is an inaccurate character string.") {
    val ltsvString = "@@:19"
    val thrown = intercept[IllegalArgumentException] {
      LTSV().ignores("bar").parseLine(ltsvString).left.map(throw _)
    }
  }


}
