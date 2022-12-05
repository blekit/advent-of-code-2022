package com.adventofcode.day1

import com.adventofcode.readInput

data class Elf(val id: Int, var calories: Int)

fun solve(pathName: String): List<Elf> {
    val readLines = readInput("/day1-input")
    return readLines.flatMapIndexed { index, line ->
        when {
            index == 0 || index == readLines.lastIndex -> listOf(index)
            line.isBlank() -> listOf(index - 1, index + 1)
            else -> emptyList()
        }
    }
        .windowed(size = 2, step = 2) { (from, to) -> readLines.slice(from..to) }
        .map { elvenSnacks -> elvenSnacks.sumOf { it.toInt() } }
        .mapIndexed { index, elvenCalories -> Elf(index + 1, elvenCalories) }
        .sortedByDescending { it.calories }
}

fun main() {
    val sortedElves = solve("/elf-list")
    println(sortedElves.first().calories)
    println(sortedElves.subList(0, 3).sumOf { it.calories })
}
