package com.adventofcode.day06

import com.adventofcode.readInput

fun main() {
    println(solvePartOne())
    println(solvePartTwo())
}

class SignalReceiver(initialMessage: String) {
    private val currentWindow: ArrayDeque<Char> = ArrayDeque()

    init {
        currentWindow.addAll(initialMessage.toCharArray().toList())
    }

    fun findMarkerLength(message: String, packetMarkerLength: Int): Int {
        var processedCharacters = currentWindow.size
        if (currentWindow.toSet().size == packetMarkerLength) return processedCharacters
        for (letter in message) {
            currentWindow.addLast(letter)
            currentWindow.removeFirst()
            processedCharacters++
            if (currentWindow.toSet().size == packetMarkerLength) return processedCharacters
        }
        return processedCharacters
    }
}

fun solvePartOne(): Int {
    val input = readInput("/day6-input")
    val signalReceiver = SignalReceiver(input[0].take(4))
    return signalReceiver.findMarkerLength(input[0].substring(4), 4)
}

fun solvePartTwo(): Any {
    val input = readInput("/day6-input")
    val signalReceiver = SignalReceiver(input[0].take(14))
    return signalReceiver.findMarkerLength(input[0].substring(14), 14)
}
