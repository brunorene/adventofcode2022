package adventofcode2022.day13

sealed class State

object First : State()
object Second : State()
object Separator : State()

sealed class Item

data class Number(val number: Int) : Item() {

    override fun toString() = number.toString()
}

class ItemList(var list: MutableList<Item>) : Item() {

    override fun toString() = list.toString()
}

data class ItemPair(val left: Item, val right: Item) {

    override fun toString() = "$left <==> $right"
}

val simpleListMatch = "\\[([0-9#,]*)]".toRegex()


fun parse(line: String): Item {
    var counter = 0
    val items = mutableMapOf<String, Item>()
    var content = line

    while (true) {
        if (content.startsWith("#")) return items[content]!!

        val match = simpleListMatch.find(content) ?: throw Exception("match is null - $line")
        val group = match.groupValues[1]
        content = when (group) {
            "" -> {
                items["#0"] = ItemList(mutableListOf())
                content.replaceRange(match.range, "#0")
            }

            else -> {
                val next = ++counter
                items["#$next"] = ItemList(
                    group.split(",")
                        .map { if (it.startsWith("#")) items[it]!! else Number(it.toInt()) }
                        .toMutableList()
                )
                content.replaceRange(match.range, "#$next")
            }
        }
    }
}

fun compare(pair: ItemPair): Int = when (pair.left) {
    is Number -> when (pair.right) {
        is Number -> pair.left.number - pair.right.number
        is ItemList -> compare(ItemPair(ItemList(mutableListOf(pair.left)), pair.right))
    }

    is ItemList -> when (pair.right) {
        is Number -> compare(ItemPair(pair.left, ItemList(mutableListOf(pair.right))))
        is ItemList -> {
            var comparison = 0
            for (idx in pair.left.list.indices) {
                comparison = if (pair.right.list.size == idx) {
                    1
                } else {
                    compare(ItemPair(pair.left.list[idx], pair.right.list[idx]))
                }
                if (comparison != 0) {
                    break
                }
            }
            if (pair.left.list.size < pair.right.list.size && comparison == 0) -1 else comparison
        }
    }
}

fun distress(): List<Int> {
    var index = 0
    var state: State = First
    var pair = ItemPair(Number(-1), Number(-1))
    var first: Item = Number(-1)

    return object {}.javaClass
        .getResourceAsStream("/input13.txt")
        ?.bufferedReader()
        ?.lineSequence()
        ?.map { line ->
            when (state) {
                First -> {
                    first = parse(line)
                    state = Second
                    0
                }

                Second -> {
                    pair = ItemPair(first, parse(line))
                    state = Separator
                    0
                }

                Separator -> {
                    state = First
                    index++
                    if (compare(pair) < 0) index else 0
                }
            }
        }?.toList() ?: listOf()
}

fun sortAll(vararg additional: Item) = ((object {}.javaClass
    .getResourceAsStream("/input13.txt")
    ?.bufferedReader()
    ?.lineSequence()
    ?.filter { it.isNotEmpty() }
    ?.map { parse(it) }
    ?.toList() ?: listOf()) + additional)
    .sortedWith { left, right -> compare(ItemPair(left, right)) }
    .mapIndexed { idx, item ->
        val c1 = compare(ItemPair(item, parse("[[2]]")))
        val c2 = compare(ItemPair(item, parse("[[6]]")))
        if (c1 * c2 == 0) idx + 1 else 0
    }
    .filter { it != 0 }
    .reduce { a, b -> a * b }

fun solutionDay13Part1() {
    println(distress().sum())
}

fun solutionDay13Part2() {
    println(sortAll(parse("[[2]]"), parse("[[6]]")))
}
