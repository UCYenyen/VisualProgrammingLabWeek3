package com.example.labweekthree.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labweekthree.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun SoalTwoView() {
    var coins by remember { mutableIntStateOf(0) }
    var coinPerClick by remember { mutableFloatStateOf(1f) }
    var upgradeCost by remember { mutableIntStateOf(10) }
    var isCatPressed by remember { mutableStateOf(false) }

    val decimalFormat = DecimalFormat("#.#")
    val scope = rememberCoroutineScope()

    fun performUpgrade() {
        if (coins >= upgradeCost) {
            coins -= upgradeCost
            upgradeCost *= 2
            coinPerClick *= 1.5f
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background_aneh),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.25f))
        ) {}
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = Color.White.copy(alpha = 0.5f))
                    .width(250.dp)
                    .padding(vertical = 16.dp)
            ) {
                Text(text = "Your Coins", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = coins.toString(), color = Color.Green, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                Text(text = "${decimalFormat.format(coinPerClick)} coins per tap", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Tap The Cat!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Image(
                    painter = painterResource(
                        if (isCatPressed) R.drawable.kucing_mangap else R.drawable.kucing_mingkem
                    ),
                    contentDescription = "Tap the Cat",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            coins += coinPerClick.toInt()
                            scope.launch {
                                isCatPressed = true
                                delay(100)
                                isCatPressed = false
                            }
                        }
                )
                Text(
                    text = if (isCatPressed) "Meow!" else "Purr~",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = Color.White)
                    .width(325.dp)
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                Text(text = "Give Me Your Coin", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Next upgrade: +${decimalFormat.format(coinPerClick * 1.5f)} coins per tap",
                    color = Color.Black.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )
                val canUpgrade = coins >= upgradeCost
                Button(
                    onClick = { performUpgrade() },
                    enabled = canUpgrade,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = if (canUpgrade) "Pay for $upgradeCost coins" else "Find ${upgradeCost - coins} more coins",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun SoalTwoPreview() {
    SoalTwoView()
}