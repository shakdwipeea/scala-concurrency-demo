package com.shakdwipeea
import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.util.ByteString
import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }
import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol._
import spray.json.JsObject
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol._
import com.softwaremill.macwire._
import akka.pattern.pipe
import com.softwaremill.tagging._
import com.softwaremill.macwire.akkasupport._

import RedditDispatch._

object PageActor {
  sealed trait PageMsg
  case class PageRequest(sub: String, linkData: LinkData) extends PageMsg
  case class PageData(sub: String, linkData: LinkData,
    contentHtml: String) extends PageMsg
}

class PageActor (persistence: ActorRef @@ Persistence) (implicit mat: ActorMaterializer, ec: ExecutionContext,
  actorSystem: ActorSystem) extends Actor with ActorLogging {
  import PageActor._

  def request(pageRequest: PageRequest) (implicit mat: ActorMaterializer,
    ec: ExecutionContext, actorSystem: ActorSystem) {
    Http()
      .singleRequest(HttpRequest(uri = "https://www.reddit.com/r/clojure.json"))
      .filter { response => response.status == StatusCodes.OK }
      .flatMap { response =>
        Unmarshal(response)
          .to[String]
          .map(body => PageData(pageRequest.sub, pageRequest.linkData,
            body))
      }
      .pipeTo(self)
  }


  def receive = {
    case pr @ PageRequest(sub, linkData) => request(pr)
    case pd @ PageData(_, _, _) => println(s"page data got for ${pd.linkData.title}")
    case a => println(s"unknown message $a")
  }
}
