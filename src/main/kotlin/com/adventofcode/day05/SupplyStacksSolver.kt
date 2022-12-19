package com.adventofcode.day05

import com.adventofcode.readInput

fun main() {
    println(solvePartOne())
    println(solvePartTwo())
}

data class Instruction(val numberOfCrates: Int, val fromStack: Int, val toStack: Int)

typealias SupplyStacks = ArrayDeque<Char>

fun solvePartOne(): String {
    val input = readInput("/day5-input")
    val supplyStacks = parseSupplyStacks(input.subList(0, 8))
    val instructions = input.drop(10).map { parseInstruction(it) }
    instructions.forEach { instruction ->
        for (i in instruction.numberOfCrates downTo 1) {
            val supplyId = supplyStacks[instruction.fromStack]?.removeFirst()
                ?: throw RuntimeException("Cannot collect supplies from empty stack ${instruction.fromStack}")
            supplyStacks[instruction.toStack]?.addFirst(supplyId)
        }
    }
    return supplyStacks.values.joinToString(separator = "") { it.first().toString() }
}

fun solvePartTwo(): String {
    val input = readInput("/day5-input")
    val supplyStacks = parseSupplyStacks(input.subList(0, 8))
    val instructions = input.drop(10).map { parseInstruction(it) }
    instructions.forEach { instruction ->
        for (i in instruction.numberOfCrates downTo 1) {
            val supplyId = supplyStacks[instruction.fromStack]?.removeAt(i-1)
                ?: throw RuntimeException("Cannot collect supplies from empty stack ${instruction.fromStack}")
            supplyStacks[instruction.toStack]?.addFirst(supplyId)
        }
    }
    return supplyStacks.values.joinToString(separator = "") { it.first().toString() }
}

private fun Int.toSupplyIdIndex() = this * 4 - 3

tailrec fun parseSupplyStacks(lines: List<String>, stacks: MutableMap<Int, SupplyStacks> = sortedMapOf()): Map<Int, SupplyStacks> {
    if (lines.isEmpty()) return stacks;
    fillStacksWithSupplyIds(lines.first(), stacks)
    return parseSupplyStacks(lines.drop(1), stacks)
}

private fun fillStacksWithSupplyIds(line: String, stacks: MutableMap<Int, SupplyStacks>) {
    for (i in 1..9) {
        val idx = i.toSupplyIdIndex()
        if (line.length >= idx && line[idx] != ' ') {
            stacks.getOrPut(i) { SupplyStacks() }.addLast(line[idx])
        }
    }
}

fun parseInstruction(line: String): Instruction {
    val instructionPattern = """move (\d+) from (\d) to (\d)""".toRegex()
    val groups = instructionPattern.matchEntire(line)?.groupValues ?: throw RuntimeException("Illegal instruction line: $line")
    return Instruction(groups[1].toInt(), groups[2].toInt(), groups[3].toInt())
}
