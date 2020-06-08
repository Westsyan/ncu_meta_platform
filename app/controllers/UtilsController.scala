package controllers

import config.MyConfig
import dao.{MetaDao, SampleDao}
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.Utils

import scala.concurrent.ExecutionContext

@Singleton
class UtilsController @Inject()(cc: ControllerComponents,
                                sampleDao:SampleDao,
                                metaDao:MetaDao)(implicit exec: ExecutionContext)
  extends AbstractController(cc) with MyConfig{

  def downloadData(id:Int,code:Int) = Action { implicit request =>
    val x = sampleDao.getSampleById(id).toAwait
    val file = x.file.split("\t")(code)
    val flist = s"${Utils.path}/data/${x.userid}/${x.proid}/data/$id".toFile.listFiles()
    Ok.sendFile(flist.find(_.getName.endsWith(file)).get).withHeaders(
      //缓存
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> s"attachment; filename=${x.sample}$file",
      CONTENT_TYPE -> "application/x-download"
    )
  }

  def downloadMeta(id:Int) = Action { implicit request =>
    val x = metaDao.getById(id).toAwait
    Ok.sendFile(s"${Utils.path}/data/${x.userid}/${x.proid}/meta/$id/meta_results.tar.gz".toFile).withHeaders(
      //缓存
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> s"attachment; filename=${x.name}_meta_results.tar.gz",
      CONTENT_TYPE -> "application/x-download"
    )
  }

}

