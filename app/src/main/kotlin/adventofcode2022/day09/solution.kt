package adventofcode2022.day09

import java.awt.Point
import kotlin.math.abs

fun moveTail(head: Point, tail: Point) =
    when (head.distanceSq(tail)) {
        0.0, 1.0, 2.0 -> tail
        else -> Point(
            if (head.x - tail.x == 0) tail.x else tail.x + ((head.x - tail.x) / abs(head.x - tail.x)),
            if (head.y - tail.y == 0) tail.y else tail.y + ((head.y - tail.y) / abs(head.y - tail.y))
        )
    }

fun ropeMove(knotCount: Int): List<Point> {
    var currentRope = (1..knotCount).map { Point(0, 0) }
    return object {}.javaClass
        .getResourceAsStream("/input09.txt")
        ?.bufferedReader()
        ?.lineSequence()
        ?.map { line -> line.split(" ").let { it[0] to it[1].toInt() } }
        ?.flatMap { (direction, steps) ->
            val moves = mutableListOf<List<Point>>()
            (1..steps).fold(currentRope) { knots, _ ->
                val next = when (direction) {
                    "L" -> Point(knots.first().x - 1, knots.first().y) // Left
                    "R" -> Point(knots.first().x + 1, knots.first().y) // Right
                    "U" -> Point(knots.first().x, knots.first().y + 1) // Up
                    else -> Point(knots.first().x, knots.first().y - 1) // Down
                }.let { newHead ->
                    var currentHead = newHead
                    val result = mutableListOf(newHead)
                    knots.drop(1).forEach { knot ->
                        currentHead = moveTail(currentHead, knot)
                        result.add(currentHead)
                    }
                    println("$direction / $result")
                    result
                }
                moves.add(next)
                next
            }
            moves
        }?.map { currentRope = it; it.last() }
        ?.distinct()
        ?.toList() ?: listOf()
}

fun solutionDay09Part1() {
    println(ropeMove(2).count())
}

fun solutionDay09Part2() {
    println(ropeMove(10).count())
}