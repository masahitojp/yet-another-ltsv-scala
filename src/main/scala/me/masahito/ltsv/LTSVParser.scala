package me.masahito.ltsv

import scala.util.parsing.combinator.RegexParsers
import scala.IllegalArgumentException

/** *
  * Forked at seratch's LTSVParser
  *  (https://github.com/seratch/ltsv4s/blob/develop/src/main/scala/com/github/seratch/ltsv4s/LTSVParser.scala)
  */
trait LTSVParser extends RegexParsers {

  private var _ignores: List[String] = List()
  private var _wants: List[String] = List()

  def ignores( ignores: String*):LTSVParser = {
    this._ignores = ignores.toList
    this
  }

  def wants(wants: String*) : LTSVParser = {
    this._wants = wants.toList
    this
  }

  // parsers
  override def skipWhitespace = false

  private def record = field ~ ( tab ~> field ).* ^^ {
    case first ~ other => first :: other
  }
  private def field = label ~ ":" ~ fieldValue ^^ {
    case None ~ ":" ~ v => None
    case Some(k) ~ ":" ~ v => Some((k, v))
  }
  private def label: Parser[Option[String]] = "[0-9A-Za-z_\\.-]+".r ^^ {
    case k if this._ignores.contains(k) => None
    case k if ((this._ignores.size < 1) && (this._wants.size > 0) && !this._wants.contains(k)) => None
    case k => Some(k)
  }
  private def fieldValue = """[\u000B\u000C\u0001-\u0008\u000E-\u00FF]*""".r
  def tab = '\t'

  private def parseLTSVLine(input: String): Either[IllegalArgumentException, List[Option[Tuple2[String, String]]]] = {
    parseAll(record, input) match {
      case Success(result, next) => Right(result)
      case failure : NoSuccess => Left(new IllegalArgumentException(failure.toString))
    }
  }

  def parseLine(input: String):Either[IllegalArgumentException, Map[String, String]] = {
    var list = List.empty[Tuple2[String, String]]
    parseLTSVLine(input).fold(
      left => Left(new IllegalArgumentException("")),
      right => {
        right.map{
          (opt: Option[(String, String)]) => {
            opt match {
              case Some(x) =>  list = x :: list
              case _ => // Do Nothing
            }
          }
        }
        Right(list.reverse.toMap)
      }
    )
  }
}
