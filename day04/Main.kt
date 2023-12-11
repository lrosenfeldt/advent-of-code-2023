import java.io.File

data class Scratchcard(val id: Int, val winning: Set<Int>, val yours: Set<Int>) {
  fun scored(): Int {
    return yours.intersect(winning).size
  }
}

fun parseLine(line: String): Scratchcard {
  return line.split(':').let {
    val id = it.first().removePrefix("Card").trim().toInt()
    val parts = it.elementAt(1).split('|')
    val winning = parts.first().split(' ').filter { it.isNotEmpty() }.map { it.toInt() }.toSet()
    val yours = parts.elementAt(1).split(' ').filter { it.isNotEmpty() }.map { it.toInt() }.toSet()
    Scratchcard(id, winning, yours)
  }
}

fun part1(lines: List<String>): Unit {
  val result =
      lines.map(::parseLine).map { it.scored() }.sumOf { if (it <= 1) it else 1 shl (it - 1) }
  println("score: $result")
}

data class Card(val range: Int, val count: Int)

fun toCard(card: Scratchcard): Card {
  return Card(card.scored(), 1)
}

fun evalScore(cards: List<Card>): Int {
  return evalScoreHelper(0, cards)
}

tailrec fun evalScoreHelper(score: Int, cards: List<Card>): Int {
  if (cards.isEmpty()) {
    return score
  }

  val head = cards.first()
  return evalScoreHelper(
      score + head.count,
      cards.drop(1).mapIndexed { index, card ->
        if (index in 0 until head.range) Card(card.range, card.count + head.count) else card
      }
  )
}

fun part2(lines: List<String>): Unit {
  val cards = lines.map(::parseLine).map(::toCard)
  val result = evalScore(cards)
  println("score = $result")
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    println("Usage: Day04 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines().filter { it.trim().length > 0 }

  part2(lines)
}
