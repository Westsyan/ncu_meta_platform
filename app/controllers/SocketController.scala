package controllers

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}
import akka.stream.Materializer
import config.MyAwait
import dao.{MetaDao, ProjectDao}
import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc.{AbstractController, ControllerComponents, WebSocket}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class SocketController @Inject()(cc: ControllerComponents,
                                 projectDao:ProjectDao,
                                 metaDao: MetaDao
                                )(implicit system: ActorSystem, ec: ExecutionContext, mat: Materializer)
  extends AbstractController(cc) with MyAwait {

  case class TaskData(taskInfo: (Int, Seq[Int]))

  implicit val messageFlowTransformer: MessageFlowTransformer[JsValue, String] =
    MessageFlowTransformer.jsonMessageFlowTransformer[JsValue, String]

  def socket(proname:String): WebSocket = WebSocket.accept[JsValue, String] { implicit request =>
    val userid = request.session.get("id").get.toInt
    val proid = projectDao.getIdByProjectname(userid,proname).toAwait
    ActorFlow.actorRef { out =>
      Props(new Actor {
        override def receive: Receive = {
          case msg: JsValue if (msg \ "info").as[String] == "start" =>
            val taskInfo = getTaskInfo(proid)
            system.scheduler.scheduleOnce(3 seconds, self, TaskData(taskInfo))
          case TaskData(taskInfo) =>
            val taskInfoNow = getTaskInfo(proid)
            if (taskInfo._1 != taskInfoNow._1 || taskInfo._2.diff(taskInfoNow._2).nonEmpty) {
              out ! "update"
            }
            system.scheduler.scheduleOnce(3 seconds, self, TaskData(taskInfoNow))
          case _ =>
            self ! PoisonPill
        }

        override def postStop(): Unit = {
          self ! PoisonPill
        }
      })
    }
  }

  /**
   *
   * @param id 用户ID
   * @return int: 任务数量，seq[Int]：任务状态集合
   */
  def getTaskInfo(id: Int): (Int, Seq[Int]) = {
    val task = metaDao.getByProid(id).toAwait
    (task.length, task.map(_.state))
  }

}
