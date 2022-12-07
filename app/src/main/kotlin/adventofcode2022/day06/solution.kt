package adventofcode2022.day06

fun findMarker(size: Int) = object {}.javaClass
    .getResourceAsStream("/input06.txt")
    ?.bufferedReader()
    ?.lineSequence()
    ?.map { line ->
        line
            .windowed(size)
            .mapIndexed { idx, window -> idx to window }
            .first { (_, part) -> part.toCharArray().distinct().count() == size }
            .first + size
    }?.first() ?: -1

fun solutionDay06Part1() {
    println(findMarker(4))
}

fun solutionDay06Part2() {
    println(findMarker(14))
}