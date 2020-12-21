package com.newznabproxy.util

import java.util.AbstractMap.SimpleImmutableEntry
import java.util.Arrays
import java.util.stream.Collectors

internal object QueryParameters {
  fun getParameters(query: String?): MutableMap<String, String> {
    return if (query == null) {
      mutableMapOf()
    } else Arrays.stream(query.split("&").toTypedArray())
      .map { param: String ->
        val split = param.split("=").toTypedArray()
        SimpleImmutableEntry(split[0], split[1])
      }
      .collect(
        Collectors.toMap(
          { obj: SimpleImmutableEntry<String, String> -> obj.key },
          { obj: SimpleImmutableEntry<String, String> -> obj.value })
      )
  }

  fun getQuery(map: Map<String, String>): String {
    return map.keys.stream()
      .map { key: String -> key + "=" + map[key] }
      .collect(Collectors.joining("&"))
  }
}