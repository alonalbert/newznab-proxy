package com.newznabproxy.tvdb

data class SeriesResult(val status: String, val data: Data) {
  data class Data(val name: String)

}
