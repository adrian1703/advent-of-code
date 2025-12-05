package fifth

import java.io.File

class Puzzle {

    fun run(): Pair<*, *> {
        val (ranges, inventory) = inventory(read())
        // could actually use bloomfilter for performance
        val freshcount = inventory.map { if (idInRange(it, ranges)) 1 else 0 }.sum()
        val mergedRanges = mergeLoop(ranges).sortedBy { it.first }
        val count = mergedRanges.sumOf { it.last - it.first + 1 }
        return Pair(freshcount, count)
    }

    fun mergeLoop(ranges: List<LongRange>): List<LongRange> {
        var result = ranges
        var hasMerged = true
        while (hasMerged) {
            hasMerged = false
            for ((xIndex, toCheck) in result.withIndex()) {
                for ((yIndex, candidate) in result.withIndex()) {
                    if (hasMerged) break
                    if (xIndex == yIndex) continue
                    if (!canMerge(toCheck, candidate)) continue
                    val merged = merge(toCheck, candidate)
                    val newResult = result.toMutableList()
                    newResult.remove(toCheck)
                    newResult.remove(candidate)
                    newResult.addFirst(merged)
                    result = newResult.sortedBy { it.first }
                    hasMerged = true
                }
            }
        }
        return result
    }

    fun canMerge(x: LongRange, y: LongRange): Boolean {
        val smaller = if (x.first <= y.first) x else y
        val larger = if (x.first <= y.first) y else x
        val result = smaller.last >= larger.first
        return result
    }

    fun merge(x: LongRange, y: LongRange): LongRange {
        val smaller = if (x.first <= y.first) x else y
        val larger = if (x.first <= y.first) y else x
        val result = smaller.first..(maxOf(smaller.last, larger.last))
        return result
    }

    fun idInRange(id: Long, ranges: List<LongRange>): Boolean {
        return ranges.any { id in it }
    }

    fun inventory(input: List<String>): Pair<MutableList<LongRange>, MutableList<Long>> {
        val freshRanges = mutableListOf<LongRange>()
        val inventory = mutableListOf<Long>()
        var rangesFlag = true
        for (i in input) {
            if (i == "") {
                rangesFlag = false
                continue
            }
            when (rangesFlag) {
                true  -> {
                    val subStrings = i.split("-") // should be 2
                    freshRanges.add(subStrings[0].toLong()..subStrings[1].toLong())
                }

                false -> {
                    inventory.add(i.toLong())
                }
            }
        }
        return Pair(freshRanges.sortedBy { it.first }.toMutableList(), inventory)
    }

    fun read(): List<String> {
        return File("main/fifth/input.txt").readLines()
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
