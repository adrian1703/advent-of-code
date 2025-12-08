package eighth

import java.io.File
import kotlin.math.min
import kotlin.math.sqrt

class Puzzle {
    fun run(): Pair<*, *> {
        val triples = read().let { toTriples(it) }
        val distanceMatrix = calcDistanceMatrix(triples)
        val circuitsMap = initCircuitsMap(triples)
        connectionLoop(orderedDistances(triples, distanceMatrix), circuitsMap)
        val circuits = circuitsMap.values.map { it.second }.toSet().toList().sortedByDescending { it.size }
        val size = circuits.subList(0, 3).map { it.size }.reduce { acc, set -> acc * set }

        val triplets = connectionLoop2(orderedDistances(triples, distanceMatrix), initCircuitsMap(triples))
        println(triplets)
        return Pair(size, (triplets!!.first.first.toInt() * triplets.second.first.toInt()))
    }

    fun connectionLoop(
        orderedDistances: List<Triple<Triple<Double, Double, Double>, Triple<Double, Double, Double>, Double>>,
        circuitsMap: HashMap<Triple<Double, Double, Double>, Pair<Int, Set<Triple<Double, Double, Double>>>>,
    ) {
        var connectionsMade = 0
        var targetConnections = 1000
        val distances = orderedDistances.toMutableList()
        while (connectionsMade <= targetConnections) {
            connectionsMade++
            val (triple1, triple2, distance) = distances.removeFirst()
            val circuit1 = circuitsMap[triple1]!!
            val circuit2 = circuitsMap[triple2]!!
            if (circuit1 == circuit2) continue // they already belong togehter
            val newCircuitId = min(circuit1.first, circuit2.first)
            val newCircuit = circuitsMap[triple1]!!.second.union(circuitsMap[triple2]!!.second)
            newCircuit.forEach { circuitsMap[it] = Pair(newCircuitId, newCircuit) }
        }
    }

    fun connectionLoop2(
        orderedDistances: List<Triple<Triple<Double, Double, Double>, Triple<Double, Double, Double>, Double>>,
        circuitsMap: HashMap<Triple<Double, Double, Double>, Pair<Int, Set<Triple<Double, Double, Double>>>>,
    ): Pair<Triple<Double, Double, Double>, Triple<Double, Double, Double>>? {
        val distances = orderedDistances.toMutableList()
        var lastSeen: Pair<Triple<Double, Double, Double>, Triple<Double, Double, Double>>? = null
        while (distances.isNotEmpty()) {
            val (triple1, triple2, distance) = distances.removeFirst()
            val circuit1 = circuitsMap[triple1]!!
            val circuit2 = circuitsMap[triple2]!!
            if (circuit1 == circuit2) continue // they already belong togehter
            val newCircuitId = min(circuit1.first, circuit2.first)
            val newCircuit = circuitsMap[triple1]!!.second.union(circuitsMap[triple2]!!.second)
            newCircuit.forEach { circuitsMap[it] = Pair(newCircuitId, newCircuit) }
            lastSeen = Pair(triple1, triple2)
        }
        return lastSeen
    }

    fun orderedDistances(
        triplets: List<Triple<Double, Double, Double>>,
        distances: Array<DoubleArray>,
    ): List<Triple<Triple<Double, Double, Double>, Triple<Double, Double, Double>, Double>> {
        val resultTmp = mutableListOf<
                Triple<
                        Triple<Double, Double, Double>,
                        Triple<Double, Double, Double>,
                        Double>
                >()
        for ((x, triple) in triplets.withIndex()) {
            for ((y, triple2) in triplets.withIndex()) {
                resultTmp.add(Triple(triple, triple2, distances[x][y]))
            }
        }
        // remove duplicates
        val seen = mutableSetOf<Pair<Triple<Double, Double, Double>, Triple<Double, Double, Double>>>()
        val result = mutableListOf<Triple<
                Triple<Double, Double, Double>,
                Triple<Double, Double, Double>,
                Double>
                >()
        resultTmp.forEach { triple ->
            if(seen.contains(triple.first to triple.second) || seen.contains(triple.second to triple.first)) return@forEach
            seen.add(triple.first to triple.second)
            seen.add(triple.second to triple.first)
            result.add(triple)
        }
        return result.sortedBy { it.third }
    }

    fun calcDistanceMatrix(input: List<Triple<Double, Double, Double>>): Array<DoubleArray> {
        val result = Array(input.size) { DoubleArray(input.size) }
        for ((x, triple) in input.withIndex()) {
            for ((y, triple2) in input.withIndex()) {
                if (x == y) result[x][y] = Double.POSITIVE_INFINITY
                else result[x][y] = calcDistance(triple, triple2)
            }
        }
        return result
    }

    fun initCircuitsMap(input: List<Triple<Double, Double, Double>>): HashMap<Triple<Double, Double, Double>, Pair<Int, Set<Triple<Double, Double, Double>>>> {
        val result = HashMap<Triple<Double, Double, Double>,Pair<Int, Set< Triple<Double, Double, Double>>>> ()
        var counter = 0
        for (triple in input) {
            result[triple] = Pair(counter, mutableSetOf(triple))
            counter++
        }
        return result
    }

    fun calcDistance(triple1: Triple<Double, Double, Double>, triple2: Triple<Double, Double, Double>): Double {
        val x1 = triple1.first
        val y1 = triple1.second
        val z1 = triple1.third
        val x2 = triple2.first
        val y2 = triple2.second
        val z2 = triple2.third
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1))
    }

    fun toTriples(input: List<String>): List<Triple<Double, Double, Double>> {
        val result = mutableListOf<Triple<Double, Double, Double>>()
        input.forEach { str ->
            val split = str.split(",")
            result.add(Triple(split[0].toDouble(), split[1].toDouble(), split[2].toDouble()))
        }
        return result
    }

    fun read(): List<String> {
        return File("main/eighth/input.txt").readLines()
    }
}

fun main() {
    Puzzle().run().also(::println)
}
