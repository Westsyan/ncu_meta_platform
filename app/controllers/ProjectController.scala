package controllers

import config.MyConfig
import dao.ProjectDao
import javax.inject._
import models.Tables.ProjectRow
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import utils.Utils

import scala.concurrent.ExecutionContext

@Singleton
class ProjectController @Inject()(cc: ControllerComponents, projectDao: ProjectDao)
                                 (implicit exec: ExecutionContext)
  extends AbstractController(cc) with MyConfig {


  def projectPage = Action { implicit request =>
    Ok(views.html.project.projectPage())
  }



  def addProject: Action[AnyContent] = Action { implicit request =>
    val data = FormTool.projectForm.bindFromRequest.get
    val name = data.projectname
    val description = data.description
    val userid = Utils.getUserid
    val project = ProjectRow(0, userid, name, description, Utils.date, 0)
    projectDao.addProject(project).toAwait
    val proId = projectDao.getIdByProjectname(userid, name).toAwait
    val path = s"${Utils.dataPath}/$userid/$proId"
    s"$path/data".mkdir
    s"$path/meta".mkdir
    Ok(Json.obj("valid" -> "true"))
  }

  def checkProjectname = Action.async { implicit request =>
    val data = FormTool.projectnameForm.bindFromRequest.get
    val projectname = data.projectname
    val id = Utils.getUserid
    projectDao.getProjectname(id, projectname).map { x =>
      val valid = if (x.isEmpty) {
        "true"
      } else {
        "false"
      }
      val message = "项目已存在！"
      val json = Json.obj("valid" -> valid, "message" -> message)
      Ok(Json.toJson(json))
    }
  }

  def checkNewproname = Action.async { implicit request =>
    val data = FormTool.newproForm.bindFromRequest.get
    val projectname = data.proname
    val id = request.session.get("id").head.toInt
    projectDao.getProjectname(id, projectname).map { x =>
      val valid = if (x.isEmpty) {
        "true"
      } else {
        "false"
      }
      val message = "项目已存在！"
      val json = Json.obj("valid" -> valid, "message" -> message)
      Ok(Json.toJson(json))
    }
  }

  def deleteProject(id: Int): Action[AnyContent] = Action { implicit request =>
    val userId = Utils.getUserid
    projectDao.deleteByProname(id).toAwait
   //   Await.result(sampledao.deleteByProId(userId, id), Duration.Inf)
    s"${Utils.dataPath}/userId/$id".delete
    Ok(Json.obj("valid" -> "true"))
  }

  def updateProinfo = Action.async { implicit request =>
    val data = FormTool.updatePronameForm.bindFromRequest.get
    val id = data.proId
    val proname = data.projectname
    val des = data.description
    projectDao.getById(id).flatMap{p=>
      val row = ProjectRow(p.id,p.userid,proname,des,p.createdate,p.samcount)
      projectDao.updateProject(p.id,row).map{x=>
        Ok(Json.obj("valid" -> "true"))
      }
    }
  }



  def getAllPorject = Action.async{implicit request=>
    val id = Utils.getUserid
    projectDao.getAll(id).map{x=>
      val json = x.map{y=>
        Json.obj("id" -> y.id,"name" -> y.name,"sample" ->y.samcount,
          "createdate" -> y.createdate,"description" -> y.description)
      }
      Ok(Json.toJson(json))
    }

  }




}
