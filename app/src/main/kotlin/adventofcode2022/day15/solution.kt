package adventofcode2022.day15

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int) {
    fun exclusionLine(beacon: Point, targetY: Int): Set<Point> {
        val manhattanDist = abs(x - beacon.x) + abs(y - beacon.y)

        val diff = manhattanDist - abs(y - targetY)

        return (-diff..diff)
            .map { Point(x + it, targetY) }
            .filter { it != beacon }
            .toSet()
    }

    fun exclusionLimits(
        beacon: Point,
        targetY: Int,
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE
    ): Pair<Point, Point>? {
        val manhattanDist = abs(x - beacon.x) + abs(y - beacon.y)

        val diff = manhattanDist - abs(y - targetY)

        if (diff < 0) {
            return null
        }

        return Point(listOf(x - diff, min).max(), targetY) to Point(listOf(x + diff, max).min(), targetY)
    }
}

fun sensors() = mapOf(
    (3523437 to 2746095) to (3546605 to 2721324),
    (282831 to 991087) to (743030 to -87472),
    (1473740 to 3283213) to (1846785 to 3045894),
    (1290563 to 46916) to (743030 to -87472),
    (3999451 to 15688) to (3283637 to -753607),
    (1139483 to 2716286) to (1846785 to 3045894),
    (3137614 to 2929987) to (3392051 to 3245262),
    (2667083 to 2286333) to (2126582 to 2282363),
    (3699264 to 2920959) to (3546605 to 2721324),
    (3280991 to 2338486) to (3546605 to 2721324),
    (833202 to 92320) to (743030 to -87472),
    (3961416 to 2485266) to (3546605 to 2721324),
    (3002132 to 3500345) to (3392051 to 3245262),
    (2482128 to 2934657) to (1846785 to 3045894),
    (111006 to 2376713) to (354526 to 3163958),
    (424237 to 2718408) to (354526 to 3163958),
    (3954504 to 3606495) to (3392051 to 3245262),
    (2275050 to 2067292) to (2333853 to 2000000),
    (1944813 to 2557878) to (2126582 to 2282363),
    (2227536 to 2152792) to (2126582 to 2282363),
    (3633714 to 1229193) to (3546605 to 2721324),
    (1446898 to 1674290) to (2333853 to 2000000),
    (3713985 to 2744503) to (3546605 to 2721324),
    (2281504 to 3945638) to (1846785 to 3045894),
    (822012 to 3898848) to (354526 to 3163958),
    (89817 to 3512049) to (354526 to 3163958),
    (2594265 to 638715) to (2333853 to 2000000)
).map { (k, v) -> Point(k.first, k.second) to Point(v.first, v.second) }.toMap()

fun solutionDay15Part1(y: Int) {
    println(
        sensors()
            .flatMap { (s, b) -> s.exclusionLine(b, y) }
            .toSet()
            .count()
    )
}

fun solutionDay15Part2(yLimit: Int) {
    println((0..yLimit).map { targetY ->
        val ySlice = sensors()
            .mapNotNull { (s, b) -> s.exclusionLimits(b, targetY, 0, yLimit) }
            .sortedBy { it.first.x }
            .reduce { acc, pair ->
                if (acc.second.x >= pair.first.x - 1)
                    Pair(
                        Point(min(acc.first.x, pair.first.x), targetY),
                        Point(max(acc.second.x, pair.second.x), targetY)
                    ) else acc
            }
        if (ySlice.first.x > 0)
            Point(0, targetY)
        else if (ySlice.second.x < yLimit)
            Point(ySlice.second.x + 1, targetY)
        else
            null
    }.mapNotNull { it }
        .map { beacon -> println(beacon); 4000000.toLong() * beacon.x.toLong() + beacon.y.toLong() })
}
