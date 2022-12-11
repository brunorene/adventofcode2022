package adventofcode2022.day11

fun monkeys(smoothing: Long) = listOf(
    Monkey(mutableListOf(54, 61, 97, 63, 74), { it * 7 }, 17, smoothing),
    Monkey(mutableListOf(61, 70, 97, 64, 99, 83, 52, 87), { it + 8 }, 2, smoothing),
    Monkey(mutableListOf(60, 67, 80, 65), { it * 13 }, 5, smoothing),
    Monkey(mutableListOf(61, 70, 76, 69, 82, 56), { it + 7 }, 3, smoothing),
    Monkey(mutableListOf(79, 98), { it + 2 }, 7, smoothing),
    Monkey(mutableListOf(72, 79, 55), { it + 1 }, 13, smoothing),
    Monkey(mutableListOf(63), { it + 4 }, 19, smoothing),
    Monkey(mutableListOf(72, 51, 93, 63, 80, 86, 81), { it * it }, 11, smoothing)
).let { result ->
    result[0].monkeyTrue = result[5]
    result[0].monkeyFalse = result[3]
    result[1].monkeyTrue = result[7]
    result[1].monkeyFalse = result[6]
    result[2].monkeyTrue = result[1]
    result[2].monkeyFalse = result[6]
    result[3].monkeyTrue = result[5]
    result[3].monkeyFalse = result[2]
    result[4].monkeyTrue = result[0]
    result[4].monkeyFalse = result[3]
    result[5].monkeyTrue = result[2]
    result[5].monkeyFalse = result[1]
    result[6].monkeyTrue = result[7]
    result[6].monkeyFalse = result[4]
    result[7].monkeyTrue = result[0]
    result[7].monkeyFalse = result[4]
    result
}

class Monkey(
    private val items: MutableList<Long>,
    private val operation: (old: Long) -> Long,
    val testDivisor: Long,
    private val worrySmoothing: Long
) {
    var monkeyTrue: Monkey? = null
    var monkeyFalse: Monkey? = null
    var inspectCount: Long = 0
    var modulus: Long = 0

    private fun addItem(item: Long) {
        items.add(item)
    }

    fun turn() {
        (1..items.size).forEach { _ ->
            inspectCount++
            var head = items.removeAt(0)
            head = (operation(head) / worrySmoothing) % modulus
            if (head % testDivisor == 0.toLong()) {
                monkeyTrue?.addItem(head)
            } else {
                monkeyFalse?.addItem(head)
            }
        }
    }
}

fun round(limit: Long, smoothing: Long): List<Monkey> {
    val monkeys = monkeys(smoothing)
    val modulus = monkeys.fold(1.toLong()) { acc, monkey -> acc * monkey.testDivisor }

    (1..limit).forEach { _ ->
        for (monkey in monkeys) {
            monkey.modulus = modulus
            monkey.turn()
        }
    }
    return monkeys
}

fun results(monkeys: List<Monkey>) {
    println(monkeys.sortedBy { it.inspectCount }.reversed()
        .filterIndexed { index, _ -> index in (0..1) }
        .fold(1.toLong()) { acc, monkey -> acc * monkey.inspectCount }
    )
}

fun solutionDay11Part1() {
    results(round(20, 3))
}

fun solutionDay11Part2() {
    results(round(10000, 1))
}