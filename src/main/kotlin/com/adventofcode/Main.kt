package com.adventofcode

class Main

fun readInput(path: String): List<String> = Main::class.java.getResourceAsStream(path)?.bufferedReader()?.readLines() ?: emptyList()
