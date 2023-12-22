package Day14

import java.io.File

typealias Column = String

fun List<String>.transpose(): List<String> =
    (0..this.first().lastIndex).map { column -> this.map { it.elementAt(column) }.joinToString("") }

fun Column.tilt(): Column =
    this.split("#")
        .map { part -> part.partition { it == 'O' }.let { it.first + it.second } }
        .joinToString("#")

fun Column.load(): Long =
    this.mapIndexed { i, char -> (this.length - i).toLong() to char }.sumOf {
      if (it.second == 'O') it.first else 0L
    }

fun part1(lines: List<String>) {
  val result = lines.transpose().map { column -> column.tilt() }.map { it.load() }.sum()
  println(result)
}

typealias Platform = Array<Array<Char>>

fun List<String>.toPlatform(): Platform {
  val platform = Array(this.size) { Array(this.first().length) { ' ' } }
  for (i in 0 until this.size) {
    for (j in 0 until this.first().length) {
      platform[i][j] = this[i][j]
    }
  }
  return platform
}

fun Platform.stringify(): String {
  val builder = StringBuilder()
  for (i in 0 until this.size) {
    for (j in 0 until this.first().size) {
      builder.append(this[i][j])
    }
    builder.append("\n")
  }
  return builder.toString()
}

fun Platform.display() {
  println(this.stringify())
}

fun Platform.nrows(): Int = this.size

fun Platform.ncols(): Int = this.first().size

data class Section(val start: Int, val rocks: Int, val space: Int)

fun Platform.tiltNorth() {
  var rocks: Int
  var space: Int
  var start: Int
  for (col in 0 until this.ncols()) {
    rocks = 0
    space = 0
    start = 0
    for (row in 0..this.nrows()) {
      when {
        row == this.nrows() || this[row][col] == '#' -> {
          for (index in 1..rocks) {
            this[start + index - 1][col] = 'O'
          }
          for (index in 1..space) {
            this[start + rocks - 1 + index][col] = '.'
          }
          rocks = 0
          space = 0
          start = row + 1
        }
        this[row][col] == 'O' -> rocks++
        this[row][col] == '.' -> space++
        else -> throw Exception("Bad character ${this[row][col]} at ($row, $col)")
      }
    }
  }
}

fun Platform.tiltSouth() {
  var rocks: Int
  var space: Int
  var start: Int
  for (col in 0 until this.ncols()) {
    rocks = 0
    space = 0
    start = 0
    for (row in 0..this.nrows()) {
      when {
        row == this.nrows() || this[row][col] == '#' -> {
          for (index in 1..space) {
            this[start + index - 1][col] = '.'
          }
          for (index in 1..rocks) {
            this[start + space - 1 + index][col] = 'O'
          }
          rocks = 0
          space = 0
          start = row + 1
        }
        this[row][col] == 'O' -> rocks++
        this[row][col] == '.' -> space++
        else -> throw Exception("Bad character ${this[row][col]} at ($row, $col)")
      }
    }
  }
}

fun Platform.tiltWest() {
  var rocks: Int
  var space: Int
  var start: Int
  for (row in 0 until this.nrows()) {
    rocks = 0
    space = 0
    start = 0
    for (col in 0..this.ncols()) {
      when {
        col == this.ncols() || this[row][col] == '#' -> {
          for (index in 1..rocks) {
            this[row][start + index - 1] = 'O'
          }
          for (index in 1..space) {
            this[row][start + rocks - 1 + index] = '.'
          }
          rocks = 0
          space = 0
          start = col + 1
        }
        this[row][col] == 'O' -> rocks++
        this[row][col] == '.' -> space++
        else -> throw Exception("Bad character ${this[row][col]} at ($row, $col)")
      }
    }
  }
}

fun Platform.tiltEast() {
  var rocks: Int
  var space: Int
  var start: Int
  for (row in 0 until this.nrows()) {
    rocks = 0
    space = 0
    start = 0
    for (col in 0..this.ncols()) {
      when {
        col == this.ncols() || this[row][col] == '#' -> {
          for (index in 1..space) {
            this[row][start + index - 1] = '.'
          }
          for (index in 1..rocks) {
            this[row][start + space - 1 + index] = 'O'
          }
          rocks = 0
          space = 0
          start = col + 1
        }
        this[row][col] == 'O' -> rocks++
        this[row][col] == '.' -> space++
        else -> throw Exception("Bad character ${this[row][col]} at ($row, $col)")
      }
    }
  }
}

fun Platform.cycle() {
  this.tiltNorth()
  this.tiltWest()
  this.tiltSouth()
  this.tiltEast()
}

fun Platform.load(): Long {
  var sum = 0L
  for (row in 0 until this.nrows()) {
    for (col in 0 until this.ncols()) {
      if (this[row][col] == 'O') {
        sum += this.nrows() - row
      }
    }
  }

  return sum
}

fun part2(lines: List<String>) {
  val cycles = 1000000000L
  val platform = lines.toPlatform()
  val states = mutableListOf<String>()

  var breakpoint = -1L
  var cycleLength = -1L
  var index: Int
  for (c in 1..cycles) {
    states.add(platform.stringify())
    platform.cycle()
    index = states.indexOf(platform.stringify())
    if (index != -1) {
      println("repetetion found on cycle $c")
      breakpoint = c 
      cycleLength = breakpoint - index
      break
    }
  }

  if (breakpoint != -1L) {
    val n = (cycles - breakpoint) % cycleLength
    for (c in 1..n) {
      platform.cycle()
    }
  }

  println(platform.load())
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day14 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines()

  part2(lines)
}
