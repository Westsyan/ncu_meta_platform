package controllers

import java.io.File

import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Sink}
import akka.util.ByteString
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.streams.Accumulator
import play.api.mvc.MultipartFormData.FilePart
import play.core.parsers.Multipart.{FileInfo, FilePartHandler}
import utils.Utils

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FormTool {

  def handleFilePartAsFile: FilePartHandler[File] = {
    case FileInfo(partName, filename, contentType) =>
      val file = new File(Utils.tmpPath, Utils.random)
      val path = file.toPath
      val fileSink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(path)
      val accumulator: Accumulator[ByteString, IOResult] = Accumulator(fileSink)
      accumulator.map {
        case IOResult(count, status) =>
          FilePart(partName, filename, contentType, file)
      }
  }

  case class projectData(projectname: String, description: String)

  val projectForm: Form[projectData] = Form(
    mapping(
      "projectname" -> text,
      "description" -> text
    )(projectData.apply)(projectData.unapply)
  )

  case class projectnameData(projectname: String)

  val projectnameForm: Form[projectnameData] = Form(
    mapping(
      "projectname" -> text
    )(projectnameData.apply)(projectnameData.unapply)
  )

  case class newpronameData(proname: String)

  val newproForm: Form[newpronameData] = Form(
    mapping(
      "proname" -> text
    )(newpronameData.apply)(newpronameData.unapply)
  )

  case class updatePronameData(proId: Int, projectname: String,description:String)

  val updatePronameForm = Form(
    mapping(
      "proId" -> number,
      "projectname" -> text,
      "description" -> text
    )(updatePronameData.apply)(updatePronameData.unapply)
  )

  case class sampleData(proname: String, sample: String)

  val sampleForm = Form(
    mapping(
      "proname" -> text,
      "sample" -> text
    )(sampleData.apply)(sampleData.unapply)
  )

  case class sampleNameData(sample: String)

  val sampleNameForm = Form(
    mapping(
      "sample" -> text
    )(sampleNameData.apply)(sampleNameData.unapply)
  )

  case class updateSampleNameData(sampleId: String,sample:String)

  val updateSampleNameForm = Form(
    mapping(
      "sampleId" -> text,
      "sample" -> text
    )(updateSampleNameData.apply)(updateSampleNameData.unapply)
  )

  case class metaData(proname:String,name:String,sample:Seq[String])

  val metaForm = Form(
    mapping(
      "proname" -> text,
      "name" -> text,
      "sample" -> seq(text)
    )(metaData.apply)(metaData.unapply)
  )

  case class metaNameData(name:String)

  val metaNameForm = Form(
    mapping(
      "name" -> text
    )(metaNameData.apply)(metaNameData.unapply)
  )

  case class updateMetaNameData(metaId: String,name:String)

  val updateMetaNameForm = Form(
    mapping(
      "metaId" -> text,
      "name" -> text
    )(updateMetaNameData.apply)(updateMetaNameData.unapply)
  )

}

