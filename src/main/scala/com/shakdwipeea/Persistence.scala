package com.shakdwipeea

import akka.actor.{ Actor, ActorLogging }
import PageActor._
import com.softwaremill.tagging._
import com.softwaremill.macwire.akkasupport._
import java.io.{ BufferedWriter, File, FileWriter }
import java.util.Base64
import java.nio.charset.StandardCharsets
import org.apache.commons.text.StringEscapeUtils

class PersistenceActor extends Actor with ActorLogging {
  def writeData(pd: PageData) = {
    println(s"writing data for subreddit ${pd.sub} and title ${pd.linkData.title}")
    val dirName = s"data/${pd.sub}"
    new File(dirName).mkdirs

    val fileName = StringEscapeUtils.escapeEcmaScript(pd.linkData.title.replace("/", "-"))
    val file = new File(s"$dirName/$fileName.txt")

    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(pd.contentHtml)
    bw.close
  }

  def receive = {
    case pd @ PageData(_, linkData, _) => writeData(pd)
    case a => println(s"uknow $a")
  }
}
