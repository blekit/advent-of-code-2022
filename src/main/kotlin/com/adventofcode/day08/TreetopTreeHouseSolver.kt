package com.adventofcode.day08

import com.adventofcode.readInput

fun main() {
    println(solvePartOne())
    println(solvePartTwo())
}

fun solvePartOne(): Int {
    val treeRows = readInput("/day8-input")
    val numberOfTreesInARow = treeRows[0].length
    val forest = HighestTreesInForest(treeRows)
    var numberOfVisibleTrees = treeRows.size * 2 + numberOfTreesInARow * 2 - 4
    for (rowIndex in 1 until treeRows.size - 1) {
        for (columnIndex in 1 until numberOfTreesInARow - 1) {
            val treeHeight = treeRows[rowIndex][columnIndex]
            if (forest.shortestTreeOnBlockingPathFromSomeDirection(rowIndex, columnIndex) < treeHeight) {
                numberOfVisibleTrees++
            }
        }
    }
    return numberOfVisibleTrees
}

fun solvePartTwo(): Int {
    val treeRows = readInput("/day8-input")
    val treeColumns = treeRows.transpose()
    val result = mutableListOf<ScenicView>()
    for (i in treeRows.indices) {
        for (j in treeColumns.indices) {
            val currentTreeHeight = treeRows[i][j]
            val scenicView = ScenicView(
                treeHouseHeight = currentTreeHeight,
                treesInTheNorth = treeColumns[j].take(i).reversed(),
                treesInTheSouth = treeColumns[j].drop(i + 1),
                treesInTheWest = treeRows[i].take(j).reversed(),
                treesInTheEast = treeRows[i].drop(j + 1)
            )
            result.add(scenicView)
        }
    }
    return result.maxOfOrNull { it.rating() } ?: throw RuntimeException("No tree to calculate rating for")
}

private fun List<String>.transpose(): List<String> {
    val transposed = Array<String>(this.size) { _ -> "" }
    val numberOfColumns = this[0].length
    for (rowIndex in indices) {
        for (columnIndex in 0 until numberOfColumns) {
            transposed[rowIndex] = transposed[rowIndex] + this[columnIndex][rowIndex]
        }

    }
    return transposed.toList()
}

private fun findHighestTree(trees: List<String>): MutableList<String> {
    val result = mutableListOf<String>(trees[0])
    for (rowIndex in 1 until trees.size) {
        val highestTrees = result[rowIndex - 1].toCharArray()
        for (treeIndex in 0 until trees[rowIndex].length) {
            highestTrees[treeIndex] = maxOf(highestTrees[treeIndex], trees[rowIndex - 1][treeIndex])
        }
        result.add(highestTrees.joinToString(""))
    }
    return result
}

private class HighestTreesInForest(treeRows: List<String>) {
    private var highestFromNorth: List<String>
    private var highestFromSouth: List<String>
    private val highestFromWest: List<String>
    private val highestFromEast: List<String>

    init {
        val treeColumns = treeRows.transpose()
        highestFromNorth = findHighestTree(treeRows)
        highestFromSouth = findHighestTree(treeRows.reversed()).reversed()
        highestFromWest = findHighestTree(treeColumns)
        highestFromEast = findHighestTree(treeColumns.reversed()).reversed()
    }

    fun shortestTreeOnBlockingPathFromSomeDirection(x: Int, y: Int): Char {
        return minOf(
            highestFromNorth[x][y], highestFromSouth[x][y], highestFromWest[y][x], highestFromEast[y][x]
        )
    }
}

private class ScenicView(
    val treeHouseHeight: Char,
    val treesInTheNorth: String,
    val treesInTheSouth: String,
    val treesInTheWest: String,
    val treesInTheEast: String
) {
    fun rating() = calculateVisibleTrees(treeHouseHeight, treesInTheNorth) *
            calculateVisibleTrees(treeHouseHeight, treesInTheSouth) *
            calculateVisibleTrees(treeHouseHeight, treesInTheWest) *
            calculateVisibleTrees(treeHouseHeight, treesInTheEast)
}

private fun calculateVisibleTrees(treeHouseHeight: Char, treesInLine: String): Int {
    if (treesInLine.isBlank()) return 0
    return calculateVisibleTrees(treeHouseHeight, treesInLine, 0)
}

private tailrec fun calculateVisibleTrees(treeHouseHeight: Char, treesInLine: String, visibleTrees: Int): Int {
    if (treesInLine.isBlank()) return visibleTrees
    if (treesInLine[0] >= treeHouseHeight) return visibleTrees + 1
    return calculateVisibleTrees(treeHouseHeight, treesInLine.drop(1), visibleTrees + 1)
}
