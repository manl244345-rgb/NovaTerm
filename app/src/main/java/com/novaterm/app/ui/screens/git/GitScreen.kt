package com.novaterm.app.ui.screens.git

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.novaterm.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Status", "Log", "Branches", "Remotes")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Git", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                    navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) } },
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Default.Refresh, "Fetch", tint = NovaTextSecondary) }
                        IconButton(onClick = {}) { Icon(Icons.Default.Add, "Init", tint = NovaTextSecondary) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
                )
                TabRow(selectedTabIndex = selectedTab, containerColor = NovaSurface, contentColor = NovaBlue) {
                    tabs.forEachIndexed { index, title ->
                        Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title, style = MaterialTheme.typography.labelMedium) })
                    }
                }
            }
        },
        floatingActionButton = {
            when (selectedTab) {
                0 -> FloatingActionButton(onClick = {}, containerColor = NovaGreen) {
                    Icon(Icons.Default.Check, "Commit", tint = androidx.compose.ui.graphics.Color.White)
                }
                else -> {}
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (selectedTab) {
                0 -> GitStatusTab()
                1 -> GitLogTab()
                2 -> GitBranchesTab()
                3 -> GitRemotesTab()
            }
        }
    }
}

@Composable
private fun GitStatusTab() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Repo info card
        Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Hub, null, tint = NovaPink, modifier = Modifier.size(20.dp))
                    Text("No repository selected", fontWeight = FontWeight.SemiBold, color = NovaTextPrimary)
                }
                Text("Initialize a new repository or clone an existing one to get started.", style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = {}) { Text("Initialize", style = MaterialTheme.typography.labelMedium) }
                    Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = NovaBlue)) { Text("Clone", style = MaterialTheme.typography.labelMedium) }
                }
            }
        }
        Text("Recent Repositories", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary)
        Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp)) {
            Box(modifier = Modifier.padding(32.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No recent repositories", color = NovaTextMuted, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun GitLogTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.History, null, tint = NovaTextMuted, modifier = Modifier.size(48.dp))
            Text("No commit history", color = NovaTextSecondary)
            Text("Open a repository to view its commit log.", color = NovaTextMuted, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun GitBranchesTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.AccountTree, null, tint = NovaTextMuted, modifier = Modifier.size(48.dp))
            Text("No branches", color = NovaTextSecondary)
        }
    }
}

@Composable
private fun GitRemotesTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Cloud, null, tint = NovaTextMuted, modifier = Modifier.size(48.dp))
            Text("No remotes configured", color = NovaTextSecondary)
        }
    }
}
