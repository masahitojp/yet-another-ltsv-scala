package me.masahito.ltsv

import scala.util.parsing.combinator.RegexParsers
import scala.IllegalArgumentException
import java.io.{FileInputStream, InputStreamReader, BufferedReader}
import io.Source

/** *
  * Forked at seratch's LTSVParser
  *  (https://github.com/seratch/ltsv4s/blob/develop/src/main/scala/com/github/seratch/ltsv4s/LTSVParser.scala)
  */
class LTSVParser extends RegexParsers {

  private var _ignores: List[String] = List()
  private var _wants: List[String] = List()

  // --------------------------------------------------------------------------
  // LTSV Definition
  //  ABNF:
  //   ltsv = *(record NL) [record]
  //   record = [field *(TAB field)]
  //   field = label ":" field-value
  //   label = 1*lbyte
  //   field-value = *fbyte
  //
  //   TAB = %x09
  //   NL = [%x0D] %x0A
  //   lbyte = %x30-39 / %x41-5A / %x61-7A / "_" / "." / "-" ;; [0-9A-Za-z_.-]
  //   fbyte = %x01-08 / %x0B / %x0C / %x0E-FF
  // --------------------------------------------------------------------------

  override def skipWhitespace = false

  private def ltsv: Parser[List[Map[String, String]]] = repsep(record, nl)

  private def record = field ~ ( tab ~> field ).* ^^ {
    case Some(first) ~ other => (first :: other.flatMap(s => s)).toMap
    case None ~ other => other.flatMap(s => s).toMap
  }
  private def field = label ~ ":" ~ fieldValue ^^ {
    case Some(k) ~ ":" ~ v => Some((k, v))
    case None ~ ":" ~ v => None
  }
  private def label: Parser[Option[String]] = "[0-9A-Za-z_\\.-]+".r ^^ {
    case k if this._ignores.contains(k) => None
    case k if ((this._ignores.size < 1) && (this._wants.size > 0) && !this._wants.contains(k)) => None
    case k => Some(k)
  }
  private def fieldValue = """[\u000B\u000C\u0001-\u0008\u000E-\u00FF]*""".r

  private def tab = '\t'

  private def nl = opt("\r") ~ "\n"

  private def parse(input: String): Either[IllegalArgumentException, List[Map[String, String]]] = {
    parseAll(ltsv, input) match {
      case Success(result, next) => Right(result)
      case failure : NoSuccess => Left(new IllegalArgumentException(failure.toString))
    }
  }

  // public I/F
  /**
   * Specifies keys to be ignored
   * @param ignores keys to be ignored
   * @return LTSVParser Object
   */
  def ignores( ignores: String*):LTSVParser = {
    this._ignores = ignores.toList
    this
  }

  /**
   * Specifies keys to be wanted
   * @param wants wkeys to be wanted
   * @return LTSVParser Object
   */
  def wants(wants: String*) : LTSVParser = {
    this._wants = wants.toList
    this
  }

  /**
   * parse line
   * @param input line
   * @return
   */
  def parseLine(input: String):Either[IllegalArgumentException, Map[String, String]] = {
    parse(input).fold(
      left => Left(left),
      right => {
        Right(right.head)
      }
    )
  }

  /**
   * parse lines
   * @param input lines
   * @return
   */
  def parseLines(input: String):Either[IllegalArgumentException, List[Map[String, String]]] = {
    parse(input).fold(
      left => Left(left),
      right => {
        Right(right)
      }
    )
  }

  def parseFile[U](filePath: String, charSet: String = "UTF-8"): Either[IllegalArgumentException, List[Map[String, String]]] = {
    var source = Source.fromFile(filePath, charSet).mkString
    parseLines(source)
  }

  def parseFileIter[U](filePath: String, charSet: String = "UTF-8")(body:Iterator[Either[IllegalArgumentException, Map[String, String]]] => U) =  {
    val in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charSet))
    try{
      body(new LTSVIterator(in, this))
    }
    finally {
      in.close()
    }
  }

}
