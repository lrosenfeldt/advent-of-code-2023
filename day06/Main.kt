package Day06

import java.io.File
import kotlin.math.*

data class Race(val time: Long, val record: Long) {
  fun winningStrategies(): LongRange {
    // quadratic equation solved
    val left = time / 2.0 - sqrt(time * time / 4.0 - record)
    val right = time / 2.0 + sqrt(time * time / 4.0 - record)

    val ceiledLeft = ceil(left)
    val lower =
        when (left) {
          ceiledLeft -> (left + 1.0).toLong()
          else -> ceiledLeft.toLong()
        }

    val flooredRight = floor(right)
    val upper =
        when (right) {
          flooredRight -> (right - 1.0).toLong()
          else -> flooredRight.toLong()
        }

    return lower..upper
  }
}

fun parseLine(line: String): List<Long> {
  return line.substringAfter(':').split(' ').map { it.trim() }.filter { it.isNotEmpty() }.map {
    it.toLong()
  }
}

fun parseRaces(lines: List<String>): List<Race> {
  val times = parseLine(lines.first())
  val records = parseLine(lines.elementAt(1))
  return times.zip(records).map { (first, last) -> Race(first, last) }
}

fun part1(lines: List<String>): Unit {
  val races = parseRaces(lines)
  println(races.map { it.winningStrategies().count() }.reduce { mult, factor -> mult * factor })
}

fun part2(lines: List<String>): Unit {
  val time = lines.first().substringAfter(':').filter { it.isWhitespace().not() }.toLong()
  val distance = lines.elementAt(1).substringAfter(':').filter { it.isWhitespace().not() }.toLong()
  val race = Race(time, distance)
  println(race.winningStrategies())
  println(race.winningStrategies().count())
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day06 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines()
  part2(lines)
}
