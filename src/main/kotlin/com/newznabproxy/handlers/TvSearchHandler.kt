package com.newznabproxy.handlers

import com.newznabproxy.tvdb.TvDbApi
import com.newznabproxy.util.Logger.log
import com.newznabproxy.util.QueryParameters.getParameters
import com.newznabproxy.util.QueryParameters.getQuery
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

internal class TvSearchHandler : HttpHandler {
  private val httpClient = HttpClient.newHttpClient()

  override fun handle(exchange: HttpExchange) {
    try {
      val uri = exchange.requestURI
      log("URI: %s", uri)
      val parameters = getParameters(uri.query)
      val host = uri.path.split("/").toTypedArray()[2]
      val action = parameters["t"]
      if (action != null && action.endsWith("search")) {
        parameters["t"] = "search"
      }
      parameters.remove("rid")
      val tvDbId = parameters.remove("tvdbid")
      if (tvDbId != null) {
        var query = TvDbApi.getSeriesName(tvDbId).replace(' ', '+')
        val season: String? = parameters.remove("season")
        if (season != null) {
          query += "+s$season"
          val episode: String? = parameters.remove("ep")
          if (episode != null) {
            query += "e%02d".format(episode.toInt())
          }
          parameters.put("q", query)
        }
      }
      val categories = parameters["cat"]
      if (categories != null) {
        parameters["cat"] = "1000,2000,3000,4000,6000,7000,8000"
      }
      val newUri = URI(
        "https", uri.userInfo, host, 443,
        "/api", getQuery(parameters), uri.fragment
      )
      log("New uri: %s", newUri)
      val request = HttpRequest.newBuilder(newUri).build()
      val response = httpClient
        .send(request, BodyHandlers.ofString())
      val body = response.body()
      val responseHeaders = exchange.responseHeaders
      responseHeaders.add("Content-Type", "application/xml")
      exchange.sendResponseHeaders(200, body.length.toLong())
      exchange.responseBody.use { out -> out.write(body.toByteArray()) }
    } catch (e: Throwable) {
      // TODO: 12/21/20: Handle error
      e.printStackTrace()
    }
  }
}