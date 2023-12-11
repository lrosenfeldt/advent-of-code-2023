package Day01

import java.io.File

fun main(args: Array<String>): Unit {
  if (args.isEmpty()) {
    println("Usage: Day01 <input file>")
    System.exit(1)
  }

  fun firstDigit(line: String): Char? {
    for (char in line) {
      if (char.isDigit()) {
        return char
      }
    }
    return null
  }

  fun lastDigit(line: String): Char? {
    for (char in line.reversed()) {
      if (char.isDigit()) {
        return char
      }
    }
    return null
  }

  fun lineToInt(line: String): Int {
    val first = firstDigit(line)
    val last = lastDigit(line)
    if (first != null && last != null) {
      return "$first$last".toInt()
    }
    return 0
  }

  fun lineToInt2(line: String): Int {
    var first: Char? = null
    for (i in 0 until line.length) {
      val c =
          when {
            line.startsWith("one", i) -> '1'
            line.startsWith("two", i) -> '2'
            line.startsWith("three", i) -> '3'
            line.startsWith("four", i) -> '4'
            line.startsWith("five", i) -> '5'
            line.startsWith("six", i) -> '6'
            line.startsWith("seven", i) -> '7'
            line.startsWith("eight", i) -> '8'
            line.startsWith("nine", i) -> '9'
            line[i].isDigit() -> line[i]
            else -> null
          }
      if (c != null) {
        first = c
        break
      }
    }
    var last: Char? = null
    for (i in line.length - 1 downTo 0) {
      val c =
          when {
            line.startsWith("one", i) -> '1'
            line.startsWith("two", i) -> '2'
            line.startsWith("three", i) -> '3'
            line.startsWith("four", i) -> '4'
            line.startsWith("five", i) -> '5'
            line.startsWith("six", i) -> '6'
            line.startsWith("seven", i) -> '7'
            line.startsWith("eight", i) -> '8'
            line.startsWith("nine", i) -> '9'
            line[i].isDigit() -> line[i]
            else -> null
          }
      if (c != null) {
        last = c
        break
      }
    }
    if (first != null && last != null) {
      return "$first$last".toInt()
    }
    return 0
  }

  val input = File(args[0])
  val solution = input.readLines().map { lineToInt2(it) }.sum()
  println(solution)
}
