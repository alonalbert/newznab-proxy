package com.newznabproxy.handlers

import CAPS
import com.newznabproxy.tvdb.TvDbApi
import com.newznabproxy.util.Logger
import com.newznabproxy.util.QueryParameters
import com.newznabproxy.util.SeriesHelper
import com.newznabproxy.util.SizeUnit
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.Charset
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml

private const val HOST = "binsearch.info"

class BinsearchHandler : HttpHandler {
  companion object {
    private val RE_ITEM_NAME = Regex("^\\[ (.*) ]")
    private val RE_ITEM_SIZE = Regex("size (\\d+) ([a-z]+)", RegexOption.IGNORE_CASE)
  }

  private val httpClient = HttpClient.newHttpClient()

  init {
    println(
      generateRss(
        loadUri(URI("https://binsearch.info/?q=Saturday+Night+Live+.S46E05.John.Mulaney&max=100&adv_age=1100&server=")),
        "https://binsearch.info/?q=survivor+1080p"
      )
    )
  }

  override fun handle(exchange: HttpExchange) {
    try {
      val responseHeaders = exchange.responseHeaders
      responseHeaders.add("Content-Type", "application/xml")

      val uri = exchange.requestURI
      Logger.log("URI: %s", uri)
      val parameters = QueryParameters.getParameters(uri.query)
      val action = parameters["t"]
      if (action == "caps") {
        exchange.sendResponseHeaders(200, CAPS.length.toLong())
        exchange.responseBody.use { out -> out.write(CAPS.toByteArray()) }
        return
      }
      val tvDbId = parameters.remove("tvdbid")
      var query: String
      if (tvDbId != null) {
        query = TvDbApi.getSeriesName(tvDbId)
        query += " " + SeriesHelper.getEpisodeString(parameters)
        parameters.remove("season")
        parameters.remove("ep")
        parameters["q"] = URLEncoder.encode(query, "utf-8")
      } else {
        query = parameters["q"] ?: "foobar"
      }
      val max = parameters["limit"] ?: "100"
      parameters.clear()

      parameters["q"] = query
      parameters["max"] = max
      parameters["postdate"] = "date"

      val newUri = URI(
        "https", uri.userInfo, HOST, 443,
        "/", QueryParameters.getQuery(parameters), uri.fragment
      )
      Logger.log("New uri: %s", newUri)
      val body = loadUri(newUri)
      val rss = generateRss(body, uri.toString())
      Logger.log("%s", rss)
      exchange.sendResponseHeaders(200, 0)
      exchange.responseBody.use { out ->
        val writer = out.bufferedWriter(Charset.defaultCharset())
        writer.write(rss)
        writer.flush()
      }
    } catch (e: Throwable) {
      Logger.log(e, "BinsearchHandler error")
      exchange.sendResponseHeaders(500, 0)
      exchange.responseBody.use { out -> out.write("Error".toByteArray()) }
    }

  }

  private fun loadUri(newUri: URI): String {
    val request = HttpRequest.newBuilder(newUri).build()
    val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
  }

  private fun generateRss(html: String, uri: String): String {
//    if (true) {
//      return GEN
//    }
    val doc = Jsoup.parse(html)
    val items = doc.select("table[id=r2] tr")
    val xml = xml("rss") {
      namespace("atom", "http://www.w3.org/2005/Atom")
      namespace("newznab", "http://www.newznab.com/DTD/2010/feeds/attributes/")
      attribute("version", "2.0")

      element("channel") {
        element("atom")
        attribute("href", uri)
        attribute("rel", "self")
        attribute("type", "application/rss+xml")
        element("title") {
          text("binsearch.info")
        }
        element("description") {
          text("binsearch.info API")
        }
        element("link") {
          text("https://binsearch.info")
        }
        element("newznab:response") {
          attribute("offset", 0)
          attribute("total", items.size - 1)
        }
        items.drop(1).forEach { item ->
          handleItem(item)
        }
      }
    }

    return xml.toString(false)
  }

  private fun Node.handleItem(item: Element) {
    val title = getTitle(item)
    val size = getSize(item)
    val id = getId(item)
    val date = getDate(item)

    if (title == null || size < 0 || id == null || date == null) {
      return
    }

    element("item") {
      element("title") {
        text(title)
      }
      element("guid") {
        attribute("isPermaLink", false)
        text(id)
      }

      element("pubDate") {
        text(date)
      }

      element("enclosure") {
        attribute("url", "?id=$id")
        attribute("type", "application/x-nzb")
      }
    }
  }

  private fun getTitle(item: Element): String? {
    val text = item.select("span[class=s]").text() ?: return null
    return RE_ITEM_NAME.find(text)?.groups?.get(1)?.value ?: text
  }

  private fun getSize(item: Element): Int {
    val container = item.select("span[class=d]") ?: return -1
    val textNodes = container.textNodes()
    if (textNodes.size == 0) {
      return -1
    }
    val match = RE_ITEM_SIZE.find("size 123 GB, parts") ?: return -1
    val size = match.groups[1]!!.value.toInt()
    return SizeUnit.valueOf(match.groups[2]!!.value).getSize(size)
  }

  private fun getId(item: Element): String? {
    return item.select("input[type=checkbox][name]")?.attr("name")
  }

  private fun getDate(item: Element): String? {
    val tds = item.select("td")
    return tds?.last()?.text()
  }
}