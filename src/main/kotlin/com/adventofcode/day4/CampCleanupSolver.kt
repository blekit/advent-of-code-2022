package com.adventofcode.day4

import com.adventofcode.readInput

fun main() {
    println(solvePartOne())
    println(solvePartTwo())
}

fun solvePartOne(): Int {
    return readInput("/day4-input")
        .map { it.split(",") }
        .map { it[0].toRange() to it[1].toRange() }
        .count { it.first.contains(it.second) || it.second.contains(it.first) }
}

fun solvePartTwo(): Int {
    return readInput("/day4-input")
        .map { it.split(",") }
        .map { it[0].toRange() to it[1].toRange() }
        .count { it.first.overlaps(it.second) || it.second.overlaps(it.first)}
}

fun String.toRange(): IntRange {
    val rangeEnds = this.split("-")
    return rangeEnds[0].toInt()..rangeEnds[1].toInt()
}

private fun IntRange.contains(other: IntRange): Boolean = this.contains(other.first) && this.contains(other.last)

private fun IntRange.overlaps(other: IntRange): Boolean = this.contains(other.first) || this.contains(other.last)
