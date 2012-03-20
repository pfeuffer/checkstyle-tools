package de.pfeufferweb.tools.checkstyle

import scala.xml._
import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import de.pfeufferweb.tools.FileFinder

/**
 * Diese Klasse liest die Meldungen aus mehreren Checkstyle-Dateien und summiert
 * die Fehler gruppiert nach Fehlerart oder Datei.
 * <p>
 * Aufruf: CheckstyleSum [files|errors] <Checkstyle-Dateien>
 */
object CheckstyleSum {

  def main(args: Array[String]) {
    val counter = if (args(0) == "files") new ByFileCounter else new ByErrorCounter
    new CheckstyleSum(counter).process(args.drop(1))
    counter.printCounts
    counter.printSum
  }
}

class CheckstyleSum(counter: Counter) {

  def process(files: Array[String]) = {
    getFiles(files).foreach(readFile(counter, _))
  }

  private def getFiles(files: Array[String]) = {
    if (files.isEmpty) new FileFinder("checkstyle-result.xml").findFiles else files
  }

  private def readFile(counter: Counter, checkstyleFile: String) {
    val data = XML.loadFile(checkstyleFile)
    counter process data
  }
}

abstract class Counter {

  val counts = new HashMap[String, Int]

  def readFile(checkstyleFile: String) = process(XML.loadFile(checkstyleFile))

  def process(data: Elem)

  def inc(s: String, n: Int = 1) = counts += Pair(s, counts.get(s).getOrElse(0) + n)

  def printCounts =
    counts.toList.sortBy({ _._2 }).foreach(x => println(x._2 + "\t" + x._1))

  def printSum =
    println(counts.values.foldLeft(0)((sum, c) => { sum + c }))
}

class ByFileCounter extends Counter {

  def process(data: Elem) = (data \\ "file").foreach(incFile(_))

  private def incFile(file: Node) = inc((file \\ "@name").text, (file \\ "error").size)
}

class ByErrorCounter extends Counter {

  def process(data: Elem) = {
    (data \\ "file") foreach {
      file => (file \\ "error") foreach incError
    }
  }

  private def incError(entry: Node) = inc((entry \\ "@source").text)
}
