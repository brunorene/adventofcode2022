package adventofcode2022.day10

data class Cpu(val x: Int, val cycle: Int) {

    private fun internalPaint() = if ((cycle - 1) % 40 in (x - 1..x + 1)) "#" else " "

    fun paint(): String = when (cycle) {
        in (40..240 step 40) -> internalPaint() + "\n"
        in 241..3000 -> ""
        else -> internalPaint()
    }
}

fun signalStrength() = object {}.javaClass
    .getResourceAsStream("/input10.txt")
    ?.bufferedReader()
    ?.lineSequence()
    ?.fold(mutableListOf(Cpu(1, 1))) { cpuCycles, command ->
        val lastCycle = cpuCycles.last()
        cpuCycles.add(Cpu(lastCycle.x, lastCycle.cycle + 1))
        if (command.startsWith("addx")) {
            cpuCycles.add(Cpu(lastCycle.x + command.split(" ")[1].toInt(), lastCycle.cycle + 2))
        }
        cpuCycles
    } ?: listOf()

fun solutionDay10Part1() {
    println(
        signalStrength()
            .filter { it.cycle in (20..220 step 40) }
            .sumOf { it.cycle * it.x }
    )
}

fun solutionDay10Part2() {
    println(signalStrength().joinToString("") { it.paint() })
}