package de.volkswagen.mbneu.tools.checkstylesum

import scala.xml._

/**
 * Diese Klasse liest die gegebenen Checkstyle-Ergebnisdateien und erstellt daraus
 * eine Checkstyle-konforme XML-Suppressions-Datei für alle gefundenen Fehler.
 */
object CheckstyleSuppress {

  def main(args: Array[String]) = println(new PrettyPrinter(800, 4).format(generateXml(args)))

  private def generateXml(files: Array[String]) = {
    <suppressions>{ files map readFile }</suppressions>
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
