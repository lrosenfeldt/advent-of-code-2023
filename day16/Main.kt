package Day16

import java.io.File

typealias Layout = Array<Array<Char>>

fun Layout.nrows(): Int = this.size

fun Layout.ncols(): Int = this.first().size

fun List<String>.toLayout(): Layout {
  val layout = Array(this.size) { Array(this.first().length, { 0.toChar() }) }
  for ((i, row) in this.withIndex()) {
    for ((j, col) in row.withIndex()) {
      layout[i][j] = col
    }
  }

  return layout
}

enum class Direction(val x: Int, val y: Int) {
  NORTH(0, -1),
  EAST(1, 0),
  SOUTH(0, 1),
  WEST(-1, 0);

  fun isLR(): Boolean = this == Direction.EAST || this == Direction.WEST

  fun isTB(): Boolean = this == Direction.NORTH || this == Direction.SOUTH
}

typealias Point = Pair<Int, Int>

typealias BeamPoint = Triple<Int, Int, Direction>

fun BeamPoint.isAtEdge(layout: Layout): Boolean {
  val (x, y, d) = this
  return when (d) {
    Direction.NORTH -> y == 0
    Direction.EAST -> x == (layout.ncols() - 1)
    Direction.SOUTH -> y == (layout.nrows() - 1)
    Direction.WEST -> x == 0
  }
}

fun BeamPoint.isInBounds(layout: Layout): Boolean =
    (this.first in 0 until layout.ncols()) && (this.second in 0 until layout.nrows())

fun Layout.beamPath(start: BeamPoint): List<Point> {
  val visited = mutableSetOf<BeamPoint>()
  val path = mutableListOf<Point>()

  var next = mutableSetOf<BeamPoint>(start)
  var nextStaged: MutableSet<BeamPoint>
  while (next.isNotEmpty() && !visited.containsAll(next)) {
    visited.addAll(next)
    nextStaged = mutableSetOf()

    for (beamPoint in next) {
      if (beamPoint.isAtEdge(this)) {
        continue
      }

      val (x, y, dir) = beamPoint
      val element = this[y][x]
      val directions: List<Direction> =
          when (element) {
            '.' -> listOf(dir)
            '|' -> if (dir.isTB()) listOf(dir) else listOf(Direction.NORTH, Direction.SOUTH)
            '-' -> if (dir.isLR()) listOf(dir) else listOf(Direction.EAST, Direction.WEST)
            '/' -> {
              when (dir) {
                Direction.NORTH -> listOf(Direction.EAST)
                Direction.WEST -> listOf(Direction.SOUTH)
                Direction.SOUTH -> listOf(Direction.WEST)
                Direction.EAST -> listOf(Direction.NORTH)
              }
            }
            '\\' -> {
              when (dir) {
                Direction.NORTH -> listOf(Direction.WEST)
                Direction.WEST -> listOf(Direction.NORTH)
                Direction.SOUTH -> listOf(Direction.EAST)
                Direction.EAST -> listOf(Direction.SOUTH)
              }
            }
            else -> throw Exception("Bad char $element")
          }

      nextStaged.addAll(
          directions.map { d -> Triple(x + d.x, y + d.y, d) }.filter { it.isInBounds(this) }
      )
    }

    next = nextStaged
  }
  return path
}

fun Layout.pathToLines(path: List<Point>): List<String> =
    this.mapIndexed { y, line ->
      line.joinToString("")
          .mapIndexed { x, _ -> if (path.contains(Pair(x, y))) '#' else '.' }
          .joinToString("")
    }

fun part1(lines: List<String>) {
  val layout = lines.toLayout()
  val path = layout.beamPath(Triple(0, 0, Direction.EAST))
  println(path)
  println(layout.pathToLines(path).joinToString("\n"))
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day16 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readText().trimEnd().split(',')
  part1(lines)
}
