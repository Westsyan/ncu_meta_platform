package dao

import javax.inject.{Inject, Singleton}
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProjectDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                            (implicit exec:ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._

  def addProject(project: ProjectRow) : Future[Unit] = {
    db.run(Project += project).map(_=>())
  }

  def getProjectname(accountid:Int,projectname:String) : Future[Seq[ProjectRow]] = {
    db.run(Project.filter(_.userid === accountid).filter(_.name === projectname).result)
  }

  def getAllProject(accountid:Int) : Future[Seq[String]] ={
    db.run(Project.filter(_.userid === accountid).map(_.name).result)
  }

  def getAll(accountid : Int) : Future[Seq[ProjectRow]] = {
    db.run(Project.filter(_.userid === accountid).result)
  }

  def getIdByProjectname(accountid:Int,projectname:String) : Future[Int] ={
    db.run(Project.filter(_.userid === accountid).filter(_.name === projectname).map(_.id).result.head)
  }

  def updateProject(id:Int,row:ProjectRow) : Future[Unit] ={
    db.run(Project.filter(_.id === id).update(row)).map(_=>())
  }

  def updateCount(id:Int,samcount:Int) : Future[Unit] = {
    db.run(Project.filter(_.id === id).map(_.samcount).update(samcount).map(_ => ()))
  }

  def getProject(accountid:Int,projectname:String) : Future[ProjectRow] = {
    db.run(Project.filter(_.userid === accountid).filter(_.name === projectname).result.head)
  }

  def deleteByProname(proId:Int) :Future[Unit] ={
    db.run(Project.filter(_.id === proId).delete).map(_=>())
  }

  def deleteByUserid(id:Int) : Future[Unit] = {
    db.run(Project.filter(_.userid === id).delete).map(_ => ())
  }

  def updatePronameById(id:Int,proname:String) : Future[Unit] = {
    db.run(Project.filter(_.id === id).map(_.name).update(proname)).map(_=>())
  }

  def updateDescriptionById(id:Int,description:String) : Future[Unit] = {
    db.run(Project.filter(_.id === id).map(_.description).update(description)).map(_=>())
  }

  def getById(id:Int) : Future[ProjectRow] = {
    db.run(Project.filter(_.id === id).result.head)
  }
}
