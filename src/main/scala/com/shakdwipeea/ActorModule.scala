package com.shakdwipeea

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.routing.RoundRobinPool
import scala.concurrent.ExecutionContext
import akka.stream.ActorMaterializer
import com.softwaremill.macwire._
import com.softwaremill.tagging._
import com.softwaremill.macwire.akkasupport._

trait Page
trait Persistence
trait Reddit


trait ActorModule {
  lazy val persistenceProps = wireProps[PersistenceActor]
    .withRouter(RoundRobinPool(4))
  lazy val persistence: ActorRef @@ Persistence =
    system.actorOf(persistenceProps,"persistence").taggedWith[Persistence]

  lazy val pageProps = wireProps[PageActor]
    .withRouter(RoundRobinPool(5))
  lazy val page: ActorRef @@ Page = system.actorOf(pageProps, "page")
    .taggedWith[Page]

  lazy val redditProps = wireProps[RedditDispatch]
    .withRouter(RoundRobinPool(5))
  lazy val reddit: ActorRef @@ Reddit = system.actorOf(redditProps, "reddit")
    .taggedWith[Reddit]

  implicit val system: ActorSystem
  implicit val mat: ActorMaterializer
  implicit val ec: ExecutionContext
}
