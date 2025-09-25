import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import kotlin.random.Random
import kotlin.math.max

private enum class GameMode {
    COLOR,
    TEXT
}

private enum class SoalThreeGameState {
    MainMenu,
    CountDown,
    Running,
    GameOver
}

private enum class ColorName(val colorValue: Color) {
    RED(Color.Red),
    BLUE(Color.Blue),
    GREEN(Color.Green),
    YELLOW(Color(0xFFFFCC4A)),
    BLACK(Color.Black),
    PURPLE(Color(0xFF800080)),
    ORANGE(Color(0xFFFF6600)),
}


@Composable
fun SoalThreeView() {
    var gameState by remember { mutableStateOf(SoalThreeGameState.MainMenu) }
    var finalScore by remember { mutableIntStateOf(0) }
    var bestScore by rememberSaveable { mutableIntStateOf(0) }

    when (gameState) {
        SoalThreeGameState.MainMenu -> {
            MainMenu({ gameState = SoalThreeGameState.CountDown })
        }

        SoalThreeGameState.CountDown -> {
            CountDown(
                onCountdownFinished = { gameState = SoalThreeGameState.Running }
            )
        }

        SoalThreeGameState.Running -> {
            MainGame(
                onGameOver = { score ->
                    finalScore = score
                    bestScore = max(bestScore, score)
                    gameState = SoalThreeGameState.GameOver
                }
            )
        }

        SoalThreeGameState.GameOver -> {
            GameOverScreen(
                score = finalScore,
                bestScore = bestScore,
                onPlayAgain = { gameState = SoalThreeGameState.Running },
                onExit = { gameState = SoalThreeGameState.MainMenu }
            )
        }
    }
}

@Composable
fun CountDown(onCountdownFinished: () -> Unit) {
    var countdownText by remember { mutableStateOf("3") }

    LaunchedEffect(key1 = true) {
        for (i in 3 downTo 1) {
            countdownText = i.toString()
            delay(700L)
        }

        countdownText = "Start!"
        delay(700L)

        onCountdownFinished()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = countdownText,
            textAlign = TextAlign.Center,
            fontSize = 72.sp
        )
    }
}

@Composable
fun MainGame(onGameOver: (score: Int) -> Unit) {
    var mode by remember { mutableStateOf(GameMode.COLOR) }
    var timeLeft by remember { mutableLongStateOf(5000L) }
    var rightAnswers by remember { mutableIntStateOf(0) }
    var wrongAnswers by remember { mutableIntStateOf(0) }

    var questionWord by remember { mutableStateOf(ColorName.RED) }
    var questionInk by remember { mutableStateOf(ColorName.BLUE) }
    var isWordOnLeft by remember { mutableStateOf(true) }

    val generateNewRound = {
        var newWord: ColorName
        var newInk: ColorName
        do {
            newWord = ColorName.entries.random()
            newInk = ColorName.entries.random()
        } while (newWord == newInk)

        questionWord = newWord
        questionInk = newInk
        mode = if (Random.nextBoolean()) GameMode.COLOR else GameMode.TEXT
        isWordOnLeft = Random.nextBoolean()
        timeLeft = 5000L
    }

    val checkAnswer = { chosenAnswer: ColorName ->
        val correctAnswer = if (mode == GameMode.COLOR) questionInk else questionWord
        if (chosenAnswer == correctAnswer) {
            rightAnswers++
        } else {
            wrongAnswers++
        }

        if (wrongAnswers >= 3) {
            onGameOver(rightAnswers)
        } else {
            generateNewRound()
        }
    }

    LaunchedEffect(Unit) {
        generateNewRound()
        rightAnswers = 0
        wrongAnswers = 0
    }

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0L) {
            delay(100L)
            timeLeft -= 100L
        } else {
            wrongAnswers++
            if (wrongAnswers >= 3) {
                onGameOver(rightAnswers)
            } else {
                generateNewRound()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(128.dp, Alignment.CenterVertically)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Mode : ${mode.name}",
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        5.dp,
                        Alignment.CenterHorizontally
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckBox,
                        contentDescription = "Right Icon",
                        tint = Color(0xFF80B046),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        "$rightAnswers",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        5.dp,
                        Alignment.CenterHorizontally
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Wrong Icon",
                        tint = Color(0xFFE8462D),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        "$wrongAnswers/3",
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Time Left : ${timeLeft / 1000} s",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 24.sp
            )
            Text(
                text = questionWord.name,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 48.sp,
                color = questionInk.colorValue,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (isWordOnLeft) {
                Button(
                    onClick = { checkAnswer(questionWord) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBAC8D1),
                        contentColor = Color.Black
                    )
                ) {
                    Text(questionWord.name)
                }

                Button(
                    onClick = { checkAnswer(questionInk) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBAC8D1),
                        contentColor = Color.Black
                    )
                ) {
                    Text(questionInk.name)
                }
            } else {
                Button(
                    onClick = { checkAnswer(questionInk) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBAC8D1),
                        contentColor = Color.Black
                    )
                ) {
                    Text(questionInk.name)
                }

                Button(
                    onClick = { checkAnswer(questionWord) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBAC8D1),
                        contentColor = Color.Black
                    )
                ) {
                    Text(questionWord.name)
                }
            }
        }
    }
}

@Composable
fun MainMenu(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {
        Text(
            "Welcome \n to \n Color Word Matching",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        Button(
            onClick = onStartClick, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBAC8D1),
                contentColor = Color.Black,
            )
        ) {
            Text("Start Game")
        }
    }
}

@Composable
fun GameOverScreen(score: Int, bestScore: Int, onPlayAgain: () -> Unit, onExit: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Game Over!", fontSize = 32.sp, fontWeight = FontWeight.Light)
        Spacer(Modifier.height(48.dp))
        Text(
            "You're Score \n $score",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Best Score \n $bestScore",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
        Spacer(Modifier.height(48.dp))
        Button(
            onClick = onPlayAgain, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBAC8D1),
                contentColor = Color.Black
            )
        ) {
            Text("Restart Game")
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onExit, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBAC8D1),
                contentColor = Color.Black
            )
        ) {
            Text("Exit")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SoalThreePreview() {
    SoalThreeView()
}