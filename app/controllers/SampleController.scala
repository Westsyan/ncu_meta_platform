package controllers

import config.MyConfig
import controllers.FormTool._
import dao.{ProjectDao, SampleDao}
import javax.inject._
import models.Tables.SampleRow
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.Utils

import scala.concurrent.ExecutionContext

@Singleton
class SampleController @Inject()(cc: ControllerComponents,
                                 projectDao: ProjectDao, sampleDao: SampleDao)
                                (implicit exec: ExecutionContext)
  extends AbstractController(cc) with MyConfig {

  def uploadPage(proname: String) = Action { implicit request =>
    Ok(views.html.data.uploadPage(proname))
  }

  def samplePage(proname: String) = Action { implicit request =>
    Ok(views.html.data.samplePage(proname))
  }

  def uploadData = Action(parse.multipartFormData(handleFilePartAsFile)) { implicit request =>
    val file = request.body.files
    val sampledata = sampleForm.bindFromRequest.get
    val proname = sampledata.proname
    val sample = sampledata.sample
    val userId = Utils.getUserId
    val pro = projectDao.getProject(userId, proname).toAwait
    val proid = pro.id
    val in1 = file.head.ref.getPath
    val name1 = file.head.filename
    val suffix1 = if (name1.split('.').last == "gz") "_R1.fq.gz" else "_R1.fq"
    val outname1 = sample + suffix1
    val in2 = file(1).ref.getPath
    val name2 = file(1).filename
    val suffix2 = if (name2.split('.').last == "gz") "_R2.fq.gz" else "_R2.fq"
    val outname2 = sample + suffix2
    val id = sampleDao.addSampleReturnId(SampleRow(0, sample, userId, proid, Utils.date, suffix1 + "\t" + suffix2, 0)).toAwait
    val outPath = s"${Utils.dataPath}/$userId/$proid/data/$id/"
    outPath.mkdirs
    try {
      val out1 = outPath + outname1
      val out2 = outPath + outname2
      in1.moveToFile(out1.toFile)
      in2.moveToFile(out2.toFile)
      sampleDao.updateState(id, 1).toAwait
    } catch {
      case e: Exception =>
        sampleDao.updateState(id, 2).toAwait
        (outPath + "/log.txt").writeStringToFile(e.getMessage)
    }finally {
      projectDao.updateCount(proid, pro.samcount + 1)
    }
    Ok(Json.obj("valid" -> "true"))
  }

  def checkSampleName(proname: String) = Action { implicit request =>
    val data = sampleNameForm.bindFromRequest.get
    val sample = data.sample
    val proid = projectDao.getIdByProjectname(Utils.getUserid, proname).toAwait
    val valid = sampleDao.checkName(proid, sample).toAwait
    Ok(Json.obj("valid" -> (!valid).toString))
  }

  def getAllSample(proname: String) = Action.async { implicit request =>
    val userid = Utils.getUserid
    projectDao.getIdByProjectname(userid, proname).flatMap { x =>
      sampleDao.getByUseridAndProid(userid, x).map { y =>
        val json = y.map { z =>
          Json.obj("id" -> z.id, "sample" -> z.sample, "uid" -> z.userid, "pid" -> z.proid,
            "date" -> z.createdate, "state" -> z.state)
        }
        Ok(Json.toJson(json))
      }
    }
  }

  def deleteSampleById(id: Int) = Action { implicit request =>
    try {
      val sample = sampleDao.getSampleById(id).toAwait
      sampleDao.deleteSample(id).toAwait
      val count = sampleDao.getByProId(sample.proid).toAwait
      projectDao.updateCount(sample.proid, count.length)
      val filePath = s"${Utils.path}/data/${sample.userid}/${sample.proid}"
      filePath.delete
      Ok(Json.obj("valid" -> "true"))
    } catch {
      case e: Exception => Ok(Json.obj("valid" -> "false", "message" -> e.getMessage))
    }
  }

  def getAllSampleName(proname: String) = Action.async { implicit request =>
    val userid = Utils.getUserid
    projectDao.getIdByProjectname(userid, proname).flatMap { x =>
      sampleDao.getByUseridAndProid(userid, x).map { y =>
        val json = y.map { z =>
          Json.obj("id" -> z.id, "sample" -> z.sample)
        }
        Ok(Json.toJson(y.map(_.sample)))
      }
    }
  }

  def updateSampleName = Action { implicit request =>
    try {
      val data = FormTool.updateSampleNameForm.bindFromRequest.get
      sampleDao.updateSample(data.sampleId.toInt, data.sample).toAwait
      Ok(Json.obj("valid" -> "true"))
    }catch{
      case e:Exception=>
        Ok(Json.obj("valid"->"false","message"-> e.getMessage))
    }
  }


  def downloadSample(id:Int,file:String,tools:String) = Action { implicit request =>
    val userid = Utils.getUserid
    Ok.sendFile(s"${Utils.path}/data/$userid/$tools/$id/$file".toFile).withHeaders(
      //缓存
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> s"attachment; filename=$file",
      CONTENT_TYPE -> "application/x-download"
    )
  }

}
