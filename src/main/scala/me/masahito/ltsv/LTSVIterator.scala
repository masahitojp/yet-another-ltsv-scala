package me.masahito.ltsv

import java.io.BufferedReader

/**
 * original: http://xerial.org/scala-cookbook/recipes/2012/06/28/reading-files/
 * @param in BufferedReader
 */
class LTSVIterator (in:BufferedReader, parser: LTSVParser) extends Iterator[Either[Throwable, Map[String, String]]] {
  private var nextLine : String = null

  def hasNext = {
    if(nextLine == null){
      nextLine = in.readLine
    }
    nextLine != null
  }

  def next() = {
    if(hasNext) {
      val line = nextLine
      nextLine = null
      parser.parseLine(line)
    }
    else Iterator.empty.next()
  }
}

