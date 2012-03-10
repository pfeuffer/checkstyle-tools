package de.pfeufferweb.tools

import java.io.File

class FileFinder(matcher: File => Boolean) {

  def this(name: String) = this(_.getName == name)

  def findFiles: Array[String] = findFiles(new File("."))

  def findFiles(startDir: File): Array[String] = {
    val filesInActualDir = startDir.listFiles
    val found = filesInActualDir.filter(matcher).map(_.getAbsolutePath)
    found ++ filesInActualDir.filter(_.isDirectory()).flatMap(findFiles(_))
  }
}