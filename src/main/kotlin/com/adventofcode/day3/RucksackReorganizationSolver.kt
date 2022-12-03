package com.adventofcode.day3

import readInput

fun main() {
    println(solvePartOne())
    println(solvePartTwo())
}

fun solvePartOne(): Int {
    return readInput("/day3-input")
        .map {
            val splittedLine = it.chunked(it.length / 2)
            splittedLine[0].toCharArray() to splittedLine[1].toCharArray()
        }
        .flatMap { it.first.intersect(it.second.toSet()) }
        .sumOf { letterPriority(it) }
}

fun solvePartTwo(): Int {
    return readInput("/day3-input")
        .map { it.toCharArray().toSet() }
        .windowed(3, 3)
        .flatMap { it.reduce { rucksack, commonCodes -> rucksack.intersect(commonCodes.toSet()) } }
        .sumOf { letterPriority(it) }
}

fun letterPriority(letter: Char): Int = when (letter) {
    in 'a'..'z' -> letter.code - 96
    in 'A'..'Z' -> letter.code - 38
    else -> throw RuntimeException("Illegal letter $letter")
}
