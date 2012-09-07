/*
 * Copyright 2012 Michael Krolikowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mkroli.maven.scrooge

import java.io.File

import com.twitter.scrooge.Importer
import com.twitter.scrooge.ScroogeParser
import com.twitter.scrooge.ServiceOption
import com.twitter.scrooge.TypeResolver
import com.twitter.scrooge.WithFinagleClient
import com.twitter.scrooge.WithFinagleService
import com.twitter.scrooge.WithOstrichServer
import com.twitter.scrooge.javagen.JavaGenerator
import com.twitter.scrooge.scalagen.ScalaGenerator

trait Logger {
  def info(msg: String): Unit
}

class ScroogeWrapper(logger: Logger,
  thriftDirectory: File,
  outputDirectory: File,
  language: String,
  ostrich: Boolean,
  finagleClient: Boolean,
  finagleService: Boolean) {
  def generate() {
    val generator = language match {
      case "java" => new JavaGenerator
      case "scala" => new ScalaGenerator
    }
    val importer = Importer.fileImporter(thriftDirectory.getAbsolutePath() :: Nil)
    val parser = new ScroogeParser(importer)
    val serviceFlags: Set[ServiceOption] = Set((WithOstrichServer -> ostrich),
      (WithFinagleClient -> finagleClient),
      (WithFinagleService -> finagleService)).flatMap {
        case (f, b) if b => Some(f)
        case _ => None
      }

    for (inputFile <- thriftDirectory.listFiles()) {
      logger.info("Parsing %s".format(inputFile.getAbsolutePath()))
      generator(TypeResolver().resolve(
        parser.parseFile(
          inputFile.getAbsolutePath())).document,
        serviceFlags,
        outputDirectory)
    }
  }
}
