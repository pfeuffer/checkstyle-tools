package de.volkswagen.mbneu.tools.checkstylesum

import scala.xml._
import java.io.File

/**
 * Diese Klasse liest die gegebenen Checkstyle-Ergebnisdateien und erstellt daraus
 * eine Checkstyle-konforme XML-Suppressions-Datei für alle gefundenen Fehler.
 */
object CheckstyleSuppress {

  def main(args: Array[String]) = println(new PrettyPrinter(800, 4).format(generateXml(args)))

  //  def main(args: Array[String]) = XML.saveFull("checkstyleSuppressions.xml", generateXml(args), "UTF-8", true, null)

  private def generateXml(files: Array[String]) = {
    <suppressions>{ getFiles(files) map readFile }</suppressions>
  }

  private def getFiles(files: Array[String]) = {
    if (files.isEmpty) findCheckstyleResults(new File(".")) else files
  }

  private def findCheckstyleResults(f: File): Array[String] = {
    val these = f.listFiles
    val checkstyleFiles = these.filter(_.getName == "checkstyle-result.xml").map(_.getAbsolutePath)
    checkstyleFiles ++ these.filter(_.isDirectory()).flatMap(findCheckstyleResults(_))
  }

  private def readFile(checkstyleFile: String) = {
    val data = XML.loadFile(checkstyleFile)
    (data \\ "file").map(createSuppressionsForFileNode(_))
  }

  private def createSuppressionsForFileNode(file: Node) =
    (file \\ "error").map(suppress(extractFileName(file), _))

  private def extractFileName(file: Node) = {
    val absoluteFileName = (file \\ "@name").text
    absoluteFileName.substring(absoluteFileName.lastIndexOf("\\") + 1)
  }

  private def suppress(file: String, error: Node) =
    <suppress checks={ (error \\ "@source").text } files={ file } lines={ (error \\ "@line").text }/>
}
