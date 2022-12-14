package adventofcode2022.day12

data class Node(val value: Char, val level: Int, val row: Int, val col: Int, val parent: Node?) {

    fun candidateNeighbours(matrix: List<String>, visited: Set<Pair<Int, Int>>): List<Node> {
        val nodes = mutableListOf<Node>()

        for ((r, c) in listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)) {
            val nextRow = row + r
            val nextCol = col + c

            if (nextRow in matrix.indices &&
                nextCol in (0 until matrix[0].length) &&
                matrix[nextRow][nextCol] - value <= 1 &&
                !visited.contains(nextRow to nextCol)
            ) {
                nodes.add(Node(matrix[nextRow][nextCol], level + 1, nextRow, nextCol, this))
            }
        }
        return nodes
    }
}


fun river(startingPoint: Regex): Int {
    val possibleStarts = mutableListOf<Pair<Int, Int>>()
    var end = -1 to -1
    val matrix = object {}.javaClass
        .getResourceAsStream("/input12.txt")
        ?.bufferedReader()
        ?.lineSequence()
        ?.foldIndexed(mutableListOf<String>()) { row, matrix, line ->
            var rowData = line
            for (col in rowData.indices) {
                if (startingPoint.matches(rowData.subSequence(col, col + 1))) {
                    possibleStarts.add(row to col)
                    rowData = rowData.replace('S', 'a')
                }
            }
            val col = rowData.indexOf('E')
            if (col >= 0) {
                end = row to col
                rowData = rowData.replace('E', 'z')
            }
            matrix.add(rowData)
            matrix
        } ?: listOf()

    val result = mutableListOf<Int>()

    for (start in possibleStarts) {
        val visited = HashSet<Pair<Int, Int>>()

        val queue = ArrayDeque<Node>()

        val startNode = Node(
            matrix[start.first][start.second],
            0,
            start.first,
            start.second,
            null
        )
        visited.add(start.first to start.second)

        queue.add(startNode)

        while (!queue.isEmpty()) {
            val current = queue.removeFirst()
            if (current.row == end.first && current.col == end.second) {
                result.add(current.level)
            }

            for (neighbour in current.candidateNeighbours(matrix, visited)) {
                visited.add(neighbour.row to neighbour.col)
                queue.add(neighbour)
            }
        }
    }

    return result.min()
}

fun solutionDay12Part1() {
    println(river("S".toRegex()))
}

fun solutionDay12Part2() {
    println(river("[Sa]".toRegex()))
}

