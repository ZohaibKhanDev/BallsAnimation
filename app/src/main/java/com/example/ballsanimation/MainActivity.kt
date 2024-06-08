package com.example.ballsanimation
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ballsanimation.ui.theme.BallsAnimationTheme
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BallsAnimationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BouncingBalls()
                }
            }
        }
    }
}

data class Ball(val id: Int, var isJumping: Boolean)

@Composable
fun BouncingBalls() {
    val balls = remember {
        List(10) { Ball(it, false) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        balls.forEach { ball ->
            BouncingBall(ball)
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun BouncingBall(ball: Ball) {
    var isJumping by remember { mutableStateOf(ball.isJumping) }
    val transition = rememberInfiniteTransition()

    val ballPosition by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val offsetY = if (isJumping) ballPosition * 600f else ballPosition * 300f
    val offsetX = (ball.id * 80f) % 800 // Adjusting x position based on the ball ID

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(100.dp)
            .clickable(
                onClickLabel = "Null",
                onClick = {
                    isJumping = !isJumping
                },
                indication = rememberRipple(color = Color.Transparent),
                interactionSource = MutableInteractionSource()
            )
            .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(
                    Random.nextInt(256),
                    Random.nextInt(256),
                    Random.nextInt(256)
                ),
                radius = 50f,
                center = androidx.compose.ui.geometry.Offset(
                    x = size.width / 2,
                    y = size.height / 2
                )
            )
        }
    }
}