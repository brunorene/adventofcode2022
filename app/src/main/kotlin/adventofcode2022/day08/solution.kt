package adventofcode2022.day08

data class Tree(
    val height: Int,
    val up: ArrayDeque<Int>,
    val down: ArrayDeque<Int>,
    val left: ArrayDeque<Int>,
    val right: ArrayDeque<Int>
) {
    fun score(): Int =
        listOf(up, down, left, right)
            .map { view -> view to (view.indexOfFirst { it >= height } + 1) }
            .map { (view, intermediate) -> if (intermediate == 0) view.size else intermediate }
            .reduce { acc, i -> acc * i }
}

data class Coord(val x: Int, val y: Int)

fun parseForest(): Map<Coord, Tree> {
    val forest = mutableMapOf<Coord, Tree>()
    object {}.javaClass
        .getResourceAsStream("/input08.txt")
        ?.bufferedReader()
        ?.lineSequence()
        ?.forEachIndexed { row, line ->
            line.forEachIndexed { col, height ->
                val tree = Tree(height.digitToInt(), ArrayDeque(), ArrayDeque(), ArrayDeque(), ArrayDeque())
                for (c in col - 1 downTo 0) tree.left.add(line[c].digitToInt())
                for (c in col + 1 until line.length) tree.right.add(line[c].digitToInt())
                for (r in 0 until row) {
                    forest[Coord(col, r)]?.also { aboveTree ->
                        aboveTree.down.add(height.digitToInt())
                        tree.up.addFirst(aboveTree.height)
                    }
                }
                forest[Coord(col, row)] = tree
            }
        }
    return forest
}

fun solutionDay08Part1() {
    println(parseForest().filter { (_, tree) ->
        (tree.up.maxOrNull() ?: -1) < tree.height ||
                (tree.down.maxOrNull() ?: -1) < tree.height ||
                (tree.left.maxOrNull() ?: -1) < tree.height ||
                (tree.right.maxOrNull() ?: -1) < tree.height
    }.count())
}

fun solutionDay08Part2() {
    println(parseForest().values.maxOfOrNull { it.score() })
}
