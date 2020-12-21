package com.newznabproxy.tvdb.model

data class LoginResult(val status: String, val data: Data) {
  data class Data(val token: String)
}
