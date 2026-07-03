package com.novaterm.app.ui.screens.monitor

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.novaterm.app.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemMonitorScreen(navController: NavController) {
    var cpuUsage by remember { mutableStateOf(12f) }
    var memUsed by remember { mutableStateOf(2.1f) }
    var memTotal by remember { mutableStateOf(8f) }
    var batteryLevel by remember { mutableStateOf(87) }
    var downloadSpeed by remember { mutableStateOf("2.4 MB/s") }
    var uploadSpeed by remember { mutableStateOf("0.8 MB/s") }
    var storageUsed by remember { mutableStateOf(48f) }
    var storageTotal by remember { mutableStateOf(128f) }

    // Simulate live updates
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            cpuUsage = (5..45).random().toFloat()
            memUsed = ((15..35).random() / 10f)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("System Monitor", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) } },
                actions = { IconButton(onClick = {}) { Icon(Icons.Default.Refresh, "Refresh", tint = NovaTextSecondary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
            )
        },
        containerColor = NovaBlack
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GaugeCard(modifier = Modifier.weight(1f), title = "CPU", value = cpuUsage / 100f, displayValue = "${cpuUsage.toInt()}%", icon = Icons.Default.Memory, color = NovaCyan)
                    GaugeCard(modifier = Modifier.weight(1f), title = "Memory", value = memUsed / memTotal, displayValue = "${memUsed}/${memTotal}GB", icon = Icons.Default.Storage, color = NovaPurple)
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GaugeCard(modifier = Modifier.weight(1f), title = "Battery", value = batteryLevel / 100f, displayValue = "$batteryLevel%", icon = Icons.Default.BatteryFull, color = NovaGreen)
                    GaugeCard(modifier = Modifier.weight(1f), title = "Storage", value = storageUsed / storageTotal, displayValue = "${storageUsed.toInt()}/${storageTotal.toInt()}GB", icon = Icons.Default.SdStorage, color = NovaAmber)
                }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Network", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, fontWeight = FontWeight.SemiBold)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            NetworkStat("↓ Download", downloadSpeed, NovaGreen)
                            NetworkStat("↑ Upload", uploadSpeed, NovaBlue)
                        }
                    }
                }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("NovaTerm Environment", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, fontWeight = FontWeight.SemiBold)
                        InfoRow("Running Sessions", "1 terminal")
                        InfoRow("Background Jobs", "0 jobs")
                        InfoRow("Installed Packages", "6 packages")
                        InfoRow("App Storage", "24 MB")
                        InfoRow("Workspace", "Default")
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun GaugeCard(modifier: Modifier, title: String, value: Float, displayValue: String, icon: ImageVector, color: androidx.compose.ui.graphics.Color) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
                Text(title, style = MaterialTheme.typography.labelMedium, color = NovaTextSecondary)
            }
            Text(displayValue, style = MaterialTheme.typography.titleMedium, color = NovaTextPrimary, fontWeight = FontWeight.Bold, fontFamily = MonoFontFamily)
            LinearProgressIndicator(progress = { value.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth(), color = color, trackColor = NovaSurfaceVariant)
        }
    }
}

@Composable
private fun NetworkStat(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = color, fontWeight = FontWeight.Bold, fontFamily = MonoFontFamily)
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
        Text(value, style = MaterialTheme.typography.bodySmall, color = NovaTextPrimary, fontFamily = MonoFontFamily)
    }
}
