package Day02

import java.io.File

data class Cubes(val red: Int, val green: Int, val blue: Int) {
  fun power(): Int {
    return red * green * blue
  }
}

data class Game(val id: Int, val cubes: List<Cubes>)

// input: [3 blue, 4 red]
fun parseRound(rounds: List<String>): Cubes {
  val reds = rounds.find { it.endsWith("red") }?.substringBefore("red")?.trim()?.toInt()
  val greens = rounds.find { it.endsWith("green") }?.substringBefore("green")?.trim()?.toInt()
  val blues = rounds.find { it.endsWith("blue") }?.substringBefore("blue")?.trim()?.toInt()
  return Cubes(reds ?: 0, greens ?: 0, blues ?: 0)
}

fun parseLine(line: String): Game {
  return line.split(':').let {
    val id = it.first().substringAfter("Game").trim().toInt()
    val rounds: List<Cubes> = it.last().split(';').map { it.split(',') }.map(::parseRound)
    Game(id, rounds)
  }
}

fun isImportantGame(game: Game): Boolean {
  return game.cubes.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }
}

fun minimumCubes(cl: Cubes, cr: Cubes): Cubes {
  val reds =
      if (cl.red > cr.red) {
        cl.red
      } else {
        cr.red
      }
  val blues =
      if (cl.blue > cr.blue) {
        cl.blue
      } else {
        cr.blue
      }
  val greens =
      if (cl.green > cr.green) {
        cl.green
      } else {
        cr.green
      }
  return Cubes(reds, greens, blues)
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    println("Usage: Day02 <input file>")
    System.exit(1)
  }

  val input = File(args.first())
  val results = input.readLines().map { it.trim() }.filter { it.length > 0 }.map(::parseLine)

  fun part1(results: List<Game>) {
    println(results.filter(::isImportantGame).map { it.id }.sum())
  }

  val sumOfPowers =
      results
          .map { it.cubes }
          .map { it.fold(Cubes(0, 0, 0), ::minimumCubes).power() }
          .sum()
  println(sumOfPowers)
}
