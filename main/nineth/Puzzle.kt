package nineth

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Puzzle {

    fun run(): Pair<*, *> {
        val input = read()
        val verticies = toPairs(input)
        val dist = largestRectangle(verticies)
        val outer = outerWalls(verticies)
        val dist2 = largestRectangle2(verticies, outer)
        return Pair(dist, dist2)
    }

    fun calcRectangle(pairA: Pair<Long, Long>, pairB: Pair<Long, Long>): Long {
        val xDistance = abs(pairB.first - pairA.first) + 1
        val yDistance = abs(pairB.second - pairA.second) + 1
        return xDistance * yDistance
    }

    fun largestRectangle(input: List<Pair<Long, Long>>): Long {
        var largestDistance = 0L
        for (pairA in input) {
            for (pairB in input) {
                val distance = calcRectangle(pairA, pairB)
                if (largestDistance < distance) largestDistance = distance
            }
        }
        return largestDistance
    }

    fun checkIfInRectangle(
        pairA: Pair<Long, Long>,
        pairB: Pair<Long, Long>,
        walls: List<Pair<Pair<Long, Long>, Pair<Long, Long>>>,
    ): Boolean {
        val a = pairA
        val b = Pair(pairB.first, pairA.second)
        val c = Pair(pairA.first, pairB.second)
        val d = pairB

        for (wall in walls) {
            val e = wall.first
            val f = wall.second
            if (
                strictIntersect(a, b, e, f)
                || strictIntersect(a, c, e, f)
                || strictIntersect(c, d, e, f)
                || strictIntersect(b, d, e, f)
            ) {
                return false
            }
        }
        return true
    }

    // 2439897250
    fun strictIntersect(
        a: Pair<Long, Long>,
        b: Pair<Long, Long>,
        c: Pair<Long, Long>,
        d: Pair<Long, Long>
    ): Boolean {
        // Helper: computes the orientation of triplet (p,q,r)
        fun orientation(p: Pair<Long, Long>, q: Pair<Long, Long>, r: Pair<Long, Long>): Int {
            val value = (q.second - p.second) * (r.first - q.first) - (q.first - p.first) * (r.second - q.second)
            return when {
                value > 0 -> 1   // Clockwise
                value < 0 -> -1  // Counterclockwise
                else -> 0        // Colinear
            }
        }
        val o1 = orientation(a, b, c)
        val o2 = orientation(a, b, d)
        val o3 = orientation(c, d, a)
        val o4 = orientation(c, d, b)
        // Only return true when all orientations are strictly non-zero and different
        return o1 != o2 && o3 != o4 && o1 != 0 && o2 != 0 && o3 != 0 && o4 != 0
    }


    //2439897250
    fun intersect2(a: Pair<Long, Long>, b: Pair<Long, Long>, c: Pair<Long, Long>, d: Pair<Long, Long>): Boolean {
        val lineIsVertical = a.first == b.first //is downwards
        val boundaryIsVertical = c.first == d.first
        if (lineIsVertical && boundaryIsVertical) return false

        if (lineIsVertical && !boundaryIsVertical) {
            val firstCheck = a.first < max(c.first, d.first) && a.first > min(c.first, d.first)
            val secondCheck = c.second < max(a.second, b.second) && c.first > min(a.second, b.second)
            return firstCheck && secondCheck
        } else {
            val firstCheck = a.second < max(c.second, d.second) && a.second > min(c.second, d.second)
            val secondCheck = c.first < max(a.first, b.first) && c.first > min(a.first, b.first)
            return firstCheck && secondCheck
        }
    }

    // 118007100
    fun intersect(
        a: Pair<Long, Long>,
        b: Pair<Long, Long>,
        c: Pair<Long, Long>,
        d: Pair<Long, Long>
    ): Boolean {
        return ccw(a, c, d) != ccw(b, c, d) && ccw(a, b, c) != ccw(a, b, d)
    }


    fun ccw(a: Pair<Long, Long>, b: Pair<Long, Long>, c: Pair<Long, Long>): Boolean {
        return (c.second - a.second) * (b.first - a.first) > (b.second - a.second) * (c.first - a.first)
    }

    fun largestRectangle2(input: List<Pair<Long, Long>>, walls: List<Pair<Pair<Long, Long>, Pair<Long, Long>>>): Long {
        var largestDistance = 0L
        for (pairA in input) {
            for (pairB in input) {
                if (!checkIfInRectangle(pairA, pairB, walls)) continue
                val distance = calcRectangle(pairA, pairB)
                if (largestDistance < distance) largestDistance = distance
            }
        }
        return largestDistance
    }

    fun outerWalls(input: List<Pair<Long, Long>>): List<Pair<Pair<Long, Long>, Pair<Long, Long>>> {
        val result = mutableListOf<Pair<Pair<Long, Long>, Pair<Long, Long>>>()
        for ((i, pair) in input.withIndex()) {
            if (i == input.size - 1) result.add(Pair(pair, input[0])) //loop around
            else result.add(Pair(pair, input[i + 1]))
        }
        return result
    }

    fun toPairs(input: List<String>): MutableList<Pair<Long, Long>> {
        val result = mutableListOf<Pair<Long, Long>>()
        input.forEach { line ->
            val split = line.split(",")
            result.add(Pair(split[0].toLong(), split[1].toLong()))
        }
        return result
    }

    fun read(): List<String> {
        return File("main/nineth/input.txt").readLines()
    }
}

fun main() {
    Puzzle().run().also(::println)
}
