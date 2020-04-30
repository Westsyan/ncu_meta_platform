package utils

import java.io.File

import config.MyFile

import scala.sys.process._

class ExecCommand extends MyFile{
  var isSuccess: Boolean = false
  val err = new StringBuilder
  val out = new StringBuilder
  val log = ProcessLogger(out append _ append "\n", err append _ append "\n")

  def exec(command: String) = {
    val exitCode = command ! log
    if (exitCode == 0) isSuccess = true
  }

  def exec(command1: String, command2: String) = {
    val exitCode = (command1 #&& command2) ! log
    if (exitCode == 0) isSuccess = true
  }

  def exec(command: Array[String]) = {
    val exitArray = command.map { x =>
      val exitCode = Process(x) ! log
      println(getInfo(x, exitCode))
      exitCode
    }
    val invalid = exitArray.count(!_.equals(0))
    if (invalid == 0) isSuccess = true

  }

  def execo(command: String, outFile: File) = {
    val exitCode = (command #> outFile) ! log
    if (exitCode == 0) isSuccess = true
  }

  def exect(command: String, tmpDir: String) = {
    val exitCode = Process(command, new File(tmpDir)) ! log
    println(getInfo(command, exitCode))
    if (exitCode == 0) isSuccess = true
  }


  def exect(command: Array[String], tmpDir: String) = {
    import scala.util.control.Breaks._
    var exit = 0
    breakable({
      command.foreach {x=>
        val exitCode =  Process(x,new File(tmpDir)) ! log
        println(getInfo(x, exitCode))
        exit += exitCode
        if (exit != 0) break()
      }
    })

    if (exit == 0) isSuccess = true
  }

  def getInfo(command: String, exitCode: Int): String = {
    val commands = command.split(" ").take(3)
    val proname = if (commands.head == "python" || commands.head == "perl" || commands.head == "sh") {
      commands.drop(1).head.split("/").last
    } else if (commands.head == "java") {
      commands.drop(2).head.split("/").last
    } else {
      commands.head.split("/").last
    }
    val info = if (exitCode == 0) {
      s"[info] Programe $proname run success!"
    } else {
      s"[error] Programe $proname run falied!"
    }

    info
  }

  def getErrStr = {
    err.toString()
  }

  def getOutStr = {
    out.toString()
  }


}
