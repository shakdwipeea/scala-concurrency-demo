package com.shakdwipeea

import akka.actor.{ Actor, ActorLogging }
import PageActor._
import com.softwaremill.tagging._
import com.softwaremill.macwire.akkasupport._

class PersistenceActor extends Actor with ActorLogging {
  def receive = {
    case pd @ PageData(_, linkData, _) => println("save ${linkData.title}")
    case a => println(s"uknow $a")
  }
}
