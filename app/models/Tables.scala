package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._
  import org.joda.time.DateTime
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Meta.schema ++ Project.schema ++ Sample.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Meta
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param userid Database column userid SqlType(INT)
   *  @param proid Database column proid SqlType(INT)
   *  @param createdate Database column createdate SqlType(VARCHAR), Length(255,true)
   *  @param state Database column state SqlType(INT) */
  case class MetaRow(id: Int, name: String, userid: Int, proid: Int, createdate: String, state: Int)
  /** GetResult implicit for fetching MetaRow objects using plain SQL queries */
  implicit def GetResultMetaRow(implicit e0: GR[Int], e1: GR[String]): GR[MetaRow] = GR{
    prs => import prs._
    MetaRow.tupled((<<[Int], <<[String], <<[Int], <<[Int], <<[String], <<[Int]))
  }
  /** Table description of table meta. Objects of this class serve as prototypes for rows in queries. */
  class Meta(_tableTag: Tag) extends profile.api.Table[MetaRow](_tableTag, Some("ncu_meta_platform"), "meta") {
    def * = (id, name, userid, proid, createdate, state) <> (MetaRow.tupled, MetaRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(userid), Rep.Some(proid), Rep.Some(createdate), Rep.Some(state))).shaped.<>({r=>import r._; _1.map(_=> MetaRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column userid SqlType(INT) */
    val userid: Rep[Int] = column[Int]("userid")
    /** Database column proid SqlType(INT) */
    val proid: Rep[Int] = column[Int]("proid")
    /** Database column createdate SqlType(VARCHAR), Length(255,true) */
    val createdate: Rep[String] = column[String]("createdate", O.Length(255,varying=true))
    /** Database column state SqlType(INT) */
    val state: Rep[Int] = column[Int]("state")

    /** Primary key of Meta (database name meta_PK) */
    val pk = primaryKey("meta_PK", (id, userid))
  }
  /** Collection-like TableQuery object for table Meta */
  lazy val Meta = new TableQuery(tag => new Meta(tag))

  /** Entity class storing rows of table Project
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param userid Database column userid SqlType(INT)
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param description Database column description SqlType(VARCHAR), Length(255,true)
   *  @param createdate Database column createdate SqlType(VARCHAR), Length(255,true)
   *  @param samcount Database column samcount SqlType(INT) */
  case class ProjectRow(id: Int, userid: Int, name: String, description: String, createdate: String, samcount: Int)
  /** GetResult implicit for fetching ProjectRow objects using plain SQL queries */
  implicit def GetResultProjectRow(implicit e0: GR[Int], e1: GR[String]): GR[ProjectRow] = GR{
    prs => import prs._
    ProjectRow.tupled((<<[Int], <<[Int], <<[String], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table project. Objects of this class serve as prototypes for rows in queries. */
  class Project(_tableTag: Tag) extends profile.api.Table[ProjectRow](_tableTag, Some("ncu_meta_platform"), "project") {
    def * = (id, userid, name, description, createdate, samcount) <> (ProjectRow.tupled, ProjectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userid), Rep.Some(name), Rep.Some(description), Rep.Some(createdate), Rep.Some(samcount))).shaped.<>({r=>import r._; _1.map(_=> ProjectRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column userid SqlType(INT) */
    val userid: Rep[Int] = column[Int]("userid")
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column description SqlType(VARCHAR), Length(255,true) */
    val description: Rep[String] = column[String]("description", O.Length(255,varying=true))
    /** Database column createdate SqlType(VARCHAR), Length(255,true) */
    val createdate: Rep[String] = column[String]("createdate", O.Length(255,varying=true))
    /** Database column samcount SqlType(INT) */
    val samcount: Rep[Int] = column[Int]("samcount")

    /** Primary key of Project (database name project_PK) */
    val pk = primaryKey("project_PK", (id, userid))
  }
  /** Collection-like TableQuery object for table Project */
  lazy val Project = new TableQuery(tag => new Project(tag))

  /** Entity class storing rows of table Sample
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param sample Database column sample SqlType(VARCHAR), Length(255,true)
   *  @param userid Database column userid SqlType(INT)
   *  @param proid Database column proid SqlType(INT)
   *  @param createdate Database column createdate SqlType(VARCHAR), Length(255,true)
   *  @param file Database column file SqlType(VARCHAR), Length(255,true)
   *  @param state Database column state SqlType(INT) */
  case class SampleRow(id: Int, sample: String, userid: Int, proid: Int, createdate: String, file: String, state: Int)
  /** GetResult implicit for fetching SampleRow objects using plain SQL queries */
  implicit def GetResultSampleRow(implicit e0: GR[Int], e1: GR[String]): GR[SampleRow] = GR{
    prs => import prs._
    SampleRow.tupled((<<[Int], <<[String], <<[Int], <<[Int], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table sample. Objects of this class serve as prototypes for rows in queries. */
  class Sample(_tableTag: Tag) extends profile.api.Table[SampleRow](_tableTag, Some("ncu_meta_platform"), "sample") {
    def * = (id, sample, userid, proid, createdate, file, state) <> (SampleRow.tupled, SampleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(sample), Rep.Some(userid), Rep.Some(proid), Rep.Some(createdate), Rep.Some(file), Rep.Some(state))).shaped.<>({r=>import r._; _1.map(_=> SampleRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column sample SqlType(VARCHAR), Length(255,true) */
    val sample: Rep[String] = column[String]("sample", O.Length(255,varying=true))
    /** Database column userid SqlType(INT) */
    val userid: Rep[Int] = column[Int]("userid")
    /** Database column proid SqlType(INT) */
    val proid: Rep[Int] = column[Int]("proid")
    /** Database column createdate SqlType(VARCHAR), Length(255,true) */
    val createdate: Rep[String] = column[String]("createdate", O.Length(255,varying=true))
    /** Database column file SqlType(VARCHAR), Length(255,true) */
    val file: Rep[String] = column[String]("file", O.Length(255,varying=true))
    /** Database column state SqlType(INT) */
    val state: Rep[Int] = column[Int]("state")

    /** Primary key of Sample (database name sample_PK) */
    val pk = primaryKey("sample_PK", (id, userid))
  }
  /** Collection-like TableQuery object for table Sample */
  lazy val Sample = new TableQuery(tag => new Sample(tag))
}
