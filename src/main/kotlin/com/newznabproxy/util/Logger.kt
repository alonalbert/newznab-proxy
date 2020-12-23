@file:Suppress("unused")

package com.newznabproxy.util

object Logger {
  private const val FORMAT = "%1\$tD %1\$tT: %2\$s\n"
  fun log(log: String) {
    System.out.printf(FORMAT, System.currentTimeMillis(), log)
  }

  fun log(format: String, vararg args: Any) {
    log(String.format(format, *args))
  }

  fun log(e: Throwable, log: String) {
    System.err.printf(FORMAT, System.currentTimeMillis(), log)
    e.printStackTrace()
  }

  fun log(e: Throwable, format: String, vararg args: Any?) {
    val log = String.format(format, *args)
    log(e, log)
  }
}