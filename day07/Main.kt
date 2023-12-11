package Day07

import java.io.File

data class Hand(val type: Int, val cardValues: List<Int>, val bid: Int) : Comparable<Hand> {
  override fun compareTo(other: Hand): Int {
    if (type != other.type) {
      return type - other.type
    }

    val firstDiff =
        cardValues.zip(other.cardValues).map { (first, second) -> first - second }.find { it != 0 }
    return when (firstDiff) {
      null -> 0
      else -> firstDiff
    }
  }
}

fun List<Hand>.winnings(): Int {
  return this.sorted().mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
}

fun part1(lines: List<String>): Unit {
  fun handType(hand: String): Int {
    val highest =
        hand.groupBy { it }.mapValues { it.value.size }.values.toList().sortedDescending().let {
          it.first() to it.elementAtOrElse(1, { 0 })
        }
    return when (highest.first) {
      5 -> 7
      4 -> 6
      3 -> if (highest.second == 2) 5 else 4
      2 -> if (highest.second == 2) 3 else 2
      1 -> 1
      else -> -1
    }
  }

  fun cardValue(card: Char): Int {
    return when (card) {
      'A' -> 14
      'K' -> 13
      'Q' -> 12
      'J' -> 11
      'T' -> 10
      in '2'..'9' -> card.toString().toInt()
      else -> -1
    }
  }

  val hands =
      lines.map {
        it.split(' ').let {
          Hand(handType(it.first()), it.first().map(::cardValue), it.elementAt(1).toInt())
        }
      }
  println(hands.winnings())
}

fun part2(lines: List<String>): Unit {
  fun cardValue(card: Char): Int {
    return when (card) {
      'A' -> 14
      'K' -> 13
      'Q' -> 12
      'J' -> 1
      'T' -> 10
      in '2'..'9' -> card.toString().toInt()
      else -> -1
    }
  }

  fun handType(hand: String): Int {
    val (cards, joker) =
        hand
            .groupBy { it }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }
            .partition { it.first != 'J' }
    val highest =
        when (cards.firstOrNull()) {
          null -> joker.first()
          else ->
              Pair(cards.first().first, cards.first().second + (joker.firstOrNull()?.second ?: 0))
        }
    val secondHighestCount = cards.elementAtOrNull(1)?.second ?: 0
    return when (highest.second) {
      5 -> 7
      4 -> 6
      3 -> if (secondHighestCount == 2) 5 else 4
      2 -> if (secondHighestCount == 2) 3 else 2
      1 -> 1
      else -> -1
    }
  }
  val hands =
      lines.map {
        it.split(' ').let {
          Hand(handType(it.first()), it.first().map(::cardValue), it.elementAt(1).toInt())
        }
      }
  println(hands.winnings())
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day07 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines().filter { it.trim().length > 0 }

  part2(lines)
}
