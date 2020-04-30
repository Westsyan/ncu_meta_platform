package utils

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import play.api.mvc.{AnyContent, MultipartFormData, Request}

/**
  * Created by yz on 2017/6/16.
  */
object Utils {

  def random :String = {
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
    var value = ""
    for (i <- 0 to 20){
      val ran = Math.random()*62
      val char = source.charAt(ran.toInt)
      value += char
    }
    value
  }

  def getTime(startTime: Long) = {
    val endTime = System.currentTimeMillis()
    (endTime - startTime) / 1000.0
  }

  def getUserid(implicit request: Request[AnyContent]): Int = {
     request.session.get("id").get.toInt
  }


  def getUserId(implicit request: Request[MultipartFormData[File]]): Int = {
      request.session.get("id").get.toInt
  }

  def getRequestFile(name: String)(implicit request: Request[MultipartFormData[File]]): File = {
    request.body.file(name).get.ref
  }

  def isDouble(value: String): Boolean = {
    try {
      value.toDouble
    } catch {
      case _: Exception =>
        return false
    }
    true
  }

  def date : String = {
    val now = new Date()
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)
    date
  }

  val isWindow: Boolean = {
    System.getProperties.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1
  }

  val windowsPath = "I:/ncu_platform/meta_platform"
  val linuxPath = "/mnt/sdb/platform/resources/meta_platform"
  val path = {
    if (isWindow) windowsPath else linuxPath
  }

  val dataPath = path + "/data"

  val toolPath = path+"/tools"

  val tmpPath =path +"/tmp"

}
