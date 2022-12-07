package com.adventofcode.day7

import com.adventofcode.readInput

fun main() {
    println(solvePartOne())
    println(solvePartTwo())
}

private fun String.isCommand(): Boolean = this.startsWith("$")

private fun String.isFile(): Boolean = this.matches(Regex("""\d+.*"""))

private fun String.fileSize(): Long = this.split(" ").first().toLong()

private fun String.isDirectory(): Boolean = this.startsWith("dir")

private fun String.getDirectoryPath(parentPath: String) = "$parentPath/${this.split(" ").last()}"

class InputProcessor(
    private var currentDirectory: Directory = Directory("/"),
    val allDirectories: MutableMap<String, Directory> = mutableMapOf(currentDirectory.path to currentDirectory)
) {
    fun processLine(line: String) {
        when {
            line.isFile() -> currentDirectory.addFile(line.fileSize())
            line.isDirectory() -> {
                val directory = Directory(line.getDirectoryPath(currentDirectory.path))
                allDirectories[directory.path] = directory
                currentDirectory.addChildDirectory(directory)
            }

            line.isCommand() -> {
                when (line) {
                    "$ cd .." -> currentDirectory = allDirectories[currentDirectory?.parentPath]!!
                    "$ cd /" -> currentDirectory = allDirectories["/"]!!
                    "$ ls" -> {}
                    else -> currentDirectory = allDirectories["${currentDirectory?.path}/${line.split(" ").last()}"]!!
                }
            }

            else -> throw RuntimeException("Illegal instruction: $line")
        }
    }
}

class Directory(
    val path: String,
    private var fileSize: Long = 0,
    private val childDirectories: MutableList<Directory> = mutableListOf(),
) {

    val parentPath: String = path.substring(0, path.lastIndexOf('/'))

    val directorySize: Long
        get() = this.fileSize + childDirectories.sumOf { it.directorySize }

    fun addFile(fileSize: Long) {
        this.fileSize += fileSize
    }

    fun addChildDirectory(directory: Directory) {
        this.childDirectories.add(directory)
    }

    override fun toString(): String = "$path: $directorySize"
}

fun solvePartOne(): Long {
    val input = readInput("/day7-input")
    val processor = InputProcessor()
    input.forEach { processor.processLine(it) }
    return processor.allDirectories.filter { (_, v) -> v.directorySize <= 100000 }
        .values.sumOf { it.directorySize }
}

fun solvePartTwo(): Long {
    val input = readInput("/day7-input")
    val processor = InputProcessor()
    input.forEach { processor.processLine(it) }
    val diskSize = 70000000
    val spaceNeeded = 30000000
    val unusedSpace = diskSize - processor.allDirectories["/"]!!.directorySize
    val spaceToFree = spaceNeeded - unusedSpace
    return processor.allDirectories.map { it.value.directorySize }.filter { it >= spaceToFree }.min()
}
