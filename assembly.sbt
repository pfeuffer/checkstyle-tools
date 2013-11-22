import AssemblyKeys._ // put this at the top of the file

assemblySettings

// your assembly settings here
mainClass in assembly := Some("de.pfeufferweb.tools.checkstyle.CheckstyleSuppress")

//mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
//  {
//    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.first
//    case x => old(x)
//  }
//}
