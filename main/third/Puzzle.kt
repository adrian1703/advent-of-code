package third

import java.io.File

class Puzzle {

    fun run(): Pair<Int, Long> {
        val result = read()
            .map { convert(it) }
            .sum()

        val result2 = read()
            .map { convert2(it) }
            .sum()


        return Pair(result, result2)
    }

    fun convert(input: String): Int {
        var before = Pair(-100, 0)
        var middle = Pair(-100, 0)
        var after = Pair(-100, 0)
        for (i in 0 until input.length) {
            val value = input[i].digitToInt()
            if (value > after.first) after = Pair(value, i)
            if (after.first > middle.first) {
                val oldMiddel = middle
                middle = after
                after = Pair(-100, 0)
                before = oldMiddel
            }
        }
//        println("$before $middle $after")

        val result = maxOf(before.first * 10 + middle.first, middle.first * 10 + after.first)
        return result
    }

    fun convert2(input: String): Long {
        val output = IntArray(12) { -1 }
        var j = 11
        var ii = -1
        for (i in input.indices.reversed()) {
            if (j == -1) {
                ii = i
                break
            }
            output[j--] = input[i].digitToInt()
        }
        for (i in (0..ii).reversed()) {
            val value = input[i].digitToInt()
            cascadeChange(0, value, output)
        }

//        println(output.joinToString(" "))
        return output.joinToString("").toLong()
    }

    fun cascadeChange(index: Int, value: Int, data: IntArray) {
        if (index == data.size) return
        val current = data[index]
        if (current <= value) {
            data[index] = value
            return cascadeChange(index + 1, current, data)
        } else return
    }

    fun read(): List<String> {
        return File("main/third/input.txt").readLines()
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
