package com.newznabproxy

import com.newznabproxy.handlers.binsearch.BinsearchHandler
import com.newznabproxy.handlers.tvsearch.TvSearchHandler
import com.newznabproxy.util.Logger
import com.sun.net.httpserver.HttpServer
import java.io.IOException
import java.net.InetSocketAddress
import java.util.concurrent.Executors

fun main(args: Array<String>) {
  val port = 8123
  val server: HttpServer =
    try {
      HttpServer.create(InetSocketAddress(port), 0)
    } catch (e: IOException) {
      e.printStackTrace()
      return
    }
  server.executor = Executors.newFixedThreadPool(10)
  server.createContext("/tvsearch", TvSearchHandler())
  server.createContext("/binsearch", BinsearchHandler())
  Logger.log("Starting server on port %s", port)
//  if (true) {
//    return
//  }
  server.start()
}
