package Day08

import java.io.File

enum class Direction {
  LEFT,
  RIGHT
}

fun parseDirections(line: String): List<Direction> {
  return line.map {
    when (it) {
      'L' -> Direction.LEFT
      'R' -> Direction.RIGHT
      else -> throw Exception("Bad direction $it")
    }
  }
}

fun Map<String, Pair<String, String>>.navigate(
    from: String,
    to: String,
    by: List<Direction>
): List<String> {
  return navigateHelper(this, to, by, 0, listOf(from))
}

tailrec fun navigateHelper(
    binaryTree: Map<String, Pair<String, String>>,
    destination: String,
    directions: List<Direction>,
    position: Int,
    nodes: List<String>
): List<String> {
  if (nodes.last() == destination) {
    return nodes
  }

  val children = binaryTree.get(nodes.last())!!
  return navigateHelper(
      binaryTree,
      destination,
      directions,
      (position + 1) % directions.size,
      nodes +
          when (directions.elementAt(position)) {
            Direction.LEFT -> children.first
            Direction.RIGHT -> children.second
          }
  )
}

fun parseLineToTriple(line: String): Triple<String, String, String> {
  val (main, children) = line.split('=').let { it.first().trim() to it.elementAt(1).trim() }
  val (left, right) =
      children.split(',').let { it.first().drop(1).trim() to it.elementAt(1).dropLast(1).trim() }
  return Triple(main, left, right)
}

fun part1(lines: List<String>) {
  val directions = parseDirections(lines.first())
  val nodes = lines.drop(2).map(::parseLineToTriple)
  val nodeIds = nodes.associateBy { it.first }.mapValues { it.value.second to it.value.third }

  val route = nodeIds.navigate("AAA", "ZZZ", directions)
  println(route.size - 1)
}

fun Map<String, Pair<String, String>>.pathsUntilCycle(from: String, reachedEnd: (String) -> Boolean, directions: List<Direction>): Pair<List<String>, List<Int>> {
  val path = mutableListOf<String>()
  
  val breakpoints = mutableListOf<Int>()
  var position = 0
  var currentNode = from
  var currentHash = currentNode + position.toString()

  // cycle checks
  val visited = mutableSetOf<String>()

  do {
    // visit current node
    visited.add(currentHash)
    path.add(currentNode)
    // update position
    // check for end of path
    if (reachedEnd(currentNode)) {
      breakpoints.add(path.size - 1)
    }

    // hash=XXX0 node=XXX path=[11A, XXX] visited=[11A0, XXX1]
    // update node
    currentNode = when (directions.elementAt(position)) {
      Direction.LEFT -> this.get(currentNode)!!.first
      Direction.RIGHT -> this.get(currentNode)!!.second
    }
    // update position
    position = (position + 1) % directions.size
    // update its hash
    currentHash = currentNode + position.toString()
  } while (!visited.contains(currentHash))

  return Pair(path, breakpoints)
}

tailrec fun gcd(a: Long, b: Long): Long {
  return when (b) {
    0L -> a
    else -> gcd(b, (a % b))
  }
}

fun lcm(vararg ns: Long): Long {
  if (ns.size == 0) {
    throw IllegalArgumentException()
  } else if (ns.size == 1) {
    return ns.first()
  }
  
  val (a, b) = ns.first().toLong() to ns.elementAt(1).toLong()
  var result = Math.abs(a * b) / gcd(a, b)
  for (n in ns.drop(2)) {
    result = Math.abs(result * n.toLong()) / gcd(result, n.toLong())
  }
  return result
}

fun part2(lines: List<String>): Unit {
  val directions = parseDirections(lines.first())
  val nodes = lines.drop(2).map(::parseLineToTriple)
  val graph = nodes.associateBy { it.first }.mapValues { it.value.second to it.value.third }

  val startNodes = graph.keys.filter { it.endsWith('A') }
  fun reachedEnd(node: String): Boolean {
    return node.endsWith('Z')
  }
  val results = startNodes.map { graph.pathsUntilCycle(it, ::reachedEnd, directions).second }
  if (results.any { it.size != 1 }) {
    System.err.println("Dont know how to deal with different possible routes per start node")
    System.exit(1)
  }
  println(results)
  println(lcm(*results.flatten().map { it.toLong() }.toLongArray()))
}

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    System.err.println("Usage: Day08 <input file>")
    System.exit(1)
  }

  val lines = File(args.first()).readLines()

  part2(lines)
}
