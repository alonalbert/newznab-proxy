package com.newznabproxy.util

object Logger {
  fun log(format: String?, vararg args: Any?) {
    val log = String.format(format!!, *args)
    System.out.printf("%1\$tD %1\$tT: %2\$s\n", System.currentTimeMillis(), log)
  }

  fun log(e: Throwable, format: String?, vararg args: Any?) {
    val log = String.format(format!!, *args)
    System.out.printf("%1\$tD %1\$tT: %2\$s\n", System.currentTimeMillis(), log)
    e.printStackTrace()
  }
}