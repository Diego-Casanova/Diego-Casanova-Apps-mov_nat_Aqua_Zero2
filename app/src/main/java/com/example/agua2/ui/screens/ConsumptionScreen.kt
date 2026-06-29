package com.example.agua2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agua2.data.repository.ConsumptionSummary
import com.example.agua2.di.AppViewModelProvider
import com.example.agua2.ui.theme.AlertRed
import com.example.agua2.ui.theme.BluePrimary
import com.example.agua2.ui.viewmodels.ConsumptionUiState
import com.example.agua2.ui.viewmodels.ConsumptionViewModel

/**
 * Pantalla de Consumo detallado.
 * Implementa MAD Skills: StateFlow, collectAsStateWithLifecycle y State Hoisting.
 */
@Composable
fun ConsumptionScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStats: () -> Unit,
    viewModel: ConsumptionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConsumptionContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToStats = onNavigateToStats
    )
}

/**
 * Contenido sin estado para la pantalla de consumo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumptionContent(
    uiState: ConsumptionUiState,
    onNavigateBack: () -> Unit,
    onNavigateToStats: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Consumo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is ConsumptionUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ConsumptionUiState.Success -> {
                ConsumptionBody(
                    summary = uiState.summary,
                    onNavigateToStats = onNavigateToStats,
                    modifier = Modifier.padding(padding)
                )
            }
            is ConsumptionUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun ConsumptionBody(
    summary: ConsumptionSummary,
    onNavigateToStats: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        
        ConsumptionCard(label = "Consumo actual", value = summary.currentConsumption, icon = Icons.Default.WaterDrop)
        ConsumptionCard(label = "Consumo mensual", value = summary.monthlyConsumption, icon = Icons.Default.CalendarMonth)
        ConsumptionCard(label = "Total acumulado", value = summary.totalAccumulated, icon = Icons.Default.History)
        ConsumptionCard(label = "Precio por m³", value = summary.pricePerUnit, icon = Icons.Default.AttachMoney)
        ConsumptionCard(label = "Costo actual", value = summary.currentCost, icon = Icons.Default.Payments)
        ConsumptionCard(
            label = "Deuda pendiente", 
            value = summary.pendingDebt, 
            icon = Icons.Default.Warning,
            valueColor = if (summary.hasDebt) AlertRed else BluePrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNavigateToStats,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
        ) {
            Text("Ver Gráficas")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ConsumptionCard(
    label: String,
    value: String,
    icon: ImageVector,
    valueColor: androidx.compose.ui.graphics.Color = BluePrimary
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BluePrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = valueColor)
            }
        }
    }
}
