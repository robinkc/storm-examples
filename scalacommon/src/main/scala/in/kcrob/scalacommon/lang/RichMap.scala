package in.kcrob.scalacommon.lang

/**
  * Created by kcrob.in on 09/11/17.
  */
trait RichMap {
  implicit def stringAnyToStringString(map: Map[String, Any]): Seq[(String, String)] = {
    map.toSeq.map(m => (m._1, m._2.toString))
  }
}