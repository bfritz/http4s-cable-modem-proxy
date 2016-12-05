## http4s demo for Dec 2016 Indy Scala meetup

This repo is my take on hello world for [http4s](http://http4s.org/).
It's inspired by [surfer](https://github.com/wathiede/surfer), a tool
written in Go that exposes Motorola SURFboard stats for [Prometheus](http://prometheus.io/)
to scrape.

There are two http4s [services](http://http4s.org/docs/0.15/service.html)...
The [first service](src/main/scala/Modem.scala) serves static
[files](src/main/resources/modem/demo/) scraped from a real modem
to mock it for demo purposes.  The fake modem can be started with:

    sbt "run-main Modem"

Visit [http://localhost:8080/index.htm](http://localhost:8080/index.htm)
to see the fake modem in "action".  Pressing "enter" in the SBT
terminal after the service is started will close it.

The [second service](src/main/scala/Proxy.scala) is a proxy that waits
for incoming requests, scrapes the fake modem to extract status details,
and serves the status to the user as JSON.  It can be started with:

    sbt "run-main Proxy"

Typically, you will want to start it in a second SBT session in a new
terminal window.

The JSON status is exposed at 
[http://localhost:8090/api/status](http://localhost:8090/api/status).
