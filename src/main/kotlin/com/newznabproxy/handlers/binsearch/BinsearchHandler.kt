package com.newznabproxy.handlers.binsearch

import CAPS
import com.newznabproxy.tvdb.TvDbApi
import com.newznabproxy.util.Logger
import com.newznabproxy.util.QueryParameters
import com.newznabproxy.util.SeriesHelper
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import java.io.PrintStream
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.text.RegexOption.IGNORE_CASE
import kotlin.text.RegexOption.MULTILINE
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml

private const val HOST = "binsearch.info"
private const val GET_NZB_URL = "https://$HOST/fcgi/nzb.fcgi"
private const val GET_NZB_DATA = "%s=on&action=nzb"

class BinsearchHandler : HttpHandler {
  companion object {
    private val NFO_RE_OPTIONS = setOf(IGNORE_CASE, MULTILINE)

    private val RE_COMPLETE_NAME = Regex("^Complete name +: (.*)$", NFO_RE_OPTIONS)
    private val RE_FILE_SIZE = Regex("^File size +: ([0-9.]+) ([a-z]+)$", NFO_RE_OPTIONS)
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
      exchange.sendResponseHeaders(200, 0)
      PrintStream(exchange.responseBody, true).use { out ->
        val response = generateResponse(exchange.requestURI)
        Logger.log(response)
        out.println(response)
      }
    } catch (e: Throwable) {
      Logger.log(e, "BinsearchHandler error")
      exchange.sendResponseHeaders(500, 0)
      PrintStream(exchange.responseBody, true).use { out ->
        out.println("Internal error:")
        e.printStackTrace(out)
      }
    }
  }

  fun generateResponse(uri: URI): String {
    Logger.log("URI: %s", uri)
    val parameters = QueryParameters.getParameters(uri.query)
    val action = parameters["t"]

    return when (action) {
      "caps" -> CAPS
      "getnzb" -> loadNzb(parameters["id"]!!)
      else -> loadRss(parameters, uri)
    }
  }

  private fun loadRss(parameters: MutableMap<String, String>, uri: URI): String {
    val tvDbId = parameters.remove("tvdbid")
    var query: String?

    if (tvDbId != null) {
      query = URLEncoder.encode(TvDbApi.getSeriesName(tvDbId), "utf-8")
    } else {
      // Binsearch mush have some query - Sonarr sends no query when testing
      query = parameters["q"]
    }
    if (query == null) {
      // Sonarr sends a test URI with no query
      return generateRss(
        listOf(ItemInfo("fake-id", "Fake Title", 100, "1-Jan-2000")),
        uri.toString()
      )
    }
    val episodeString = SeriesHelper.getEpisodeString(parameters)
    if (episodeString != null) {
      query += "+$episodeString"
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
    return generateRss(loadUri(newUri), uri.toString())
  }

  private fun loadNzb(id: String): String {
    return httpClient.send(
      HttpRequest.newBuilder(URI.create(GET_NZB_URL))
        .POST(HttpRequest.BodyPublishers.ofString(GET_NZB_DATA.format(id)))
        .build(),
      HttpResponse.BodyHandlers.ofString()
    ).body()
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

    return runBlocking {
      val items = parseItems(html)
      generateRss(items, uri)
    }
  }

  private fun generateRss(items: List<ItemInfo>, uri: String): String {
    return xml("rss") {
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
          attribute("total", items.size)
        }
        items.forEach {
          addNode(getItemElement(it))
        }
      }
    }.toString(false)
  }

  private fun getItemElement(info: ItemInfo): Node {
    return xml("root") {
      element("item") {
        element("title") {
          text(info.title)
        }
        element("guid") {
          attribute("isPermaLink", false)
          text(info.id)
        }

        element("pubDate") {
          text(info.date)
        }

        element("enclosure") {
          attribute("url", "?t=getnzb&id=${info.id}")
          attribute("length", info.size)
          attribute("type", "application/x-nzb")
        }
      }
    }.children[0] as Node
  }

  private fun getId(item: Element): String? {
    return item.select("input[type=checkbox][name]")?.attr("name")
  }

  private fun getDate(item: Element): String? {
    val tds = item.select("td")
    return tds?.last()?.text()
  }

  private suspend fun parseItems(html: String): List<ItemInfo> {
    val doc = Jsoup.parse(html)
    val items = doc.select("table[id=r2] tr")
    return items.drop(1).map { item ->
      GlobalScope.async {
        loadItemInfo(item)
      }
    }.awaitAll()
      .filterNotNull()
  }

  private fun loadItemInfo(item: Element): ItemInfo? {
    if (item.select("a:contains(collection)") == null) {
      return null
    }
    val id = getId(item) ?: return null
    val date = getDate(item) ?: return null

    val nfoLink = item.select("a[class=submodal]")?.attr("href") ?: return null
    val nfo = Jsoup.parse(loadUri(URI("https://$HOST$nfoLink"))).body().text()

    val title = (RE_COMPLETE_NAME.find(nfo) ?: return null).groups[1]!!.value

    val sizeGroups = (RE_FILE_SIZE.find(nfo) ?: return null).groups
    val size = SizeUnit.valueOf(sizeGroups[2]!!.value).getSize(sizeGroups[1]!!.value.toFloat())

    return ItemInfo(id, title, size, date)
  }
}