package dao

import javax.inject.Inject
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class SampleDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                         (implicit exec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def addSampleReturnId(row: SampleRow): Future[Int] = {
    db.run(Sample returning Sample.map(_.id) += row)
  }

  def updateState(id: Int, state: Int): Future[Unit] = {
    db.run(Sample.filter(_.id === id).map(_.state).update(state)).map(_ => ())
  }

  def checkName(proid: Int, name: String): Future[Boolean] = {
    db.run(Sample.filter(_.proid === proid).filter(_.sample === name).exists.result)
  }

  def getByUseridAndProid(userid: Int, proid: Int): Future[Seq[SampleRow]] = {
    db.run(Sample.filter(_.userid === userid).filter(_.proid === proid).result)
  }

  def deleteSample(id:Int) : Future[Unit] ={
    db.run(Sample.filter(_.id === id).delete).map(_=>())
  }

  def getSampleById(id:Int) : Future[SampleRow] = {
    db.run(Sample.filter(_.id === id).result.head)
  }

  def getByProId(proid:Int) : Future[Seq[SampleRow]] = {
    db.run(Sample.filter(_.proid === proid).result)
  }

  def getIdByPosition(userid:Int,proid:Int,sample:Seq[String]) : Future[Seq[SampleRow]] = {
    db.run(Sample.filter(_.userid === userid).filter(_.proid === proid).filter(_.sample.inSetBind(sample)).result)
  }

  def updateSample(id:Int,sample:String) : Future[Unit] = {
    db.run(Sample.filter(_.id === id).map(_.sample).update(sample)).map(_=>())
  }

}
