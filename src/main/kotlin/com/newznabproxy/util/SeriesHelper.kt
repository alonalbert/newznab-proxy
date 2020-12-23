package com.newznabproxy.util

object SeriesHelper {
  fun getEpisodeString(parameters:  Map<String, String>) : String? {
    val season: String? = parameters["season"]
    if (season != null) {
      val episode: String? = parameters["ep"]
      var result = "s$season"
      if (episode != null) {
        result += "e%02d".format(episode.toInt())
      }
      return result
    }
    return null
  }
}