package com.example.labweekthree.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labweekthree.R
import kotlinx.coroutines.delay
import kotlin.random.Random

private enum class SoalOneGameState {
    MainMenu,
    Waiting,
    ReadyToTap,
    TrialRecap,
    FinalResult
}

data class ResultCategory(val message: String, val color: Color,val image: Int)

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SoalOneView() {
    var gameState by remember { mutableStateOf(SoalOneGameState.MainMenu) }
    var currentTrial by remember { mutableIntStateOf(1) }

    val trialResults by remember { mutableStateOf(mutableListOf<Long>()) }
    var startTime by remember { mutableLongStateOf(0L) }
    var lastReactionTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(key1 = gameState, key2 = currentTrial) {
        if (gameState == SoalOneGameState.Waiting) {
            val randomDelay = Random.nextLong(500, 4500)
            delay(randomDelay)

            if (gameState == SoalOneGameState.Waiting) {
                startTime = System.currentTimeMillis()
                gameState = SoalOneGameState.ReadyToTap
            }
        }
    }

    fun resetGame() {
        currentTrial = 1
        trialResults.clear()
        gameState = SoalOneGameState.MainMenu
    }

    fun proceedToNextStep() {
        if (currentTrial < 3) {
            currentTrial++
            gameState = SoalOneGameState.TrialRecap
        } else {
            gameState = SoalOneGameState.FinalResult
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                when (gameState) {
                    SoalOneGameState.MainMenu -> {
                        gameState = SoalOneGameState.Waiting
                    }
                    SoalOneGameState.TrialRecap ->{
                        gameState = SoalOneGameState.MainMenu
                    }

                    SoalOneGameState.Waiting -> {
                        trialResults.add(-1L)
                        proceedToNextStep()
                    }

                    SoalOneGameState.ReadyToTap -> {
                        lastReactionTime = System.currentTimeMillis() - startTime
                        trialResults.add(lastReactionTime)
                        proceedToNextStep()
                    }

                    SoalOneGameState.FinalResult -> {
                        resetGame()
                    }
                }
            }
    ) {
        when (gameState) {
            SoalOneGameState.MainMenu -> MainMenuView(trialResults)
            SoalOneGameState.Waiting -> WaitingView()
            SoalOneGameState.ReadyToTap -> ReadyView()
            SoalOneGameState.TrialRecap -> {
                if (trialResults.last() == -1L) {
                    FailureView(trialResults)
                } else {
                    TrialRecapView(currentTrial - 1, trialResults.last(), trialResults)
                }
            }
            SoalOneGameState.FinalResult -> FinalResultView(trialResults)
        }
    }
}

@Composable
private fun MainMenuView(trialResults: List<Long>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF6FCDDC)),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Reaction Test", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Icon(Icons.Default.FlashOn, contentDescription = "Flash Icon", modifier = Modifier.size(120.dp), tint = Color.White)
        Text(text = "Click to Start", color = Color.White.copy(alpha = 0.8f), fontSize = 20.sp)

        if (trialResults.isNotEmpty()) {
            Spacer(Modifier.height(40.dp))
            TrialResultsCard(results = trialResults)
        }
    }
}

@Composable
private fun WaitingView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFD7D7D7)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Get Ready", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))
        Icon(Icons.Default.Warning, contentDescription = "Warning Icon", tint = Color.White, modifier = Modifier.size(120.dp))
        Spacer(Modifier.height(24.dp))
        Text(text = "Wait for the green light...", color = Color.White, fontSize = 20.sp)
        Text(text = "DON'T CLICK YET", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp, modifier = Modifier.padding(top = 14.dp))
    }
}

@Composable
private fun ReadyView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF2ECC71)),
        verticalArrangement =  Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "GO!", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Icon(Icons.AutoMirrored.Filled.DirectionsRun, contentDescription = "Warning Icon", tint = Color.White, modifier = Modifier.size(120.dp))
        Text(text = "CLICK NOW!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Normal)
        Text(text = "TAP AS FAST AS YOU CAN", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
    }
}

@Composable
private fun FailureView(trialResults: List<Long>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFB4141)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "FAIL!", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Icon(Icons.Default.ThumbDown, contentDescription = "Thumbs Down", tint = Color.White, modifier = Modifier.size(120.dp))
        Text(
            text = "You clicked too early.\nTRY TO READ THE RULE BRO",
            color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Normal, textAlign = TextAlign.Center
        )
        Text(text = "Click to continue...", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
        Spacer(Modifier.height(20.dp))
        TrialResultsCard(results = trialResults)
    }
}

@Composable
private fun TrialRecapView(trialNumber: Int, time: Long, trialResults: List<Long>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4CAF51)),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Trial $trialNumber Complete!", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Icon(Icons.Default.CheckCircle, contentDescription = "Warning Icon", tint = Color.White, modifier = Modifier.size(120.dp))
        Text(text = "Time: ${time}ms", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "Continue to Trial ${trialNumber + 1}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Light)
        TrialResultsCard(results = trialResults)
    }
}

@Composable
private fun FinalResultView(trialResults: List<Long>) {
    val validTrials = trialResults.filter { it != -1L }
    val averageTime = if (validTrials.isNotEmpty()) validTrials.average().toLong() else 0L

    val resultCategory = when {
        averageTime in 1..179 -> ResultCategory("DANG YOU ARE SO FAST BRO!", Color(0xFF2ECC71), R.drawable.aaron_jempol_atas)
        averageTime in 180.. 280 -> ResultCategory("YOUR REFLEX IS GOOD", Color(0xFF3498DB), R.drawable.arron_siap)
        averageTime in 281.. 450 -> ResultCategory("MEH LIKE OTHER PERSON", Color(0xFFF39C12), R.drawable.aaron_meh)
        else -> ResultCategory("YOU LIKE A SNAIL BRO", Color(0xFFE74C3C), R.drawable.aaron_jempol_bawah)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(resultCategory.color),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = resultCategory.message,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Image(
            painter = painterResource(id = resultCategory.image),
            contentDescription = "Image",
            modifier = Modifier
                .padding(top = 20.dp)
                .size(148.dp)
        )
        Text(text = "Average: ${averageTime}ms", color = Color.White, fontSize = 22.sp)
        Text(text = "Click to Start New Test", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
        Spacer(Modifier.height(10.dp))
        TrialResultsCard(results = trialResults, finalScore = averageTime)
    }
}

@Composable
private fun TrialResultsCard(results: List<Long>, finalScore: Long? = null) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Trial Results", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF176FD1))
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.width(200.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                (1..3).forEach { trialIndex ->
                    val result = results.getOrNull(trialIndex - 1)
                    val text = when {
                        result == null -> "-"
                        result == -1L -> "Fail"
                        else -> "${result}ms"
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$trialIndex",
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = text,
                            color = if (result == -1L) Color.Red else Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            if (finalScore != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Average Score",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "${finalScore}ms",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFFEE7C2B)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SoalOneViewPreview() {
    SoalOneView()
}