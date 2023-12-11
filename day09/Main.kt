package Day09

import java.io.File

fun parse(line: String): List<Long> {
  return line.split(' ').map { it.toLong() }
}

fun solve(ns: List<Long>): Long {
  return solveHelper(ns, 0)
}

tailrec fun solveHelper(
    curr: List<Long>,
    col: Long,
): Long {
  val next = curr.zipWithNext { a, b -> b - a }
  if (next.all { it == 0L }) {
    return curr.last() + col
  }
  return solveHelper(next, col + curr.last())
}

fun solveBack(ns: List<Long>): Long {
  return solveBackHelper(ns, listOf()).foldRight(0L) { el, acc -> el - acc }
}

tailrec fun solveBackHelper(curr: List<Long>, hist: List<Long>): List<Long> {
  if (curr.all { it == 0L }) {
    return hist
  }
  val next = curr.zipWithNext { a, b -> b - a }
  return solveBackHelper(next, hist + curr.first())
}

fun part1(lines: List<List<Long>>): Unit {
  val result = lines.map(::solve).sum()
  println("result = $result")
}

fun part2(lines: List<List<Long>>): Unit {
  val result = lines.map(::solveBack).sum()
  println("result = $result")
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day08 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines().map(::parse)

  part1(lines)
  part2(lines)
}
