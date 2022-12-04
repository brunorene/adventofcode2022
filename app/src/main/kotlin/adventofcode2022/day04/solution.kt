package adventofcode2022.day04

fun fullOverlap() = object {}.javaClass
    .getResourceAsStream("/input04.txt")
    ?.bufferedReader()
    ?.lineSequence()
    ?.map { line ->
        val (left, right) = line.split(",").let { it[0] to it[1] }
        val (leftMin, leftMax) = left.split("-").let { it[0].toInt() to it[1].toInt() }
        val (rightMin, rightMax) = right.split("-").let { it[0].toInt() to it[1].toInt() }
        (leftMin <= rightMin && rightMax <= leftMax) || (rightMin <= leftMin && leftMax <= rightMax)
    }
    ?.count { it }

fun partialOverlap() = object {}.javaClass
    .getResourceAsStream("/input04.txt")
    ?.bufferedReader()
    ?.lineSequence()
    ?.map { line ->
        val (left, right) = line.split(",").let { it[0] to it[1] }
        val (leftMin, leftMax) = left.split("-").let { it[0].toInt() to it[1].toInt() }
        val (rightMin, rightMax) = right.split("-").let { it[0].toInt() to it[1].toInt() }
        (rightMin in leftMin..leftMax) || (rightMax in leftMin..leftMax) ||
                (leftMin in rightMin..rightMax) || (leftMax in rightMin..rightMax)
    }
    ?.count { it }

fun solutionDay04Part1() {
    println(fullOverlap())
}

fun solutionDay04Part2() {
    println(partialOverlap())
}