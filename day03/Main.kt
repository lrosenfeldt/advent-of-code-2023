package Day03

import java.io.File

data class Number(val number: Int, val lineNo: Int, val position: IntRange)

data class Symbol(val literal: Char, val lineNo: Int, val position: Int)

data class Puzzle(
    val lines: Int,
    val columns: Int,
    val numbers: List<Number>,
    val symbols: List<Symbol>
) {
  fun partNumbers(): Set<Number> {
    val result = mutableSetOf<Number>()
    for (symbol in symbols) {
      val lineRange =
          when (symbol.lineNo) {
            0 -> 0..symbol.lineNo + 1
            lines - 1 -> symbol.lineNo - 1 until lines
            else -> symbol.lineNo - 1..symbol.lineNo + 1
          }
      val columnRange =
          when (symbol.position) {
            0 -> 0..symbol.position + 1
            columns - 1 -> symbol.position - 1 until columns
            else -> symbol.position - 1..symbol.position + 1
          }
      result.addAll(
          numbers.filter { it.lineNo in lineRange && it.position.any { it in columnRange } }
      )
    }

    return result
  }

  fun gears(): List<Pair<Number, Number>> {
    val gearSymbols = symbols.filter { s -> s.literal == '*' }

    val gear = mutableListOf<Pair<Number, Number>>()
    for (symbol in gearSymbols) {
      val lineRange =
          when (symbol.lineNo) {
            0 -> 0..symbol.lineNo + 1
            lines - 1 -> symbol.lineNo - 1 until lines
            else -> symbol.lineNo - 1..symbol.lineNo + 1
          }
      val columnRange =
          when (symbol.position) {
            0 -> 0..symbol.position + 1
            columns - 1 -> symbol.position - 1 until columns
            else -> symbol.position - 1..symbol.position + 1
          }
      val adjacentNumbers = numbers.filter { it.lineNo in lineRange && it.position.any { it in columnRange } }
      if (adjacentNumbers.size != 2) {
        continue 
      } else {
        gear.add( adjacentNumbers[0] to adjacentNumbers[1])
      }
    }

    return gear
  }
}

fun parseLine(lineNo: Int, line: String): Pair<List<Number>, List<Symbol>> {
  val numbers = mutableListOf<Number>()
  val symbols = mutableListOf<Symbol>()
  
  var position = 0
  var char: Char
  while (position < line.length) {
    char = line[position]
    when (char) {
      '.' -> {
        position++
      }
      in '0'..'9' -> {
        val digits = StringBuilder()
        val start = position
        var currentIndex = position

        do {
          digits.append(line[currentIndex++])
        } while (currentIndex < line.length && line[currentIndex].isDigit())

        numbers.add(Number(digits.toString().toInt(), lineNo, start until currentIndex))
        position = currentIndex
      }
      '#', '$', '%', '&', '*', '+', '-', '/', '=', '@' -> {
        symbols.add(Symbol(char, lineNo, position))
        position++
      }
    }
  }

  return Pair(numbers, symbols)
}

fun parse(lines: List<String>): Puzzle {
  val numbers = mutableListOf<Number>()
  val symbols = mutableListOf<Symbol>()

  for (lineNo in 0 until lines.size) {
    val (nums, syms) = parseLine(lineNo, lines[lineNo])
    numbers.addAll(nums)
    symbols.addAll(syms)
  }

  return Puzzle(lines.size, lines.first().length, numbers, symbols)
}


fun main(args: Array<String>) {
  if (args.isEmpty()) {
    println("Usage: Day03 <input file>")
    System.exit(1)
  }

  val input = File(args.first())
  val lines = input.readLines().filter { it.length > 0 }

  fun part1(lines: List<String>) {
    val sumOfPartNumbers = parse(lines).partNumbers().map { it.number }.sum()
    println(sumOfPartNumbers)
  }

  fun part2(lines: List<String>) {
    val gearRatios = parse(lines).gears().map {
      val (n1, n2) = it
      n1.number * n2.number
    }
    println(gearRatios)
    println(gearRatios.sum())
  }

  part2(lines)
}
