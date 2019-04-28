//#full-examplep
package com.shakdwipeea

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext

//#main-class
object AkkaQuickstart extends App with ActorModule {
  import RedditDispatch._

  // Create the 'helloAkka' actor system
  implicit val system: ActorSystem = ActorSystem("helloAkka")
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val mat: ActorMaterializer = ActorMaterializer()

  reddit ! FetchReddit("clojure")
  //#main-send-messages
}
//#main-class
//#full-example
