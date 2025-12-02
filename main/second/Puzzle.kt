package second

import java.io.File

class Puzzle {

    fun run(): Long {
        val result = read()
            .map { bloomInput(it) } //List<Long>
//            .map { filterSequence(it) } //List<Long>
            .map { extendedFilterSequence(it) } //List<Long>
            .map { countSequence(it) } //Long
            .fold(0L) { acc, i -> acc + i }
        return result
    }

    fun countSequence(input: List<Long>): Long {
        return input.fold(0L) { acc, i -> acc + i }
    }

    fun extendedFilterSequence(input: List<Long>): List<Long> {
        val result = mutableListOf<Long>()
        input.forEach {
            val stringLong = it.toString()
            if (idIsSilly(stringLong)) {
                result.add(it)
                println("added $it")
            }
        }
        return result
    }

    fun idIsSilly(input: String): Boolean {
        val length = input.length
        for (i in 1..length / 2) {
            if (length % i != 0) continue
            val chunked = input.chunked(i)
            val chunksAreEqual = chunked.fold(true) { acc, s -> acc && (chunked[0] == s) }
            if (chunksAreEqual) return true
        }
        return false
    }

    fun filterSequence(input: List<Long>): List<Long> {
        val result = mutableListOf<Long>()
        input.forEach {
            val stringLong = it.toString()
            if (stringLong.length % 2 == 1) {
                return@forEach
            }
            val leftHalf = stringLong.substring(0, stringLong.length / 2)
            val rightHalf = stringLong.substring(stringLong.length / 2, stringLong.length)
            if (leftHalf != rightHalf) {
                return@forEach
            }
            result.add(it)
            println("added $it")
        }
        return result
    }

    fun bloomInput(input: Pair<Long, Long>): List<Long> {
        val (start, end) = input
        return LongRange(start, end).toList()
    }

    fun read(): List<Pair<Long, Long>> {
        return File("main/second/input.txt").readLines()[0].split(",").map {
            val split = it.split('-')
            return@map Pair(split[0].toLong(), split[1].toLong())
        }
    }
}

fun main() {
    Puzzle().run().also(::println)
}
