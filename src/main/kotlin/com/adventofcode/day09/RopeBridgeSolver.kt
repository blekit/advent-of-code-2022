package com.adventofcode.day09

import com.adventofcode.readInput
import kotlin.math.abs

fun main() {
    println(solvePartOne())
    println(solvePartTwo())
}

fun solvePartOne(): Int {
    return followMovementsOf(Rope(2))
}

fun solvePartTwo(): Int {
    return followMovementsOf(Rope(10))
}

private fun followMovementsOf(rope: Rope): Int {
    val result = mutableSetOf<Coordinates>()
    readInput("/day9-input")
        .map { it.split(" ") }
        .map { Direction.valueOf(it[0]) to it[1].toInt() }
        .forEach {
            for (i in 0 until it.second) {
                rope.moveIn(it.first)
                result.add(rope.tailKnot)
            }
        }
    return result.size
}


data class Coordinates(val x: Int, val y: Int) {
    fun follow(other: Coordinates): Coordinates {
        if (this == other) return this
        if (abs(x - other.x) <= 1 && abs(y - other.y) <= 1) return this
        if (abs(x - other.x) > 1 && abs(y - other.y) > 1) {
            return Coordinates((x + other.x) / 2, (y + other.y) / 2)

        }
        if (abs(x - other.x) > 1) {
            return Coordinates((x + other.x) / 2, other.y)
        }
        if (abs(y - other.y) > 1) {
            return Coordinates(other.x, (y + other.y) / 2)
        }
        throw RuntimeException("[$x, $y] cannot follow to [$other.x, $other.y]")
    }
}

enum class Direction { U, D, L, R }

class Rope(ropeLength: Int) {

    private var knots: MutableList<Coordinates> = mutableListOf()

    init {
        IntRange(1, ropeLength - 1).forEach { _ -> knots.add(Coordinates(0, 0)) }
    }

    val tailKnot
        get() = knots.last()

    private var headKnot = Coordinates(0, 0)

    fun moveIn(direction: Direction) {
        headKnot = when (direction) {
            Direction.R -> Coordinates(headKnot.x + 1, headKnot.y)
            Direction.L -> Coordinates(headKnot.x - 1, headKnot.y)
            Direction.U -> Coordinates(headKnot.x, headKnot.y + 1)
            Direction.D -> Coordinates(headKnot.x, headKnot.y - 1)
        }
        knots.forEachIndexed { index, knot ->
            if (index == 0) knots[index] = knot.follow(headKnot)
            else knots[index] = knot.follow(knots[index - 1])
        }
    }
}
