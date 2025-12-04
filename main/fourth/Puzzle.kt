package third

import java.io.File

class Puzzle {

    fun run(): Pair<Int, Int> {
        val result = read()
            .let { convertToMatrix(it) }
            .let { countReachableRows(it) }
        val result2 = read()
            .let { convertToMatrix(it) }
            .let {
                var removed = countReachableRowsAndRemove(it)
                var total = removed
                while (removed > 0) {
                    removed = countReachableRowsAndRemove(it)
                    total += removed
                }
                return@let total
            }

        return Pair(result, result2)
    }

    fun countReachableRowsAndRemove(input: Array<IntArray>): Int {
        var result = 0
        for (x in input.indices) {
            val row = input[x]
            for (y in row.indices) {
                if (canBeReached(input, x, y)) {
                    result++
                    input[x][y] = 0
                }
            }
        }
        return result
    }

    fun countReachableRows(input: Array<IntArray>): Int {
        var result = 0
        for (x in input.indices) {
            val row = input[x]
            for (y in row.indices) {
                if (canBeReached(input, x, y)) result++
            }
        }
        return result
    }

    fun canBeReached(input: Array<IntArray>, x: Int, y: Int): Boolean {
        if (input[x][y] == 0) return false // cannot reach nothing
        var neighbors = 0
        for (xx in -1..1) {
            for (yy in -1..1) {
                if (xx == 0 && yy == 0) continue
                val nx = x + xx
                val ny = y + yy
                if (nx < 0 || nx >= input.size || ny < 0 || ny >= input[x].size) continue
                neighbors += input[nx][ny]
            }
        }

        return neighbors < 4
    }

    /**
     * 0----->y
     * |
     * |
     * V
     * x
     *
     * 0= nothing
     * 1= roll
     */
    fun convertToMatrix(input: List<String>): Array<IntArray> {
        val result = Array(input.size) { IntArray(input[0].length) }
        for (x in input.indices) {
            val line = input[x]
            for (y in line.indices) {
                val char = line[y]
                val value = when (char) {
                    '.'  -> 0
                    '@'  -> 1
                    else -> throw IllegalArgumentException("Invalid char $char")
                }
                result[x][y] = value
            }
        }
        return result
    }

    fun read(): List<String> {
        return File("main/fourth/input.txt").readLines()
//        return listOf(
//            "987654321111111",
//            "811111111111119",
//            "234234234234278",
//            "818181911112111",
//        )
    }
}

fun main() {
    Puzzle().run().also(::println)
}
