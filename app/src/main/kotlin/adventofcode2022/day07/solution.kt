package adventofcode2022.day07

data class File(val name: String, val size: Int) {
    override fun toString(): String = "$name:$size"
}

data class Directory(
    val name: String,
    val parent: Directory?,
    val dirs: MutableMap<String, Directory>,
    val files: MutableSet<File>
) {
    fun size(): Int = files.fold(0) { acc, file -> acc + file.size } +
            dirs.values.sumOf { it.size() }

    fun totalSize(dirLimit: Int): Int {
        var mySize = size()
        if (mySize > dirLimit) mySize = 0
        return mySize + dirs.values.sumOf { it.totalSize(dirLimit) }
    }

    fun descendantsSize(): List<Int> = (listOf(size()) + dirs.values.flatMap { it.descendantsSize() }).sorted()

    override fun toString(): String = "$name - ${dirs.keys} - $files"
}

fun rootDir(): Directory {
    val root = Directory("/", null, mutableMapOf(), hashSetOf())
    object {}.javaClass
        .getResourceAsStream("/input07.txt")
        ?.bufferedReader()
        ?.lineSequence()
        ?.fold(root) { pwd, line ->
            val cdMatch = "\\$ cd ([^/]+)".toRegex().find(line)
            val dirMatch = "dir (.+)".toRegex().find(line)
            val fileMatch = "([0-9]+) (.+)".toRegex().find(line)
            when {
                "$ cd .." == line -> pwd.parent ?: pwd
                cdMatch != null -> pwd.dirs[cdMatch.groupValues[1]] ?: pwd
                dirMatch != null -> {
                    val dir = Directory(dirMatch.groupValues[1], pwd, mutableMapOf(), hashSetOf())
                    pwd.dirs.putIfAbsent(dir.name, dir)
                    pwd
                }

                fileMatch != null -> {
                    pwd.files.add(File(fileMatch.groupValues[2], fileMatch.groupValues[1].toInt()))
                    pwd
                }

                else -> pwd
            }
        }
    return root
}

fun solutionDay07Part1() {
    println(rootDir().totalSize(100000))
}

fun solutionDay07Part2() {
    val root = rootDir()
    println(root.descendantsSize().first { it >= root.size() - 40000000 })
}