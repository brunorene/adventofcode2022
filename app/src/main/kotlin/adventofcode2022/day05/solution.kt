package adventofcode2022.day05

fun crates() = listOf(
    ArrayDeque(listOf('H', 'B', 'V', 'W', 'N', 'M', 'L', 'P')),
    ArrayDeque(listOf('M', 'Q', 'H')),
    ArrayDeque(listOf('N', 'D', 'B', 'G', 'F', 'Q', 'M', 'L')),
    ArrayDeque(listOf('Z', 'T', 'F', 'Q', 'M', 'W', 'G')),
    ArrayDeque(listOf('M', 'T', 'H', 'P')),
    ArrayDeque(listOf('C', 'B', 'M', 'J', 'D', 'H', 'G', 'T')),
    ArrayDeque(listOf('M', 'N', 'B', 'F', 'V', 'R')),
    ArrayDeque(listOf('P', 'L', 'H', 'M', 'R', 'G', 'S')),
    ArrayDeque(listOf('P', 'D', 'B', 'C', 'N')),
)

data class Move(val count: Int, val from: Int, val to: Int)

fun onTop(load: (List<ArrayDeque<Char>>, Move) -> List<ArrayDeque<Char>>) = object {}.javaClass
    .getResourceAsStream("/input05.txt")
    ?.bufferedReader()
    ?.lineSequence()
    ?.filter { line -> line.startsWith("move") }
    ?.map { line ->
        val parts = line.split(" ")
        Move(parts[1].toInt(), parts[3].toInt() - 1, parts[5].toInt() - 1)
    }
    ?.fold(crates(), load)
    ?.map { it.last() }
    ?.joinToString("") ?: ""

fun solutionDay05Part1() {
    println(onTop { crates, move ->
        (1..move.count).forEach { _ -> crates[move.to].add(crates[move.from].removeLast()) }
        crates
    })
}

fun solutionDay05Part2() {
    println(onTop { crates, move ->
        crates[move.to].addAll(crates[move.from].takeLast(move.count))
        (1..move.count).forEach { _ -> crates[move.from].removeLast() }
        crates
    })
}

