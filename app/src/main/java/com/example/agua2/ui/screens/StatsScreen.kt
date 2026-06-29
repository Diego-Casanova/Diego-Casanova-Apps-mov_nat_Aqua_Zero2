package com.example.agua2.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agua2.di.AppViewModelProvider
import com.example.agua2.ui.theme.BluePrimary
import com.example.agua2.ui.theme.BlueSecondary
import com.example.agua2.ui.viewmodels.StatsUiState
import com.example.agua2.ui.viewmodels.StatsViewModel

/**
 * Pantalla de Estadísticas con State Hoisting.
 */
@Composable
fun StatsScreen(
    onNavigateBack: () -> Unit,
    viewModel: StatsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StatsContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsContent(
    uiState: StatsUiState,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is StatsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is StatsUiState.Success -> {
                StatsBody(
                    uiState = uiState,
                    modifier = Modifier.padding(padding)
                )
            }
            is StatsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun StatsBody(
    uiState: StatsUiState.Success,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ChartCard(title = "Consumo últimos 6 meses (m³)") {
            BarChart(
                data = uiState.consumptionData,
                labels = uiState.labels,
                color = BluePrimary
            )
        }

        ChartCard(title = "Gastos últimos 6 meses ($)") {
            BarChart(
                data = uiState.costData,
                labels = uiState.labels,
                color = BlueSecondary
            )
        }

        ChartCard(title = "Comparación Mensual vs Total") {
            Box(modifier = Modifier.height(150.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Mensual: ${uiState.monthlyValue} m³", fontWeight = FontWeight.Bold, color = BluePrimary)
                    LinearProgressIndicator(
                        progress = { uiState.monthlyValue / uiState.totalValue },
                        modifier = Modifier.fillMaxWidth().height(12.dp).padding(vertical = 8.dp),
                        color = BluePrimary,
                        trackColor = BlueSecondary.copy(alpha = 0.2f)
                    )
                    Text("Total Acumulado: ${uiState.totalValue} m³", fontSize = 12.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ChartCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BluePrimary)
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun BarChart(data: List<Float>, labels: List<String>, color: Color) {
    val maxValue = data.maxOrNull() ?: 1f
    
    Column {
        Canvas(modifier = Modifier.height(150.dp).fillMaxWidth()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barWidth = (canvasWidth / (data.size * 2))
            val spacing = barWidth
            
            data.forEachIndexed { index, value ->
                val barHeight = (value / maxValue) * canvasHeight
                drawRect(
                    color = color,
                    topLeft = Offset(
                        x = spacing / 2 + index * (barWidth + spacing),
                        y = canvasHeight - barHeight
                    ),
                    size = Size(barWidth, barHeight)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            labels.forEach { label ->
                Text(text = label, fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}
