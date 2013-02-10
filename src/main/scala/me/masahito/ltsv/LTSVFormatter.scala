package me.masahito.ltsv

/**
 * Created with IntelliJ IDEA.
 * User: masahito
 * Date: 2013/02/11
 * Time: 1:57
 * To change this template use File | Settings | File Templates.
 */
trait LTSVFormatter {
  def formatLine(line: Map[String, String]): String = {
    line.map{case(k,v) => k + ":" + v }.mkString("\t")
  }

  def formatLines(lines: List[Map[String, String]]): List[String] = {
    lines.map(line => this.formatLine(line))
  }
}
