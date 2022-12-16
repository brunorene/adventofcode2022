package adventofcode2022.day14

sealed class Material

object Rock : Material() {
    override fun toString(): String {
        return "#"
    }
}

object Sand : Material() {
    override fun toString(): String {
        return "o"
    }
}

fun abyss(regolith: MutableMap<Pair<Int, Int>, Material>) = regolith.map { (k, _) -> k.second }.max()

fun fallSand(
    regolith: MutableMap<Pair<Int, Int>, Material>,
    abyss: Int,
    bottom: Int
): MutableMap<Pair<Int, Int>, Material> {
    var current = 500 to 0
    while (true) {
        var down = false
        for (x in listOf(0, -1, 1)) {
            val next = current.first + x to current.second + 1
            if (regolith[next] !in listOf(Rock, Sand) && next.second < bottom) {
                current = next
                down = true
                break
            }
        }
        if (current.second >= abyss && bottom == Int.MAX_VALUE) {
            return regolith
        }
        if (!down) {
            regolith[current] = Sand
            return regolith
        }
    }
}

fun allSand(hasBottom: Boolean): MutableMap<Pair<Int, Int>, Material> {
    var regolith = regolith()
    val abyss = abyss(regolith)
    val bottom = if (hasBottom) abyss + 2 else Int.MAX_VALUE
    do {
        val size = regolith.size
        regolith = fallSand(regolith, abyss, bottom)
    } while (regolith.size > size)
    return regolith
}

fun regolith(): MutableMap<Pair<Int, Int>, Material> = object {}.javaClass
    .getResourceAsStream("/input14.txt")
    ?.bufferedReader()
    ?.lineSequence()
    ?.flatMap { line ->
        line.splitToSequence(" -> ".toRegex())
            .map { edge -> val (x, y) = edge.split(","); x.toInt() to y.toInt() }
            .zipWithNext()
            .flatMap { (a, b) ->
                val (x1, y1) = a
                val (x2, y2) = b
                val result = mutableListOf<Pair<Int, Int>>()
                for (x in (listOf(x1, x2).min()..listOf(x1, x2).max())) {
                    for (y in (listOf(y1, y2).min()..listOf(y1, y2).max())) {
                        result.add(x to y)
                    }
                }
                result
            }
    }?.associateBy({ k -> k }, { _ -> Rock })?.toMutableMap() ?: mutableMapOf()

fun solutionDay14Part1() {
    println(allSand(false).filter { (_, m) -> m == Sand }.count())
}

fun solutionDay14Part2() {
    println(allSand(true).filter { (_, m) -> m == Sand }.count())
}
