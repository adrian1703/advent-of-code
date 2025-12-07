package seventh

import java.io.File

class Puzzle {
    var split = 0
    fun run(): Pair<*, *> {
        val matrix = parseToMatrix(read())
        val findStartingPosition = findStartingPosition(matrix)
        propagateSplit(matrix, findStartingPosition)
        val matrix2 = parseToMatrix2(read())
        propagateRealities(matrix2)
        return Pair(split, calcRealities(matrix2))
    }

    fun calcRealities(matrix: Array<LongArray>): Long {
        return matrix[matrix.size - 1].sumOf { it }
    }

    fun findStartingPosition(matrix: Array<CharArray>): Pair<Int, Int> {
        return matrix.withIndex().filter { it.value.contains('S') }.map { Pair(it.index, it.value.indexOf('S')) }
            .first()
    }

    fun propagateSplit(matrix: Array<CharArray>, position: Pair<Int, Int>) {
        if (position.first + 1 >= matrix.size) {
            return
        } // default break

        val nextPosition = matrix[position.first + 1][position.second]
        when (nextPosition) {
            '.' -> {
                matrix[position.first + 1][position.second] = '|'
                propagateSplit(matrix, Pair(position.first + 1, position.second))
            }

            '|' -> {
                return
            }

            '^' -> {
                split++
                propagateSplit(matrix, Pair(position.first + 1, position.second - 1))
                propagateSplit(matrix, Pair(position.first + 1, position.second + 1))
            }
        }
    }

    fun propagateRealities(matrix: Array<LongArray>) {
        for ((x, row) in matrix.withIndex()) {
            if (x == matrix.size - 1) break
            for ((y, value) in row.withIndex()) {
                if (value == -1L) continue // '^'
                if (value == 0L) continue // 'no beam
                // value has to be min 1 beam aka >= 1
                val nextPosition = matrix[x + 1][y]
                when (nextPosition) {
                    in 0L..Long.MAX_VALUE -> {
                        matrix[x + 1][y] += value
                    }

                    -1L                   -> {
                        matrix[x + 1][y - 1] += value
                        matrix[x + 1][y + 1] += value
                    }
                }
            }
        }
    }

    fun parseToMatrix(input: List<String>): Array<CharArray> {
        val yLength = input[0].length
        val xLength = input.size
        val matrix = Array(xLength) { CharArray(yLength) }
        for ((i, string) in input.subList(0, xLength).withIndex()) {
            for (j in 0 until string.length) {
                matrix[i][j] = string[j]
            }
        }
        return matrix
    }

    fun parseToMatrix2(input: List<String>): Array<LongArray> {
        val yLength = input[0].length
        val xLength = input.size
        val matrix = Array(xLength) { LongArray(yLength) }
        for ((i, string) in input.subList(0, xLength).withIndex()) {
            for (j in 0 until string.length) {
                when (string[j]) {
                    '.' -> matrix[i][j] = 0L
                    '^' -> matrix[i][j] = -1L
                    'S' -> matrix[i][j] = 1L
                }
            }
        }
        return matrix
    }

    fun read(): List<String> {
        return File("main/seventh/input.txt").readLines()
//        return listOf(
//            ".......S.......",
//            "...............",
//            ".......^.......",
//            "...............",
//            "......^.^......",
//            "...............",
//            ".....^.^.^.....",
//            "...............",
//            "....^.^...^....",
//            "...............",
//            "...^.^...^.^...",
//            "...............",
//            "..^...^.....^..",
//            "...............",
//            ".^.^.^.^.^...^.",
//            "...............",
//        )
    }
}

fun main() {
    Puzzle().run().also(::println)
}
