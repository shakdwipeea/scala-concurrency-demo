package com.shakdwipeea

import akka.actor.{ Actor, ActorLogging, ActorSystem, Props }
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

object RedditDispatch {
  case class RedditLinkEntry(data: LinkData)
  case class RedditData(children: List[RedditLinkEntry])
  case class RedditFormat(data: RedditData)

  case class LinkData(title: String, url: String)

  sealed trait RedditMsg
  case class FetchReddit(sub: String) extends RedditMsg
  case class RedditLinks(sub: String, links: List[LinkData])
      extends RedditMsg
}

object RedditJsonProtocol extends DefaultJsonProtocol {
  import RedditDispatch._

  implicit val linkData = jsonFormat2(LinkData)
  implicit val redditLinkEntry = jsonFormat1(RedditLinkEntry)
  implicit val redditData = jsonFormat1(RedditData)
  implicit val redditFormat = jsonFormat1(RedditFormat)
}

class RedditDispatch (implicit mat: ActorMaterializer,
  ec: ExecutionContext, actorSystem: ActorSystem) extends Actor with ActorLogging {
  import RedditDispatch._
  import RedditJsonProtocol._

  def request(sub: String) (implicit mat: ActorMaterializer,
    ec: ExecutionContext, actorSystem: ActorSystem) {
    Http()
      .singleRequest(HttpRequest(uri = "https://www.reddit.com/r/clojure.json"))
      .filter { response => response.status == StatusCodes.OK }
      .flatMap { response =>
        Unmarshal(response)
          .to[RedditFormat]
          .map(_.data.children.map(_.data))
          .map(data => RedditLinks(sub, data))
      }
      .pipeTo(self)
  }


  def receive = {
    case FetchReddit(sub) => request(sub)

    case RedditLinks(sub, links) =>
      println(s"sub is $sub and links are $links")

    case a => println(s"Unknow msg $a")
  }
}

trait SubredditModule {
  def createRedditDispatch = system.actorOf(Props(wire[RedditDispatch]))

  implicit val system: ActorSystem
  implicit val mat: ActorMaterializer
  implicit val ec: ExecutionContext
}
