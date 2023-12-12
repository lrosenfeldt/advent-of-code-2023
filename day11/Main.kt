package Day11

import java.io.File

fun List<String>.transpose(): List<String> {
  return (0 until this.first().length).map { x ->
    this.map { line -> line.elementAt(x) }.joinToString("")
  }
}

fun expand(lines: List<String>): List<String> {
  val expandedRows =
      lines.foldIndexed(setOf<Int>()) { y, rs, row -> if (row.all { it == '.' }) rs + y else rs }
  val expandedColumns =
      lines.transpose().foldIndexed(setOf<Int>()) { x, rs, col ->
        if (col.all { it == '.' }) rs + x else rs
      }

  return lines
      .foldIndexed(listOf<String>()) { y, ls, line ->
        when (y) {
          in expandedRows -> (ls + line) + line
          else -> ls + line
        }
      }
      .map { line ->
        line
            .foldIndexed(StringBuilder()) { x, builder, char ->
              when (x) {
                in expandedColumns -> builder.append(char, char)
                else -> builder.append(char)
              }
            }
            .toString()
      }
}

fun List<String>.concat(lines: List<String>): List<String> {
  val concatted = this.toMutableList()
  concatted.addAll(lines)
  return concatted
}

fun expandAndParseLarge(lines: List<String>, n: Long): List<Pair<Long, Long>> {
  val expandedRows =
      lines.foldIndexed(setOf<Int>()) { y, rs, row -> if (row.all { it == '.' }) rs + y else rs }
  val expandedColumns =
      lines.transpose().foldIndexed(setOf<Int>()) { x, rs, col ->
        if (col.all { it == '.' }) rs + x else rs
      }

  val universe = mutableListOf<Pair<Long, Long>>()
  var offsetY: Long = 0
  var offsetX: Long = 0

  for (y in 0 until lines.size) {
    if (y in expandedRows) {
      offsetY += n
    } else {
      offsetY += 1L
    }

    for (x in 0 until lines.first().length) {
      if (x in expandedColumns) {
        offsetX += n
      } else {
        offsetX += 1L
      }
      
      if (lines.elementAt(y).elementAt(x) == '#') {
        universe.add(Pair(offsetX, offsetY))           
      }
    }
    offsetX = 0
  }

  return universe
}

fun List<String>.parse(): List<Pair<Int, Int>> {
  return this.mapIndexed { y, line ->
        line.foldIndexed(listOf<Pair<Int, Int>>()) { x, ls, char ->
          when (char) {
            '#' -> ls + Pair(x, y)
            else -> ls
          }
        }
      }
      .flatten()
}

fun <T> List<T>.pairs(): List<Pair<T, T>> {
  return this.mapIndexed { i, el -> this.drop(i + 1).map { el to it } }.flatten()
}

fun abs(n: Int): Int {
  return if (n >= 0) n else -n
}
fun abs(n: Long): Long {
  return if (n >= 0L) n else -n
}

fun distance(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int {
  return abs(p1.first - p2.first) + abs(p1.second - p2.second)
}
fun distance(p1: Pair<Long, Long>, p2: Pair<Long, Long>): Long {
  return abs(p1.first - p2.first) + abs(p1.second - p2.second)
}

fun part1(lines: List<String>) {
  val result = expand(lines).parse().pairs().map { (p1, p2) -> distance(p1, p2) }
  println(result.sum())
}

fun part2(lines: List<String>) {
  val result = expandAndParseLarge(lines, 1000000).pairs().map { (p1, p2) -> distance(p1, p2) }
  println(result.sum())
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day11 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines()
  part2(lines)
}
