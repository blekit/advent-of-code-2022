package com.adventofcode.day5

import com.adventofcode.readInput

fun main() {
    println(solvePartOne())
    println(solvePartTwo())
}

data class Instruction(val numberOfCrates: Int, val from: Int, val to: Int)

typealias SupplyStacks = ArrayDeque<Char>


fun solvePartOne(): String {
    val input = readInput("/day5-input")
    val parsedStacks = parseSupplyStacks(input.subList(0, 8))
    val instructions = input.drop(10).map { parseInstruction(it) }
    instructions.forEach { instruction ->
        for (i in instruction.numberOfCrates downTo 1) {
            val supplyId = parsedStacks[instruction.from]?.removeFirst()
                ?: throw RuntimeException("Cannot collect supplies from empty stack ${instruction.from}")
            parsedStacks[instruction.to]?.addFirst(supplyId)
        }
    }
    return parsedStacks.values.joinToString(separator = "") { it.first().toString() }
}

fun solvePartTwo(): String {
    val input = readInput("/day5-input")
    val parsedStacks = parseSupplyStacks(input.subList(0, 8))
    val instructions = input.drop(10).map { parseInstruction(it) }
    instructions.forEach { instruction ->
        for (i in instruction.numberOfCrates downTo 1) {
            val supplyId = parsedStacks[instruction.from]?.removeAt(i-1)
                ?: throw RuntimeException("Cannot collect supplies from empty stack ${instruction.from}")
            parsedStacks[instruction.to]?.addFirst(supplyId)
        }
    }
    return parsedStacks.values.joinToString(separator = "") { it.first().toString() }
}

private fun Int.toSupplyIdIndex() = this * 4 - 3

tailrec fun parseSupplyStacks(lines: List<String>, stacks: MutableMap<Int, SupplyStacks> = sortedMapOf()): Map<Int, SupplyStacks> {
    if (lines.isEmpty()) return stacks;
    val line = lines.first()
    fillStacksWithSupplyIds(line, stacks)
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
