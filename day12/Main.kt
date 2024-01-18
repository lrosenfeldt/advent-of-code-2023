package Day11

import java.io.File

enum class Operational {
  YES,
  NO,
  NA
}

fun Char.toOperational(): Operational {
  return when (this) {
    '#' -> Operational.NO
    '.' -> Operational.YES
    '?' -> Operational.NA
    else -> throw Exception("Bad operational status $this")
  }
}

fun List<Operational>.findPattern(damaged: Int, start: Int = 0): List<Operational> {
  var patternStart = start
  var patternEnd = this.lastIndex

  var found: Int = 0
  var status: Operational
  for (i in start..this.lastIndex) {
    status = this.elementAt(i) 
    when (this.elementAt(i)) {
      Operational.YES -> {
        if (found < damaged) {
          found = 0
          patternStart = i
        } else if (found == damaged) {
          patternEnd = i
          break
        }
      }
      Operational.NO -> {
        if (found >= damaged) {
          patternStart = 0
          found++ 
        } else {
          found++
        }
      }
      Operational.NA -> {
        if (found < damaged) {
          found++
        } else if (found == damaged) {
          // substring from patternStart to i
          // replaced with ######.
          
        }
      }
    }
  }
}

fun parse(lines: List<String>): List<Pair<List<Operational>, List<Int>>> {
  return lines.map {
    it.split(' ').let {
      it.component1().map { it.toOperational() } to it.component2().split(',').map { it.toInt() }
    }
  }
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day11 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines().take(1)
  val data = parse(lines)

  for ((status, record) in data) {
    for (r in record) {
      val p = status.withIndex().find { (i, ch) ->  }
    }
  }

  println(data)
}
