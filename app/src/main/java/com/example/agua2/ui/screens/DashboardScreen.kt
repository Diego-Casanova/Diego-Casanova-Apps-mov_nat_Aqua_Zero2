package com.example.agua2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agua2.data.repository.ConsumptionRecord
import com.example.agua2.di.AppViewModelProvider
import com.example.agua2.ui.viewmodels.DashboardUiState
import com.example.agua2.ui.viewmodels.DashboardViewModel

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onNavigateToConsumption: () -> Unit,
    viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        uiState = uiState,
        onLogout = onLogout,
        onNavigateToConsumption = onNavigateToConsumption,
        onAddConsumption = viewModel::addRandomConsumption,
        onDeleteConsumption = viewModel::deleteConsumption
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onLogout: () -> Unit,
    onNavigateToConsumption: () -> Unit,
    onAddConsumption: () -> Unit,
    onDeleteConsumption: (Int) -> Unit
) {
    // Estado para controlar el panel (diálogo) de eliminación
    var recordToDelete by remember { mutableStateOf<ConsumptionRecord?>(null) }

    // Diálogo de confirmación para borrar
    if (recordToDelete != null) {
        AlertDialog(
            onDismissRequest = { recordToDelete = null },
            title = { Text("Eliminar Registro") },
            text = { Text("¿Deseas eliminar este registro de ${recordToDelete?.amountLiters} litros del día ${recordToDelete?.date}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteConsumption(recordToDelete!!.id)
                        recordToDelete = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { recordToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Consumo") },
                actions = {
                    // BOTÓN APARTE PARA AGREGAR
                    IconButton(onClick = onAddConsumption) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir Consumo")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            // Botón que solo navega a detalles/gráficas
            FloatingActionButton(onClick = onNavigateToConsumption) {
                Icon(Icons.Default.WaterDrop, contentDescription = "Ver Gráficas")
            }
        }
    ) { padding ->
        when (uiState) {
            is DashboardUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is DashboardUiState.Success -> {
                ConsumptionList(
                    records = uiState.history,
                    onRecordClick = { recordToDelete = it },
                    modifier = Modifier.padding(padding)
                )
            }
            is DashboardUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun ConsumptionList(
    records: List<ConsumptionRecord>,
    onRecordClick: (ConsumptionRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    if (records.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay registros. Usa el botón '+' arriba para agregar uno.")
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(records) { record ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRecordClick(record) }, // Al tocar, abre el panel de borrar
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = "${record.amountLiters} Litros", style = MaterialTheme.typography.titleMedium)
                                Text(text = record.date, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
