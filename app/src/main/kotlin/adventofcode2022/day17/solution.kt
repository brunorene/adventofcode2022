package adventofcode2022.day17

const val RIGHT_LIMIT = 6

data class Point(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

sealed class Move

object Left : Move()
object Right : Move()

sealed class Piece {

    internal var points: List<Point> = listOf()

    abstract fun samePiece(left: Int, bottom: Int): Piece

    fun relativeTo(referenceY: Int) {
        return points.map { p -> Point(p.x, p.y - referenceY) }
    }

    fun topY() = points.maxOfOrNull { it.y } ?: 0

    private fun hit(allPieces: List<Piece>, futurePlacement: (Point) -> Point) =
        listOf(Bottom, *allPieces.dropLast(1).toTypedArray())
            .any { it.points.intersect(points.map(futurePlacement).toSet()).isNotEmpty() }

    fun moveSide(move: Move, allPieces: List<Piece>) {
        points = when (move) {
            Left -> if (points[0].x > 0 && !hit(allPieces) { Point(it.x - 1, it.y) }) {
                points.map { Point(it.x - 1, it.y) }
            } else points

            Right -> if (points[points.size / 2].x < RIGHT_LIMIT && !hit(allPieces) { Point(it.x + 1, it.y) }) {
                points.map { Point(it.x + 1, it.y) }
            } else points
        }
    }

    fun moveDown(allPieces: List<Piece>) =
        if (!hit(allPieces) { Point(it.x, it.y - 1) }) {
            points = points.map { Point(it.x, it.y - 1) }
            true
        } else false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Piece) return false

        if (points != other.points) return false

        return true
    }

    override fun hashCode(): Int {
        return points.hashCode()
    }
}

object Bottom : Piece() {
    init {
        points =
            listOf(
                Point(0, 0),
                Point(1, 0),
                Point(2, 0),
                Point(3, 0),
                Point(4, 0),
                Point(5, 0),
                Point(6, 0)
            )
    }

    override fun samePiece(left: Int, bottom: Int): Piece {
        return this
    }
}

class Cross(left: Int, bottom: Int) : Piece() {

    init {
        //   3
        // 0 1 2
        //   4
        points = listOf(
            Point(left, bottom + 1),
            Point(left + 1, bottom + 1),
            Point(left + 2, bottom + 1),
            Point(left + 1, bottom + 2),
            Point(left + 1, bottom),
        )
    }

    override fun samePiece(left: Int, bottom: Int): Piece {
        return
    }
}

class UpperL(left: Int, bottom: Int) : Piece() {

    init {
        //     2
        //     3
        // 0 1 4
        points = listOf(
            Point(left, bottom),
            Point(left + 1, bottom),
            Point(left + 2, bottom + 2),
            Point(left + 2, bottom + 1),
            Point(left + 2, bottom),
        )
    }
}

class Horizontal(left: Int, bottom: Int) : Piece() {

    init {
        // 0 1 3 2
        points = listOf(
            Point(left, bottom),
            Point(left + 1, bottom),
            Point(left + 3, bottom),
            Point(left + 2, bottom),
        )
    }

    override fun toString(): String {
        return "Horizontal -> $points"
    }
}

class Vertical(left: Int, bottom: Int) : Piece() {

    init {
        // 0
        // 1
        // 2
        // 3
        points = listOf(
            Point(left, bottom + 3),
            Point(left, bottom + 2),
            Point(left, bottom + 1),
            Point(left, bottom),
        )
    }
}

class Square(left: Int, bottom: Int) : Piece() {

    init {
        // 0 1
        // 3 2
        points = listOf(
            Point(left, bottom + 1),
            Point(left + 1, bottom + 1),
            Point(left + 1, bottom),
            Point(left, bottom),
        )
    }
}

const val GAP = 4

var firstPiece: Piece = Horizontal(2, GAP)

fun nextPiece(allPieces: List<Piece>) = allPieces.maxOf { it.topY() }.let { topY ->
    when (allPieces.last()) {
        is Horizontal -> Cross(2, topY + GAP)
        is Cross -> UpperL(2, topY + GAP)
        is UpperL -> Vertical(2, topY + GAP)
        is Vertical -> Square(2, topY + GAP)
        is Square -> Horizontal(2, topY + GAP)
        is Bottom -> Horizontal(2, GAP)
    }
}

fun draw(pieces: List<Piece>) {
    val allPoints = pieces.flatMap { it.points }.toSet()
    val maxY = allPoints.maxOf { it.y }
    val output = (maxY downTo 0).flatMap { y -> (0..6).map { x -> Point(x, y) } }.joinToString("") { p ->
        (if (allPoints.contains(p)) "#" else if (p.y == 0) "-" else ".") + if (p.x == 6) "\n" else ""
    }
    println(output)
}

fun moves() = generateSequence {
    object {}.javaClass
        .getResourceAsStream("/input17.txt")
        ?.bufferedReader()
        ?.lineSequence()
        ?.flatMap { it.map { c -> if (c == '<') Left else Right } }
        ?: sequenceOf()
}.flatten()

fun game(moves: Sequence<Move>, pieceCount: Int): List<Piece> {
    val pieces = mutableListOf(firstPiece)

    for (move in moves) {
        pieces.last().moveSide(move, pieces)
        val moved = pieces.last().moveDown(pieces)
        if (!moved) {
            if (pieces.size == pieceCount) return pieces
            pieces.add(nextPiece(pieces))
        }
    }

    return pieces
}

fun solutionDay17Part1() {
    println(game(moves(), 2022).last().topY())
}

fun solutionDay17Part2() {
    val moves = moves()
    val pieces = game(moves, 10000)
    val allX = { p: List<Piece>, idx: Int -> p[idx].points.map { it.x }.sorted() }
    (5 until 5000 step 5).map { idx1 ->
        (5 until idx1 - 5 step 5).map { idx2 ->
            if (allX(pieces, idx1) == allX(pieces, idx2)) {
                pieces.slice((idx2 + 1 until idx1)).map { p -> }
            }
        }
    }
}
