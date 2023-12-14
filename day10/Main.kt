package Day10

import java.io.File

enum class Pipe {
  PIPE,
  DASH,
  L,
  J,
  SEVEN,
  F,
  DOT
}

fun Char.toPipe(): Pipe {
  return when (this) {
    '|' -> Pipe.PIPE
    '-' -> Pipe.DASH
    'L' -> Pipe.L
    'J' -> Pipe.J
    '7' -> Pipe.SEVEN
    'F' -> Pipe.F
    '.' -> Pipe.DOT
    else -> throw Exception("Invalid pipe $this")
  }
}

typealias Tiles = Array<Array<Pipe>>

fun Tiles.display(): String = this.map { line -> line.joinToString(", ") }.joinToString("\n")

fun parseTiles(lines: List<String>): Pair<Pair<Int, Int>, Tiles> {
  val tiles = Array(lines.size, { Array(lines.first().length, { Pipe.DOT }) })
  var start = Pair(-1, -1)

  for ((y, line) in lines.withIndex()) {
    for ((x, char) in line.withIndex()) {
      if (char == 'S') {
        start = Pair(x, y)
        tiles[y][x] = getStartTileAt(lines, x, y)
      } else {
        tiles[y][x] = char.toPipe()
      }
    }
  }

  return start to tiles
}

fun getStartTileAt(tiles: List<String>, x: Int, y: Int): Pipe {
  val top =
      tiles.elementAtOrNull(y - 1)?.elementAtOrNull(x)?.toPipe().let {
        when (it) {
          Pipe.PIPE, Pipe.SEVEN, Pipe.F -> true
          else -> false
        }
      }
  val right =
      tiles.elementAtOrNull(y)?.elementAtOrNull(x + 1)?.toPipe().let {
        when (it) {
          Pipe.DASH, Pipe.J, Pipe.SEVEN -> true
          else -> false
        }
      }
  val bottom =
      tiles.elementAtOrNull(y + 1)?.elementAtOrNull(x)?.toPipe().let {
        when (it) {
          Pipe.PIPE, Pipe.L, Pipe.J -> true
          else -> false
        }
      }
  val left =
      tiles.elementAtOrNull(y)?.elementAtOrNull(x - 1)?.toPipe().let {
        when (it) {
          Pipe.L, Pipe.F, Pipe.DASH -> true
          else -> false
        }
      }
  return when {
    top && right -> Pipe.L
    top && bottom -> Pipe.PIPE
    top && left -> Pipe.J
    right && bottom -> Pipe.F
    right && left -> Pipe.DASH
    bottom && left -> Pipe.SEVEN
    else -> throw Exception("bad start tile at $x, $y")
  }
}

typealias Distances = Array<Array<Int>>

typealias Point = Pair<Int, Int>

fun Tiles.walkLoop(start: Point): Distances {
  val distances: Distances = Array(this.size, { Array(this.first().size, { -1 }) })

  var neighbors = setOf<Point>(start)
  var distance = 0
  while (neighbors.isNotEmpty()) {
    for ((x, y) in neighbors) {
      distances[y][x] = distance
    }
    distance++

    neighbors =
        neighbors
            .map { (x, y) ->
              when (this[y][x]) {
                Pipe.PIPE -> listOf(Pair(x, y - 1), Pair(x, y + 1))
                Pipe.DASH -> listOf(Pair(x - 1, y), Pair(x + 1, y))
                Pipe.L -> listOf(Pair(x, y - 1), Pair(x + 1, y))
                Pipe.J -> listOf(Pair(x, y - 1), Pair(x - 1, y))
                Pipe.SEVEN -> listOf(Pair(x - 1, y), Pair(x, y + 1))
                Pipe.F -> listOf(Pair(x + 1, y), Pair(x, y + 1))
                Pipe.DOT ->
                    throw Exception("Unclosed loop (${start.first}, ${start.second}) -> ($x, $y)")
              }.filter { (x, y) ->
                y in 0..this.lastIndex && x in 0..this.first().lastIndex && distances[y][x] < 0
              }
            }
            .flatten()
            .toSet()
  }

  return distances
}

fun maxOfDistances(distances: Distances): Int {
  var max = 0
  for (y in distances.indices) {
    for (x in distances.indices) {
      if (distances[y][x] > max) {
        max = distances[y][x]
      }
    }
  }

  return max
}

fun part1(lines: List<String>) {
  val (start, tiles) = parseTiles(lines)
  val distances = tiles.walkLoop(start)

  println(maxOfDistances(distances))
}

fun Distances.display(): String =
    this.map { row -> row.map { d -> if (d < 0) ' ' else 'x' }.joinToString("") }.joinToString("\n")

fun part2(lines: List<String>) {
  val (start, tiles) = parseTiles(lines)
  val distances = tiles.walkLoop(start)

  val candidates = mutableSetOf<Point>()

  for ((y, line) in tiles.withIndex()) {
    for ((x, _) in line.withIndex()) {
      if (distances[y][x] < 0) {
        candidates.add(Pair(x, y))
      }
    }
  }

  val enclosed = mutableSetOf<Point>()

  val lineEnd = tiles.first().lastIndex
  var started: Pipe
  var crossed: Int
  for ((x, y) in candidates) {
    started = Pipe.DOT
    crossed = 0
    for (ray in (x + 1)..lineEnd) {
      if (distances[y][ray] < 0) {
        continue
      }

      when (tiles[y][ray]) {
        Pipe.PIPE -> crossed++
        Pipe.DASH -> continue
        Pipe.F -> started = Pipe.F
        Pipe.L -> started = Pipe.L
        Pipe.SEVEN -> {
          if (started == Pipe.L) {
            crossed++
          }
        }
        Pipe.J -> {
          if (started == Pipe.F) {
            crossed++
          }
        }
        else -> throw Exception("Unreachable")
      }
    }
    if (crossed % 2 == 1) {
      enclosed.add(Pair(x, y))
    }
  }

  println(enclosed.size)
  println(enclosed)
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day10.jar <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines()
  part2(lines)
}
