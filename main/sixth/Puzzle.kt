package sixth

import java.io.File

class Puzzle {

    fun run(): Pair<*, *> {
        val (matrix, instructions) = parseToMatrix(read())
        val resultsByLine = applyInstruction(matrix, instructions)
        val total = resultsByLine.sum()
        val result2 = partTwo(read())
        return Pair(total, result2)
    }

    fun applyInstruction(matrix: Array<LongArray>, instruction: List<String>): MutableList<Long> {
        val result = mutableListOf<Long>()
        instruction.forEachIndexed { i, currentInstruction ->
            val line = mutableListOf<Long>()
            for (x in 0 until matrix.size) {
                line.add(matrix[x][i])
            }
            when (currentInstruction) {
                "+"  -> result.add(line.sum())
                "*"  -> result.add(line.reduce { acc, i -> acc * i })
                else -> throw IllegalArgumentException("Invalid instruction: $currentInstruction")
            }
        }
        return result
    }

    fun partTwo(input: List<String>): Long {
        val xLength = input.size
        val yLength = input[0].length
        val matrix = Array(xLength) { CharArray(yLength) }
        for ((x, str) in input.withIndex()) {
            val reversed = str.reversed()
            for ((y, char) in reversed.withIndex()) {
                matrix[x][y] = char
            }
        }
        var result = 0L
        val currentIteration = mutableListOf<Long>()
        val numbers = mutableListOf<Long>()
        for (y in 0 until yLength) {
            for (x in 0 until xLength) {
                val char = matrix[x][y]
                if (char.isDigit()) numbers.add(char.digitToInt().toLong())

            }
            numbers.reverse()
            numbers.forEachIndexed { index, number ->
                var multiplier = 1
                for (i in 0 until index) multiplier *= 10
                numbers[index] = number * multiplier
            }
            if (!numbers.isEmpty()) currentIteration.add(numbers.sumOf { it })
            numbers.clear()

            val potentialInstruction = matrix[xLength - 1][y]
            if (potentialInstruction in listOf('+', '*')) {
                when (potentialInstruction) {
                    '+' -> result += (currentIteration.sum())
                    '*' -> result += (currentIteration.reduce { acc, i -> acc * i })
                }
                currentIteration.clear()
            }
        }

        return result
    }

    fun parseToMatrix(input: List<String>): Pair<Array<LongArray>, List<String>> {
        val yLength = input[0].length
        val xLength = input.size - 1
        val matrix = Array(xLength) { LongArray(yLength) }
        for ((i, string) in input.subList(0, xLength).withIndex()) {
            val sanitizedString = removeExtraWhitespaces(string)
            for ((j, numberString) in sanitizedString.split(" ").withIndex()) {
                matrix[i][j] = numberString.toLong()
            }
        }
        val instructions = removeExtraWhitespaces(input.last()).trim().split(" ")
        return Pair(matrix, instructions)
    }

    fun removeExtraWhitespaces(input: String): String {
        return input.replace("\\s+".toRegex(), " ")
    }

    fun read(): List<String> {
        return File("main/sixth/input.txt").readLines()
//        return listOf(
//            "3-5","10-14",
//            "16-20","12-18",
//            "",
//        )
    }
}

fun main() {
    Puzzle().run().also(::println)
}
