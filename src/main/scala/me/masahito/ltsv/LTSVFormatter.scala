package me.masahito.ltsv

trait LTSVFormatter {
  def formatLine(line: Map[String, String]): String = {
    line.map{case(k,v) => k + ":" + v }.mkString("\t")
  }

  def formatLines(lines: List[Map[String, String]]): List[String]
    = lines.map(this.formatLine)
}
