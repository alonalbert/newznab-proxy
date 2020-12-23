package com.newznabproxy.handlers.binsearch

enum class SizeUnit(val size: Int) {
  B(1),
  KB(1024),
  GB(1024 * 1024),
  GiB(1024 * 1024),
  ;

  fun getSize(count: Float) : Int = (count * size).toInt()
}
