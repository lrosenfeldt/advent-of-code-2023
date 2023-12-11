package Day05

import java.io.File

fun List<String>.split(separator: String): List<List<String>> {
  val separated = mutableListOf<List<String>>()
  var current = mutableListOf<String>()
  for (str in this) {
    if (str.equals(separator)) {
      separated.add(current)
      current = mutableListOf<String>()
    } else {
      current.add(str)
    }
  }

  if (current.isNotEmpty()) {
    separated.add(current)
  }
  return separated
}

data class Mapping(val source: Long, val dest: Long, val range: Long) {
  fun mapItemOrNull(item: Long): Long? {
    return when (item) {
      in source until source + range -> (item - source) + dest
      else -> null
    }
  }
}

fun List<Mapping>.mapItem(item: Long): Long {
  var mapped: Long?
  for (mapping in this) {
    mapped = mapping.mapItemOrNull(item)
    if (mapped != null) {
      return mapped
    }
  }
  return item
}

fun parseMapping(input: String): Mapping {
  return input.trim().split(' ').map { it.toLong() }.let {
    Mapping(it.elementAt(1), it.first(), it.elementAt(2))
  }
}

fun part1(lines: List<String>): Unit {
  val seeds = lines.first().split(':').elementAt(1).trim().split(' ').map { it.toLong() }
  val mappings = lines.drop(2).split("").map { it.drop(1).map(::parseMapping) }
  val locations = seeds.map { seed -> mappings.fold(seed) { s, mapping -> mapping.mapItem(s) } }
  println(locations.min())
}

fun part2(lines: List<String>): Unit {
  val seeds =
      lines.first().split(':').elementAt(1).trim().split(' ').map { it.toLong() }.chunked(2) {
          (first, second) ->
        first until (first + second)
      }
  val mappings = lines.drop(2).split("").map { it.drop(1).map(::parseMapping) }

  var min: Long = Long.MAX_VALUE
  var localRes: Long
  var range: LongRange
  for (i in 0 until seeds.size) {
    range = seeds[i]
    // System.err.println("$i / ${seeds.size}")
    for (seed in range) {
      localRes = mappings.fold(seed) { item, mapping -> mapping.mapItem(item) }
      if (localRes < min) {
        min = localRes
      }
    }
  }
  println(min)
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day05 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines()

  part2(lines)
}
