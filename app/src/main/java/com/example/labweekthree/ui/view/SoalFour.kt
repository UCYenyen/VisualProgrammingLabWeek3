package com.example.labweekthree.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.max

private enum class GameState {
    MainMenu,
    Running,
    GameOver
}

private enum class Choice(val emoji: String) {
    ROCK("✊"),
    PAPER("✋"),
    SCISSORS("✌️");
}

private enum class RoundResult(val displayText: String) {
    WIN("You Win!"),
    LOSE("You Lose"),
    DRAW("It's a Draw")
}

private fun determineWinner(playerChoice: Choice, cpuChoice: Choice): RoundResult {
    if (playerChoice == cpuChoice) {
        return RoundResult.DRAW
    }

    return when (playerChoice) {
        Choice.ROCK -> if (cpuChoice == Choice.SCISSORS) RoundResult.WIN else RoundResult.LOSE
        Choice.PAPER -> if (cpuChoice == Choice.ROCK) RoundResult.WIN else RoundResult.LOSE
        Choice.SCISSORS -> if (cpuChoice == Choice.PAPER) RoundResult.WIN else RoundResult.LOSE
    }
}

@Composable
fun SoalFourView() {
    var gameState by remember { mutableStateOf(GameState.MainMenu) }
    var playerScore by rememberSaveable { mutableIntStateOf(0) }
    var cpuScore by rememberSaveable { mutableIntStateOf(0) }
    var bestScore by rememberSaveable { mutableIntStateOf(0) }
    val winTarget = 3

    when (gameState) {
        GameState.MainMenu -> {
            MainMenu(
                onStartClick = {
                    playerScore = 0
                    cpuScore = 0
                    gameState = GameState.Running
                }
            )
        }
        GameState.Running -> {
            MainGame(
                playerScore = playerScore,
                cpuScore = cpuScore,
                onScoreUpdate = { pScore, cScore ->
                    playerScore = pScore
                    cpuScore = cScore
                    if (playerScore >= winTarget || cpuScore >= winTarget) {
                        bestScore = max(bestScore, playerScore)
                        gameState = GameState.GameOver
                    }
                }
            )
        }
        GameState.GameOver -> {
            GameOverScreen(
                playerScore = playerScore,
                cpuScore = cpuScore,
                bestScore = bestScore,
                onPlayAgain = {
                    playerScore = 0
                    cpuScore = 0
                    gameState = GameState.Running
                },
                onExit = {
                    playerScore = 0
                    cpuScore = 0
                    gameState = GameState.MainMenu
                }
            )
        }
    }
}

@Composable
fun ScoreHeader(playerScore: Int, cpuScore: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("🧑 $playerScore - $cpuScore 🤖", fontSize = 24.sp, fontWeight = FontWeight.Light, textAlign = TextAlign.Center)
        Text("Best of 5", fontSize = 18.sp, fontWeight = FontWeight.Light, textAlign = TextAlign.Center)
    }
}

@Composable
fun MainGame(playerScore: Int, cpuScore: Int, onScoreUpdate: (Int, Int) -> Unit) {
    var playerChoice by remember { mutableStateOf<Choice?>(null) }
    var cpuChoice by remember { mutableStateOf<Choice?>(null) }
    var roundResult by remember { mutableStateOf<RoundResult?>(null) }
    var isRevealing by remember { mutableStateOf(false) }

    val choices = remember(isRevealing) { Choice.entries.shuffled() }

    if (isRevealing) {
        LaunchedEffect(Unit) {
            delay(1000)
            isRevealing = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 248.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScoreHeader(playerScore, cpuScore)
        Spacer(Modifier.weight(1f))

        if (isRevealing) {
            Text(roundResult?.displayText ?: "", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(playerChoice?.emoji ?: "❔", fontSize = 64.sp)
                Text("VS", fontSize = 32.sp)
                Text(cpuChoice?.emoji ?: "❔", fontSize = 64.sp)
            }
        } else {
            // Pick Screen UI
            Text("Pick your move!", fontSize = 18.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("❔", fontSize = 64.sp)
                Text(" VS ", fontSize = 32.sp)
                Text("❔", fontSize = 64.sp)
            }
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                choices.forEach { choice ->
                    Button(
                        onClick = {
                            val pChoice = choice
                            val cChoice = Choice.entries.toTypedArray().random()
                            val result = determineWinner(pChoice, cChoice)

                            playerChoice = pChoice
                            cpuChoice = cChoice
                            roundResult = result
                            isRevealing = true

                            var newPlayerScore = playerScore
                            var newCpuScore = cpuScore
                            if (result == RoundResult.WIN) newPlayerScore++
                            if (result == RoundResult.LOSE) newCpuScore++
                            onScoreUpdate(newPlayerScore, newCpuScore)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBAC8D1),
                            contentColor = Color.Black,
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                    ) {
                        Text("${choice.emoji} ${choice.name}", fontSize = 16.sp)
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun MainMenu(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 248.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScoreHeader(playerScore = 0, cpuScore = 0)
        Spacer(Modifier.weight(1f))
        Text(
            "Rock • Paper • Scissors",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(64.dp))
        Button(
            onClick = onStartClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBAC8D1),
                contentColor = Color.Black,
            ),
            modifier = Modifier.size(width = 200.dp, height = 60.dp)
        ) {
            Text("Start", fontSize = 18.sp)
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun GameOverScreen(
    playerScore: Int,
    cpuScore: Int,
    bestScore: Int,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit
) {
    val matchResult = if (playerScore > cpuScore) "You Win the Match!" else "You Lose the Match"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 248.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(matchResult, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))
        Text(
            "Final Score: $playerScore - $cpuScore",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Best Score: $bestScore",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            color = Color.Gray
        )
        Spacer(Modifier.height(48.dp))
        Button(
            onClick = onPlayAgain,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBAC8D1),
                contentColor = Color.Black
            ),
            modifier = Modifier.size(width = 200.dp, height = 50.dp)
        ) {
            Text("Restart")
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onExit,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBAC8D1),
                contentColor = Color.Black
            ),
            modifier = Modifier.size(width = 200.dp, height = 50.dp)
        ) {
            Text("Exit")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SoalFourPreview() {
    SoalFourView()
}