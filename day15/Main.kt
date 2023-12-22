package Day15

import java.io.File

fun String.hash15(): Int = this.fold(0) { sum, char -> ((sum + char.toInt()) * 17) % 256 }

fun part1(lines: List<String>) {
  println(lines.map { it.hash15() })
  println(lines.map { it.hash15() }.sum())
}

fun List<String>.second(): String = this.elementAt(1)

typealias Lens = Pair<String, Int>

class Hashmap15<T>() {
  private val array: Array<List<Pair<String, T>>> = Array(256, { listOf() })

  fun set(key: String, value: T) {
    val box = key.hash15()
    val boxIndex = array[box].indexOfFirst { it.first == key }
    if (boxIndex > -1) {
      array[box] = array[box].mapIndexed { i, el -> if (i == boxIndex) key to value else el }
    } else {
      array[box] = array[box] + (key to value)
    }
  }

  fun delete(key: String) {
    val box = key.hash15()
    val boxIndex = array[box].indexOfFirst { it.first == key }
    if (boxIndex > -1) {
      array[box] = array[box].filterIndexed { i, _ -> i != boxIndex }
    }
  }

  fun toMap(): Map<Int, List<Pair<String, T>>> =
      array.withIndex().associate { (i, entries) -> i to entries }

  override fun toString(): String = toMap().toString()
}

fun List<String>.toHashmap15(): Hashmap15<Int> {
  val map = Hashmap15<Int>()
  for (line in this) {
    when {
      line.contains('=') -> line.split("=").let { map.set(it.first(), it.second().toInt()) }
      line.endsWith('-') -> map.delete(line.substringBefore('-'))
      else -> throw Exception("Bad string $line")
    }
  }
  return map
}

fun part2(lines: List<String>) {
  val map = lines.toHashmap15().toMap()
  val result =
      map
          .mapValues {
            it.value.mapIndexed { i, (_, value) -> (i + 1) * value }.sum() * (it.key + 1)
          }
          .values
          .sum()
  println(result)
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day15 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readText().trimEnd().split(',')
  part2(lines)
}
