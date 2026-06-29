package com.example.agua2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agua2.ui.components.WaterDropIcon
import com.example.agua2.ui.theme.BluePrimary

@Composable
fun AccessScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WaterDropIcon(
                modifier = Modifier.size(80.dp),
                color = BluePrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Agua Zero",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text("Iniciar Sesión")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth(),
                border = androidx.compose.foundation.BorderStroke(1.dp, BluePrimary)
            ) {
                Text("Registrarse", color = BluePrimary)
            }
        }
    }
}
