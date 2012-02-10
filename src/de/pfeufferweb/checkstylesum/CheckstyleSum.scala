package de.pfeufferweb.checkstylesum

import scala.xml._
import scala.collection.mutable.Map
import scala.collection.mutable.HashMap

object CheckstyleSum {

  val errorCounts = new HashMap[String, Int]
  val fileCounts = new HashMap[String, Int]

  def main(args: Array[String]) {
    args.foreach(readFile)
    errorCounts.toList.sortBy({ _._2 }).foreach(x => println(x._2 + "\t" + x._1))
    println(fileCounts.values.foldLeft(0)((sum, c) => {sum + c}))
  }

  private def readFile(checkstyleFile: String) {
    val data = XML.loadFile(checkstyleFile)
    (data \\ "file") foreach { file =>
      (file \\ "error") foreach incError
      incFile(file)
    }
  }

  private def incError(entry: Node) =
    inc(errorCounts, (entry \\ "@source").text)

  private def incFile(file: Node) =
    inc(fileCounts, (file \\ "@name").text, (file \\ "error").size)

  private def inc(map: Map[String, Int], s: String, n: Int = 1) =
    map += Pair(s, map.get(s).getOrElse(0) + n)
}
