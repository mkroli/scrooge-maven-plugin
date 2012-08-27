package com.github.mkroli.maven.scrooge

import java.io.File

import org.apache.maven.project.MavenProject

import com.twitter.scrooge.Importer
import com.twitter.scrooge.ScroogeParser
import com.twitter.scrooge.ServiceOption
import com.twitter.scrooge.TypeResolver
import com.twitter.scrooge.WithFinagleClient
import com.twitter.scrooge.WithFinagleService
import com.twitter.scrooge.WithOstrichServer
import com.twitter.scrooge.javagen.JavaGenerator
import com.twitter.scrooge.scalagen.ScalaGenerator

class ScroogeWrapper(mavenProject: MavenProject,
  thriftDirectory: File,
  outputDirectory: File,
  language: String,
  ostrich: Boolean) {
  def generate() {
    val generator = language match {
      case "java" => new JavaGenerator
      case "scala" => new ScalaGenerator
    }
    val importer = Importer.fileImporter(thriftDirectory.getAbsolutePath() :: Nil)
    val parser = new ScroogeParser(importer)
    val serviceFlags: Set[ServiceOption] = Set(WithFinagleClient, WithFinagleService) ++
      (if (ostrich) Set(WithOstrichServer) else Set())

    for (inputFile <- thriftDirectory.listFiles()) {
      generator(TypeResolver().resolve(
        parser.parseFile(
          inputFile.getAbsolutePath())).document,
        serviceFlags,
        outputDirectory)
    }
    mavenProject.addCompileSourceRoot(outputDirectory.getAbsolutePath())
  }
}
