package com.example.agua2.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agua2.ui.components.WaterDropIcon
import com.example.agua2.ui.theme.BluePrimary
import com.example.agua2.ui.theme.BlueSecondary
import com.example.agua2.ui.theme.WhiteSemiTransparent
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToAccess: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleAlpha"
    )
    val rippleScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleScale"
    )

    LaunchedEffect(Unit) {
        delay(2500)
        onNavigateToAccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BluePrimary, BlueSecondary)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(rippleScale)
                .alpha(rippleAlpha)
                .background(Color.White.copy(alpha = 0.3f), CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WaterDropIcon(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Agua Zero",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Bienvenido",
                color = WhiteSemiTransparent,
                fontSize = 18.sp
            )
        }
    }
}
