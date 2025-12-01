package first

import java.io.File

class Puzzle {

    var pointer = 50
    var zeroCounter = 0
    var zerosPassed = 0
    var zerosPassed2 = 0

    fun run(): Triple<Int, Int, Int> {
        read().forEach { applyInstruction(it) }
        pointer = 50
        read().forEach { applyInstruction2(it) }
        pointer = 50
        read().forEach { applyInstruction3(it) }
        return Triple(zeroCounter, zerosPassed, zerosPassed2)
    }

    fun applyInstruction(instruction: String) {
        val lr = instruction.first()
        var distance = instruction.drop(1).toInt()
        distance %= 100
        if (lr == 'R') {
            pointer += distance
            pointer %= 100
            zeroCounter += if (pointer == 0) 1 else 0
        } else if (lr == 'L') {
            pointer -= distance
            if (pointer < 0) pointer += 100
            zeroCounter += if (pointer == 0) 1 else 0
        }
    }

    fun applyInstruction2(instruction: String) {
        val lr = instruction.first()
        val pointerStart = pointer
        var distance = instruction.drop(1).toInt()
        zerosPassed += distance / 100 // full rotations
        distance %= 100 // inner rotation 0 - 100

        if (lr == 'R') {
            pointer += distance
        } else if (lr == 'L') {
            pointer -= distance
        }
        // pointer 0-99 ->  -99 bis 199
        if (pointer >= 100) {
            pointer %= 100
            zerosPassed++
        } else if (pointer < 0) {
            pointer += 100
            if (pointerStart > 0) zerosPassed++ // this prevents the 0 to -xx from counting which already counted
        } else if (pointer == 0) zerosPassed++
    }

    fun applyInstruction3(instruction: String) {
        val lr = instruction.first()
        var distance = instruction.drop(1).toInt()
        while (distance > 0) {
            if (lr == 'R') {
                pointer++
            } else if (lr == 'L') {
                pointer--
            }
            if (pointer == 100) pointer = 0
            if (pointer == -1) pointer = 99
            if (pointer == 0) zerosPassed2++
            distance--
        }
    }

    fun read(): List<String> {
        return File("main/first/input.txt").readLines()
    }
}

fun main() {
    Puzzle().run().also(::println)
}
