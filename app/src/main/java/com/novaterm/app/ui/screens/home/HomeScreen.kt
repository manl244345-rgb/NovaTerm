package com.novaterm.app.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.novaterm.app.ui.navigation.Screen
import com.novaterm.app.ui.theme.*

data class QuickAction(val icon: ImageVector, val label: String, val route: String, val color: androidx.compose.ui.graphics.Color)

private val quickActions = listOf(
    QuickAction(Icons.Default.Terminal, "Terminal", Screen.Terminal.route, NovaBlue),
    QuickAction(Icons.Default.Code, "Editor", Screen.Editor.route, NovaPurple),
    QuickAction(Icons.Default.FolderOpen, "Files", Screen.Files.route, NovaGreen),
    QuickAction(Icons.Default.Language, "Browser", Screen.Browser.route, NovaCyan),
    QuickAction(Icons.Default.Inventory, "Packages", Screen.Packages.route, NovaAmber),
    QuickAction(Icons.Default.Hub, "Git", Screen.Git.route, NovaPink),
    QuickAction(Icons.Default.Lock, "SSH", Screen.Ssh.route, NovaBlue),
    QuickAction(Icons.Default.Download, "Downloads", Screen.Downloads.route, NovaCyan),
    QuickAction(Icons.Default.MenuBook, "Docs", Screen.Docs.route, NovaPurple),
    QuickAction(Icons.Default.ViewQuilt, "Workspace", Screen.Workspace.route, NovaGreen),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("NovaTerm", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NovaTextPrimary, fontFamily = MonoFontFamily)
                        Text("Linux Workstation", style = MaterialTheme.typography.labelSmall, color = NovaBlue)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Monitor.route) }) {
                        Icon(Icons.Default.MonitorHeart, "System Monitor", tint = NovaTextSecondary)
                    }
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, "Settings", tint = NovaTextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaBlack)
            )
        },
        containerColor = NovaBlack
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting card
            item {
                GreetingCard()
            }

            // System stats
            item {
                Text("System", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, modifier = Modifier.padding(horizontal = 4.dp))
                Spacer(Modifier.height(8.dp))
                SystemStatsRow()
            }

            // Quick Actions
            item {
                Text("Quick Actions", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, modifier = Modifier.padding(horizontal = 4.dp))
                Spacer(Modifier.height(8.dp))
                QuickActionsGrid(navController)
            }

            // Recent items placeholder
            item {
                Text("Recent Projects", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, modifier = Modifier.padding(horizontal = 4.dp))
                Spacer(Modifier.height(8.dp))
                RecentProjectsCard(navController)
            }

            // Git repos
            item {
                Text("Git Repositories", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, modifier = Modifier.padding(horizontal = 4.dp))
                Spacer(Modifier.height(8.dp))
                GitReposCard(navController)
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun GreetingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NovaSurfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Good day, developer", style = MaterialTheme.typography.titleMedium, color = NovaTextPrimary, fontWeight = FontWeight.SemiBold)
                Text("Workspace: Default", style = MaterialTheme.typography.bodySmall, color = NovaBlue)
                Spacer(Modifier.height(4.dp))
                Text("Ready to build something great?", style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
            }
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(NovaBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Terminal, null, tint = NovaBlue, modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
private fun SystemStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(modifier = Modifier.weight(1f), label = "CPU", value = "12%", icon = Icons.Default.Memory, color = NovaCyan)
        StatCard(modifier = Modifier.weight(1f), label = "RAM", value = "2.1 GB", icon = Icons.Default.Storage, color = NovaPurple)
        StatCard(modifier = Modifier.weight(1f), label = "Battery", value = "87%", icon = Icons.Default.BatteryFull, color = NovaGreen)
    }
}

@Composable
private fun StatCard(modifier: Modifier, label: String, value: String, icon: ImageVector, color: androidx.compose.ui.graphics.Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = NovaCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Text(value, style = MaterialTheme.typography.titleSmall, color = NovaTextPrimary, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
        }
    }
}

@Composable
private fun QuickActionsGrid(navController: NavController) {
    val chunked = quickActions.chunked(5)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        chunked.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { action ->
                    QuickActionItem(modifier = Modifier.weight(1f), action = action, onClick = { navController.navigate(action.route) })
                }
                repeat(5 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun QuickActionItem(modifier: Modifier, action: QuickAction, onClick: () -> Unit) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = NovaCard),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(action.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(action.icon, null, tint = action.color, modifier = Modifier.size(20.dp))
            }
            Text(action.label, style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary, maxLines = 1)
        }
    }
}

@Composable
private fun RecentProjectsCard(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NovaCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("my-python-app" to NovaGreen, "web-project" to NovaBlue, "rust-cli-tool" to NovaAmber).forEach { (name, color) ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.clickable { navController.navigate(Screen.Files.route) }) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(color))
                    Text(name, style = MaterialTheme.typography.bodyMedium, color = NovaTextPrimary, fontFamily = MonoFontFamily, modifier = Modifier.weight(1f))
                    Text("2h ago", style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
                }
            }
            TextButton(onClick = { navController.navigate(Screen.Files.route) }, modifier = Modifier.align(Alignment.End)) {
                Text("View all →", color = NovaBlue, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun GitReposCard(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NovaCard),
        shape = RoundedCornerShape(16.dp),
        onClick = { navController.navigate(Screen.Git.route) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("No repositories found", style = MaterialTheme.typography.bodyMedium, color = NovaTextSecondary)
                Text("Initialize or clone a repository to get started", style = MaterialTheme.typography.labelSmall, color = NovaTextMuted)
            }
            Icon(Icons.Default.Hub, null, tint = NovaBlue, modifier = Modifier.size(24.dp))
        }
    }
}
