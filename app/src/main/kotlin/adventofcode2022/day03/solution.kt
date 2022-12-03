package adventofcode2022.day03

fun wrongItems() = object {}.javaClass
    .getResourceAsStream("/input03.txt")
    ?.bufferedReader()
    ?.lineSequence()
    ?.map { it.substring(0, it.length / 2) to it.substring(it.length / 2) }
    ?.map { (left, right) -> left.filter { c -> right.contains(c) }[0] }
    ?.map { c -> c - if (c.isLowerCase()) 'a' - 1 else 'A' - 27 }
    ?.toList() ?: listOf()


fun sharedItems(): List<Int> {
    val groups = mutableListOf<MutableList<String>>()

    object {}.javaClass
        .getResourceAsStream("/input03.txt")
        ?.bufferedReader()
        ?.lineSequence()
        ?.forEachIndexed { idx, line ->
            if (idx % 3 == 0) {
                groups += mutableListOf<String>()
            }

            groups[idx / 3].add(line)
        }

    return groups
        .map { trio ->
            trio[0]
                .filter { c -> trio[1].contains(c) }
                .filter { c -> trio[2].contains(c) }
        }
        .map { c -> c[0] - if (c[0].isLowerCase()) 'a' - 1 else 'A' - 27 }
        .toList()
}

fun solutionDay03Part1() {
    println(wrongItems().sum())
}

fun solutionDay03Part2() {
    println(sharedItems().sum())
}
