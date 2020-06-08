package controllers

import config.MyConfig
import dao.{MetaDao, ProjectDao, SampleDao}
import javax.inject.{Inject, Singleton}
import models.Tables.MetaRow
import org.apache.commons.io.FileUtils
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.{ExecCommand, Utils}

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MetaController @Inject()(cc: ControllerComponents,
                               sampleDao: SampleDao,
                               projectDao: ProjectDao,
                               metaDao: MetaDao)
                              (implicit exec: ExecutionContext) extends AbstractController(cc) with MyConfig {


  def metaPage(proname: String) = Action { implicit request =>
    Ok(views.html.meta.metaPage(proname))
  }

  def resultPage(proname: String) = Action { implicit request =>
    Ok(views.html.meta.resultPage(proname))
  }

  def runMeta = Action { implicit request =>
    val form = FormTool.metaForm.bindFromRequest.get
    val sample = form.sample
    val name = form.name
    val proname = form.proname
    val userid = Utils.getUserid
    val proid = projectDao.getIdByProjectname(userid, proname).toAwait
    val id = metaDao.addMetaAndReturnId(MetaRow(0, name, userid, proid, Utils.date, 0)).toAwait
    val path = s"${Utils.path}/data/$userid/$proid/meta/$id"
    path.mkdirs
    Future {
      var state = 0
      var log = ""
      try {
        val samples = sampleDao.getIdByPosition(userid, proid, sample.map(_.trim)).toAwait
        val raw = samples.map { x =>
          val samplePath = s"${Utils.path}/data/$userid/$proid/data/${x.id}"
          val suffix = x.file.split("\t")
          s"${x.sample}\t${samplePath}/${x.sample}${suffix.head}\t${samplePath}/${x.sample}${suffix.last}"
        }.toBuffer
        val rawPath = s"$path/raw.fq.lst"
        FileUtils.writeLines(rawPath.toFile, raw.asJava)

        val exec = new ExecCommand
        val cmdChmod = s"chmod 777 $path"
        val command =
          s"""
            |cd $path
            | perl /mnt/sdb/metagenome/bin/Meta_genome_binning.pl $rawPath
            |""".stripMargin
        FileUtils.writeStringToFile(s"$path/getMetaGenome.sh".toFile,command)
        val runCommand = s"su - ncu -s /bin/bash $path/getMetaGenome.sh"
        val dos2unixGetMetaGenome = s"dos2unix $path/getMetaGenome.sh"
        exec.exect(Array(cmdChmod,dos2unixGetMetaGenome,runCommand), path)
        if (exec.isSuccess) {
          val shell = s"$path/shell"
          val sh = s"$shell".toFile.listFiles().map(_.getName)

          val S01_1 = sh.filter(_.startsWith("S01.1")).map(x => s"sh $shell/$x &").grouped(4).map{x=>
            x.mkString("\n")
          }.mkString("\nwait\n")
          val S01_2 = s"sh $shell/S01.2.stat.sh &"
          val s02_1 = sh.filter(_.startsWith("S02.1")).map(x => s"sh $shell/$x &").grouped(4).map{x=>
            x.mkString("\n")
          }.mkString("\nwait\n")
          val S02_2 = s"sh $shell/S02.2.stat.sh &"
          val S03 = sh.filter(_.startsWith("S03")).map(x => s"sh $shell/$x &").mkString("\n")
          val S04 = s"sh $shell/S04.final.sh &"
          val pidFile = s"$path/shell/pid.txt"
          val com =
            s"""
              |cd $shell
              |echo $$$$ > $pidFile
               |$S01_1
               |wait
               |$S01_2
               |$s02_1
               |wait
               |$S02_2
               |$S03
               |wait
               |$S04
               |wait
               |cd $path/result
                |rm -rf ./css ./js ./res ./meta_genome_binning.html ./binning
                |cd $path
               |tar -czvf $path/meta_results.tar.gz ./result
               |""".stripMargin

          val run = s"$shell/run.sh"
          FileUtils.writeStringToFile(run.toFile, com)
          val dos2unix = s"dos2unix $run"
          val com2 = s"su - ncu -s /bin/bash $run"
          val exec2 = new ExecCommand
          exec2.exect(Array(dos2unix, com2), shell)
          if (exec2.isSuccess) {
            state = 1
            log = exec2.getOutStr
          } else {
            state = 2
            log = exec2.getErrStr
          }
        } else {
          state = 2
          log = exec.getErrStr
        }
      } catch {
        case e: Exception =>
          state = 2
          log = e.getMessage
      } finally {
        metaDao.updateState(id, state).toAwait
        s"$path/shell/pid.txt".delete
        s"$path".toFile.listFiles().filter(x=> x.getName != "log.txt" && x.getName != "meta_results.tar.gz").foreach{xx=>
          xx.getAbsolutePath.delete
        }
        (path + "/log.txt").writeStringToFile(log)
      }
    }
    Ok(Json.toJson("success"))
  }

  def getAllMeta(proname: String) = Action.async { implicit request =>
    val userid = Utils.getUserid
    val proid = projectDao.getIdByProjectname(userid, proname).toAwait
    metaDao.getMeta(userid, proid).map { x =>
      val json = x.map { y =>
        Json.obj("id" -> y.id, "name" -> y.name, "date" -> y.createdate, "state" -> y.state)
      }
      Ok(Json.toJson(json))
    }
  }

  def deleteMeta(id: Int) = Action.async { implicit request =>
    metaDao.getById(id).flatMap { x =>
      metaDao.deleteById(id).map { y =>
        val path = s"${Utils.path}/data/${x.userid}/${x.proid}/meta/$id"
        val pidFile = s"$path/shell/pid.txt"
        if(pidFile.toFile.exists()){
          val exec = new ExecCommand
          val pid = pidFile.toFile.readLines.head.trim
          val killPid = s"sh ${Utils.toolPath}/killPid.sh $pid"
          exec.exec(killPid)
        }
        path.delete
        Ok(Json.toJson("success"))
      }
    }
  }

  def openLog(id: Int) = Action { implicit request =>
    val row = metaDao.getById(id).toAwait
    val log = s"${Utils.dataPath}/${row.userid}/${row.proid}/meta/$id/log.txt".readLines
    val html = "<p>" + log.mkString("</p><p>") + "</p>"
    Ok(Json.toJson(html))
  }

  def checkMetaName(proname: String) = Action { implicit request =>
    val data = FormTool.metaNameForm.bindFromRequest.get
    val name = data.name
    val proid = projectDao.getIdByProjectname(Utils.getUserid, proname).toAwait
    val valid = metaDao.checkName(proid, name).toAwait
    Ok(Json.obj("valid" -> (!valid).toString))
  }

  def updateMetaName = Action { implicit request =>
    try {
      val data = FormTool.updateMetaNameForm.bindFromRequest.get
      metaDao.updateName(data.metaId.toInt, data.name).toAwait
      Ok(Json.obj("valid" -> "true"))
    }catch{
      case e:Exception=>
        Ok(Json.obj("valid"->"false","message"->e.getMessage))
    }

  }


}
