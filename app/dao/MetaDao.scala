package dao

import javax.inject.{Inject, Singleton}
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MetaDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                       (implicit exec:ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def addMetaAndReturnId(row:MetaRow) : Future[Int] = {
    db.run(Meta returning Meta.map(_.id) += row)
  }

  def updateState(id:Int,state:Int) : Future[Unit] = {
    db.run(Meta.filter(_.id === id).map(_.state).update(state)).map(_=>())
  }

  def getMeta(userid:Int,proid:Int) : Future[Seq[MetaRow]] = {
    db.run(Meta.filter(_.userid === userid).filter(_.proid === proid).result)
  }

  def getById(id:Int):Future[MetaRow] = {
    db.run(Meta.filter(_.id === id).result.head)
  }

  def deleteById(id:Int) : Future[Unit] = {
    db.run(Meta.filter(_.id === id).delete).map(_=>())
  }

  def getByProid(proid:Int) : Future[Seq[MetaRow]] = {
    db.run(Meta.filter(_.proid === proid).result)
  }

  def checkName(proid: Int, name: String): Future[Boolean] = {
    db.run(Meta.filter(_.proid === proid).filter(_.name === name).exists.result)
  }

  def updateName(id:Int,meta:String) : Future[Unit] = {
    db.run(Meta.filter(_.id === id).map(_.name).update(meta)).map(_=>())
  }


}
