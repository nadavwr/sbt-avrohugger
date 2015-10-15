package sbtavrohugger
package tasks

import avrohugger.Generator
import avrohugger.format.{ Standard, SpecificRecord }
import sbtavrohugger.SbtAvrohugger.imports

import java.io.File

import sbt.Keys._
import sbt.{
  FileFunction,
  FilesInfo,
  Logger,
  globFilter,
  richFile,
  singleFileFinder
}

object SpecificGeneratorTask {

  private[sbtavrohugger] def specificCaseClassGeneratorTask(
    avroConfig: sbt.Configuration) = (streams,
    imports in avroConfig,
    sourceDirectory in avroConfig,
    scalaSource in avroConfig,
    target) map {
      (o, imports, srcDir, targetDir, cache) =>
        val cachedCompile = FileFunction.cached(cache / "avro",
          inStyle = FilesInfo.lastModified,
          outStyle = FilesInfo.exists) { (in: Set[File]) =>
            val generator = new Generator(SpecificRecord)
            FileWriter.generateCaseClasses(generator, imports, srcDir, targetDir, o.log)
          }
        cachedCompile((srcDir ** "*.av*").get.toSet).toSeq
    }

}
