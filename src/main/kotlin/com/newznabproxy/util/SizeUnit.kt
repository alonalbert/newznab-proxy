package com.newznabproxy.util

enum class SizeUnit(val size: Int) {
  B(1),
  KB(1024),
  GB(1024 * 1024),
  ;

  fun getSize(count: Int) = count * size
}
