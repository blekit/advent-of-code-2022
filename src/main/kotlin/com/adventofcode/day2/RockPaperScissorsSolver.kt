package com.adventofcode.day2

import com.adventofcode.day2.Outcome.*
import com.adventofcode.day2.PlayerChoices.*
import com.adventofcode.readInput


enum class Outcome(val score: Int) {
    LOSE(0), DRAW(3), WIN(6);
}

enum class PlayerChoices(val score: Int) {
    ROCK(1) {
        override fun beats() = SCISSORS

        override fun losesTo() = PAPER

    },
    PAPER(2) {
        override fun beats() = ROCK

        override fun losesTo() = SCISSORS
    },
    SCISSORS(3) {
        override fun beats() = PAPER

        override fun losesTo() = ROCK
    };

    abstract fun beats(): PlayerChoices
    abstract fun losesTo(): PlayerChoices
}

fun pickShapeAchievingVersus(desiredOutcome: Outcome, otherPlayersChoice: PlayerChoices) = when (desiredOutcome) {
    LOSE -> otherPlayersChoice.beats()
    WIN -> otherPlayersChoice.losesTo()
    DRAW -> otherPlayersChoice
}

fun parseOutcome(id: String) = when (id) {
    "X" -> LOSE
    "Y" -> DRAW
    "Z" -> WIN
    else -> throw RuntimeException("Illegal outcome $id")
}

fun parseChoice(id: String) = when (id) {
    "A" -> ROCK
    "B" -> PAPER
    "C" -> SCISSORS
    else -> throw RuntimeException("Illegal player choice: $id")
}

data class RockPaperScissorsRound(val otherPlayersChoice: PlayerChoices, val desiredOutcome: Outcome) {
    fun score(): Int = desiredOutcome.score + pickShapeAchievingVersus(desiredOutcome, otherPlayersChoice).score
}

fun parseRound(line: String): RockPaperScissorsRound {
    val parsedLine = line.split(" ")
    return RockPaperScissorsRound(parseChoice(parsedLine[0]), parseOutcome(parsedLine[1]))
}

fun solve(): Int {
    return readInput("/day2-input").map { parseRound(it) }.sumOf { it.score() }

}

fun main() {
    println(solve())
}
