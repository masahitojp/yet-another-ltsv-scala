package me.masahito.ltsv

import org.scalatest.FunSuite
import java.io.IOException

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

  test("Parse lines.") {

    val ltsvString = "hoge:foo\tbar:baz\nhoge:foo\tbar:baz"
    LTSV().parseLines(ltsvString).right.map(
      result => {
        assert(result.size === 2)

        result.head.get("hoge").map(s => assert(s === "foo"))
        result.head.get("bar").map(s => assert(s === "baz"))

        result.last.get("hoge").map(s => assert(s === "foo"))
        result.last.get("bar").map(s => assert(s === "baz"))
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
    intercept[IllegalArgumentException] {
      LTSV().ignores("bar").parseLine(ltsvString).left.map(throw _)
    }
  }

  test("Exception is returned when field value don't suported string") {
    // '\u000B\u000C\u0001-\u0008\u000E-\u00FF'

    val ltsvString = "abc:あいう"
    intercept[IllegalArgumentException] {
      LTSV().parseLine(ltsvString).left.map(throw _)
    }
  }

  test("parse Ascii File") {
    assert(LTSV().parseFile("src/test/resources/test.ltsv").isRight)
    LTSV().parseFile("src/test/resources/test.ltsv").right.map(
      result => {
        assert(result.size === 3)
        assert(LTSV().formatLines(result).mkString("\n") === "a:1\tb:2\tc:3\na:4\tb:5\tc:6\na:7\tb:8\tc:9")
      }
    )
  }

  test("Iterator Ascii File") {
    LTSV().parseFileIter("src/test/resources/test.ltsv"){
      f => {
        assert(f.hasNext === true)
        val first = f.next()
        assert(first.isRight)
        first.right.map(s => {
         assert(s("a") === "1")
         assert(s("b") === "2")
         assert(s("c") === "3")
        })

        assert(f.hasNext === true)
        val second = f.next()
        assert(second.isRight)
        second.right.map(s => {
          assert(s("a") === "4")
          assert(s("b") === "5")
          assert(s("c") === "6")
        })

        assert(f.hasNext === true)
        val third = f.next()
        assert(third.isRight)
        third.right.map(s => {
          assert(s("a") === "7")
          assert(s("b") === "8")
          assert(s("c") === "9")
        })

        assert(f.hasNext === false)

      }
    }
  }

  test("Exception is returned when filePath don't exist(parseFileITer)") {

    intercept[IOException] {
      LTSV().parseFileIter("no_exist.txt"){ f => {}}
    }
  }

  test("Exception is returned when filePath don't exist (parseFile)") {

    intercept[IOException] {
      LTSV().parseFile("no_exist.txt")
    }
  }
}
