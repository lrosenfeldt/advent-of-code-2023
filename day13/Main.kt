package Day13

import java.io.File

fun List<String>.splitOnEmpty(): List<List<String>> {
	val parts = mutableListOf<List<String>>()
    var current = mutableListOf<String>()
   	for (line in this) {
        if (line == "") {
            parts.add(current)
            current = mutableListOf()
        } else {
            current.add(line)
        }
    }
    if (current.isNotEmpty()) {
        parts.add(current)
    }
    return parts
}


fun List<String>.mirror(at: Int): List<Pair<String, String>> = this.slice(0..at).reversed().zip(this.slice((at+1)..this.lastIndex))

fun Pair<String, String>.diff(): Int = this.first.zip(this.second).count { it.first != it.second }

fun List<Pair<String, String>>.distance(): Int = this.sumOf { it.diff() }

fun List<String>.reflection(smudges: Int = 0): Int = 
    this.indices.take(this.lastIndex).filter { this.mirror(it).distance() == smudges }.map { it + 1 }.firstOrNull() ?: -1

fun List<String>.transpose(): List<String> = 
    (0 until this.first().length)
    	.map { x -> this.map { line -> line.elementAt(x) }.joinToString("") }

fun part1(patterns: List<List<String>>): Unit {
  val results = patterns.map {
    val above = it.reflection(0)
    if (above < 0) it.transpose().reflection(0) else above * 100
  }
  println(results.sum())
}

fun part2(patterns: List<List<String>>): Unit {
  val results = patterns.map {
    val above = it.reflection(1)
    if (above < 0) it.transpose().reflection(1) else above * 100
  }
  println(results.sum())
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
      System.err.println("Usage: Day13 <input file>")
      System.exit(1)
    } 

    val patterns = File(args.first()).readLines().splitOnEmpty()
    part2(patterns)
}
