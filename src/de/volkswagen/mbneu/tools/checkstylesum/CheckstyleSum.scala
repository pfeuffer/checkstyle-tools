package de.volkswagen.mbneu.tools.checkstylesum

import scala.xml._
import scala.collection.mutable.Map
import scala.collection.mutable.HashMap

/**
 * Diese Klasse liest die Meldungen aus mehreren Checkstyle-Dateien und summiert
 * die Fehler gruppiert nach Fehlerart oder Datei.
 * <p>
 * Aufruf: CheckstyleSum [files|errors] <Checkstyle-Dateien>
 */
object CheckstyleSum {

  def main(args: Array[String]) {
    val counter = if (args(0) == "files") new ByFileCounter else new ByErrorCounter
    args.drop(1).foreach(readFile(counter, _))
    printCounts(counter.counts)
    printSum(counter.counts)
  }

  private def readFile(counter: Counter, checkstyleFile: String) {
    val data = XML.loadFile(checkstyleFile)
    counter process data
  }

  private def printCounts(counts: Map[String, Int]) =
    counts.toList.sortBy({ _._2 }).foreach(x => println(x._2 + "\t" + x._1))

  private def printSum(counts: Map[String, Int]) =
    println(counts.values.foldLeft(0)((sum, c) => { sum + c }))

}

private abstract class Counter {

  val counts = new HashMap[String, Int]

  def readFile(checkstyleFile: String) = process(XML.loadFile(checkstyleFile))

  def process(data: Elem)

  def inc(s: String, n: Int = 1) = counts += Pair(s, counts.get(s).getOrElse(0) + n)
}

private class ByFileCounter extends Counter {

  def process(data: Elem) = (data \\ "file").foreach(incFile(_))

  private def incFile(file: Node) = inc((file \\ "@name").text, (file \\ "error").size)
}

private class ByErrorCounter extends Counter {

  def process(data: Elem) = {
    (data \\ "file") foreach {
      file => (file \\ "error") foreach incError
    }
  }

  private def incError(entry: Node) = inc((entry \\ "@source").text)
}
