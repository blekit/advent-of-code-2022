package com.adventofcode.day3

import readInput

fun main() {
    println(solvePartOne())
    println(solvePartTwo())
}

fun String.toSetOfCharacters(): Set<Char> = this.toCharArray().toSet()

fun String.splitInHalf(): Pair<Set<Char>, Set<Char>> {
    val splitted = this.chunked(this.length / 2)
    return splitted[0].toSetOfCharacters() to splitted[1].toSetOfCharacters()
}

fun solvePartOne(): Int {
    return readInput("/day3-input")
        .map { it.splitInHalf() }
        .flatMap { it.first.intersect(it.second) }
        .sumOf { letterPriority(it) }
}

fun solvePartTwo(): Int {
    return readInput("/day3-input")
        .map { it.toSetOfCharacters() }
        .windowed(3, 3)
        .flatMap { it.reduce(Set<Char>::intersect) }
        .sumOf { letterPriority(it) }
}

fun letterPriority(letter: Char): Int = when (letter) {
    in 'a'..'z' -> letter.code - 96
    in 'A'..'Z' -> letter.code - 38
    else -> throw RuntimeException("Illegal letter $letter")
}
