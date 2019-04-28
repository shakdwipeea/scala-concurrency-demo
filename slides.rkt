#lang slideshow

(require net/sendurl
         slideshow/code
         slideshow/text)

(define akka-bench-url "https://letitcrash.com/post/20397701710/50-million-messages-per-second-on-a-single")

(slide (titlet "Actors in scala")
       (item "objects which encapsulate state and behaviour")
       'next
       (item "communicate by exchaning message through mailbox")
       'next
       (item "split task up and delegate"))

(slide (titlet "Distributed by design")
       (item "local to remote from config")
       'next
       (item "routing and load balancing strategies present out of the box"))

(slide (titlet "Actors")
       (item "class with a queue")
       'next
       (item "handles one msg at a time")
       'next
       (item "extremely light weight")
       'next
       (clickback (item (tt "50 million msg / sec in 1 machine")) (λ () (send-url akka-bench-url))))

(slide (titlet "Remote Actors")
       'next
       (scale (vl-append 4.0
                         (item (bt "creating remote acros"))
                         (codeblock-pict "akka {
  actor {
    deployment {
      /sampleActor {
        remote = \"akka.tcp://sampleActorSystem@127.0.0.1:2553\"
      }
    }
  }
}")
                         (item (bt "looking up remote actors"))
                         (codeblock-pict "akka.<protocol>://<actor system name>@<hostname>:<port>/<actor path>"))
              0.75))

(slide (titlet "Routers")
       'next
       (item "Specify load balancing for actor")
       (codeblock-pict
        "akka.actor.deployment {
  /myRouter {
    router = round-robin
    nr-of-instances = 5
  }
}"))

;; (item "ActorInitializedException => Stop")
;;             (item "ActorKilledException => Stop")
;;             (item "Exception => Restart")
;;             (item "Throwable => Escalate")
;;             (item "Otherwise => Escalate")

(slide (titlet "Supervisors")
       (item "Strategy Stop / Restart / Escalate ")
       (item "Can be overwritten")
       (item "default supervisor /users: GuardianActor"))

(slide (titlet "Demo"))
             

