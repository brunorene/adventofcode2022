package adventofcode2022.day02

fun createPlay(symbol: String) = when (symbol) {
    "A", "X" -> Rock
    "B", "Y" -> Paper
    "C", "Z" -> Scissors
    else -> throw Exception("Impossible")
}

fun createOutcome(symbol: String) = when (symbol) {
    "X" -> Lose
    "Y" -> Tie
    "Z" -> Win
    else -> throw Exception("Impossible")
}

sealed interface Outcome {
    fun against(other: Play): Int
}

object Win : Outcome {
    override fun against(other: Play) = when (other) {
        Paper -> Scissors.scoreAgainst(other) + Scissors.shape()
        Rock -> Paper.scoreAgainst(other) + Paper.shape()
        Scissors -> Rock.scoreAgainst(other) + Rock.shape()
    }
}

object Lose : Outcome {
    override fun against(other: Play) = when (other) {
        Paper -> Rock.scoreAgainst(other) + Rock.shape()
        Rock -> Scissors.scoreAgainst(other) + Scissors.shape()
        Scissors -> Paper.scoreAgainst(other) + Paper.shape()
    }
}

object Tie : Outcome {
    override fun against(other: Play) = when (other) {
        Paper -> Paper.scoreAgainst(other) + Paper.shape()
        Rock -> Rock.scoreAgainst(other) + Rock.shape()
        Scissors -> Scissors.scoreAgainst(other) + Scissors.shape()
    }
}

sealed interface Play {
    fun shape(): Int
    fun scoreAgainst(other: Play): Int
}

object Rock : Play {
    override fun shape() = 1
    override fun scoreAgainst(other: Play) = when (other) {
        Paper -> 0
        Rock -> 3
        Scissors -> 6
    }
}

object Paper : Play {
    override fun shape() = 2
    override fun scoreAgainst(other: Play) = when (other) {
        Paper -> 3
        Rock -> 6
        Scissors -> 0
    }
}

object Scissors : Play {
    override fun shape() = 3
    override fun scoreAgainst(other: Play) = when (other) {
        Paper -> 6
        Rock -> 0
        Scissors -> 3
    }
}

class Round1(private val adversary: Play, private val me: Play) {
    fun score() = me.scoreAgainst(adversary) + me.shape()
}

class Round2(private val adversary: Play, private val me: Outcome) {
    fun score() = me.against(adversary)
}

fun plays(outcome: (String, String) -> Int) = object {}.javaClass
    .getResourceAsStream("/input02.txt")
    ?.bufferedReader()
    ?.lineSequence()
    ?.map { it.split(" ").let { pair -> outcome(pair[0], pair[1]) } }
    ?.toList() ?: listOf(0)

fun solutionDay02Part1() {
    println(plays { adversary, me -> Round1(createPlay(adversary), createPlay(me)).score() }.sum())
}

fun solutionDay02Part2() {
    println(plays { adversary, me -> Round2(createPlay(adversary), createOutcome(me)).score() }.sum())
}