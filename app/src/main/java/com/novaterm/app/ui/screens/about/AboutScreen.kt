package com.novaterm.app.ui.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.novaterm.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About NovaTerm", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
            )
        },
        containerColor = NovaBlack
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                        Text("N", color = NovaBlue, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, fontFamily = MonoFontFamily)
                    }
                    Text("NovaTerm", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = NovaTextPrimary, fontFamily = MonoFontFamily)
                    Text("Linux Workstation for Android", style = MaterialTheme.typography.bodyMedium, color = NovaTextSecondary)
                    Text("Version 1.0.0 (Build 1)", style = MaterialTheme.typography.labelSmall, color = NovaTextMuted, fontFamily = MonoFontFamily)
                }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Description", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "NovaTerm is the most complete Linux workstation and developer environment available for Android. " +
                            "It provides professional-grade terminal emulation, code editing, file management, Git integration, " +
                            "SSH connectivity, and a powerful package management system — all in one seamless application.",
                            style = MaterialTheme.typography.bodySmall, color = NovaTextPrimary, textAlign = TextAlign.Start
                        )
                    }
                }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Technology Stack", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, fontWeight = FontWeight.SemiBold)
                        listOf(
                            "Kotlin" to "Primary language",
                            "Jetpack Compose" to "UI framework",
                            "Material Design 3" to "Design system",
                            "Hilt" to "Dependency injection",
                            "Room" to "Local database",
                            "Coroutines + Flow" to "Async operations",
                        ).forEach { (tech, desc) ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(tech, style = MaterialTheme.typography.bodySmall, color = NovaBlue, fontFamily = MonoFontFamily)
                                Text(desc, style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
                            }
                        }
                    }
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(modifier = Modifier.weight(1f), onClick = {}, border = androidx.compose.foundation.BorderStroke(1.dp, NovaBlue.copy(0.5f))) {
                        Icon(Icons.Default.Code, null, modifier = Modifier.size(16.dp), tint = NovaBlue)
                        Spacer(Modifier.width(4.dp))
                        Text("Source Code", color = NovaBlue)
                    }
                    OutlinedButton(modifier = Modifier.weight(1f), onClick = {}, border = androidx.compose.foundation.BorderStroke(1.dp, NovaTextMuted.copy(0.5f))) {
                        Icon(Icons.Default.BugReport, null, modifier = Modifier.size(16.dp), tint = NovaTextSecondary)
                        Spacer(Modifier.width(4.dp))
                        Text("Report Bug", color = NovaTextSecondary)
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}
