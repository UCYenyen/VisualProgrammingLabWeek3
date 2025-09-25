package com.example.labweekthree.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class GameMode {
    Color,
    Text
}

private enum class SoalThreeGameState {
    MainMenu,
    CountDown,
    Running,
    GameOver
}

private enum class ColorName {
    red,
    blue,
    green,
    yellow,
    black,
    white,
    gray,
    purple,
    orange,
    brown
}


@Composable
fun SoalThreeView() {
    var gameState by remember { mutableStateOf(SoalThreeGameState.MainMenu) }
    var startTime by remember { mutableLongStateOf(0L) }

    when (gameState) {
        SoalThreeGameState.MainMenu -> {
            MainMenu({ gameState = SoalThreeGameState.CountDown })
        }

        SoalThreeGameState.CountDown -> {
            CountDown()
        }

        SoalThreeGameState.Running -> TODO()
        SoalThreeGameState.GameOver -> TODO()
    }
}

@Composable
fun CountDown() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "1",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}

@Composable
fun MainMenu(onStartClick: () -> Unit) { // It accepts a function as a parameter
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
                containerColor = Color.Gray,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.DarkGray
            )
        ) {
            Text("Start Game")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SoalThreePreview() {
    SoalThreeView()
}