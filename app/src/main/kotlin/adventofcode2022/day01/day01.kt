package adventofcode2022.day01

fun caloriesList(): List<Int> = object {}.javaClass
        .getResourceAsStream("/input01.txt")
        ?.bufferedReader()
        ?.lineSequence()
        ?.fold(mutableListOf(0)) { list, line ->
            if (line.isBlank()) list.add(0) else list.add(list.removeLast() + line.toInt())
            list
        } ?: listOf(0)

fun solutionDay01Part1() {
    println(caloriesList().max())
}

fun solutionDay01Part2() {
    println(caloriesList().sortedDescending().take(3).sum())
}