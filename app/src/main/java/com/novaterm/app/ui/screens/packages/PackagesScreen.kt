package com.novaterm.app.ui.screens.packages

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.novaterm.app.ui.theme.*

data class Package(val id: String, val name: String, val version: String, val description: String, val category: String, val isInstalled: Boolean = false, val size: String = "")

private val samplePackages = listOf(
    Package("python", "Python 3.11", "3.11.9", "High-level programming language", "Languages", true, "42 MB"),
    Package("nodejs", "Node.js", "20.11.0", "JavaScript runtime environment", "Languages", false, "58 MB"),
    Package("git", "Git", "2.43.0", "Distributed version control system", "Tools", true, "18 MB"),
    Package("rust", "Rust", "1.75.0", "Systems programming language", "Languages", false, "320 MB"),
    Package("golang", "Go", "1.21.6", "Open source programming language", "Languages", false, "120 MB"),
    Package("curl", "cURL", "8.5.0", "Transfer data from servers", "Utilities", true, "2 MB"),
    Package("wget", "Wget", "1.21.4", "Network downloader", "Utilities", true, "1.5 MB"),
    Package("nano", "Nano", "7.2", "Simple text editor", "Editors", true, "800 KB"),
    Package("sqlite3", "SQLite", "3.44.2", "Self-contained SQL database engine", "Databases", true, "4 MB"),
    Package("htop", "htop", "3.3.0", "Interactive process viewer", "Utilities", false, "300 KB"),
    Package("vim", "Vim", "9.1", "Highly configurable text editor", "Editors", false, "3 MB"),
    Package("tmux", "tmux", "3.4", "Terminal multiplexer", "Utilities", false, "500 KB"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Languages", "Tools", "Editors", "Utilities", "Databases")

    val filtered = samplePackages.filter {
        (selectedCategory == "All" || it.category == selectedCategory) &&
        (searchQuery.isEmpty() || it.name.contains(searchQuery, true) || it.description.contains(searchQuery, true))
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Nova Packages", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                    navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) } },
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Default.Refresh, "Update", tint = NovaTextSecondary) }
                        IconButton(onClick = {}) { Icon(Icons.Default.MedicalServices, "Doctor", tint = NovaTextSecondary) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
                )
                // Search
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search packages...", color = NovaTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = NovaTextMuted) },
                    trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Clear, null, tint = NovaTextMuted) } },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NovaBlue, unfocusedBorderColor = NovaTextMuted.copy(0.3f)),
                    shape = RoundedCornerShape(12.dp)
                )
                // Category chips
                androidx.compose.foundation.lazy.LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = cat == selectedCategory,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NovaBlue.copy(0.2f), selectedLabelColor = NovaBlue)
                        )
                    }
                }
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Text("${filtered.size} packages • ${filtered.count { it.isInstalled }} installed", style = MaterialTheme.typography.labelSmall, color = NovaTextMuted, modifier = Modifier.padding(bottom = 4.dp))
            }
            items(filtered) { pkg -> PackageCard(pkg = pkg) }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun PackageCard(pkg: Package) {
    var isInstalling by remember { mutableStateOf(false) }
    var installed by remember { mutableStateOf(pkg.isInstalled) }

    Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(NovaBlue.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                Text(pkg.name.first().uppercase(), color = NovaBlue, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontFamily = MonoFontFamily)
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(pkg.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = NovaTextPrimary)
                    if (installed) {
                        Text("✓", color = NovaGreen, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Text(pkg.description, style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary, maxLines = 1)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(pkg.version, style = MaterialTheme.typography.labelSmall, color = NovaTextMuted, fontFamily = MonoFontFamily)
                    if (pkg.size.isNotEmpty()) Text("• ${pkg.size}", style = MaterialTheme.typography.labelSmall, color = NovaTextMuted)
                }
            }
            if (isInstalling) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp, color = NovaBlue)
            } else {
                Button(
                    onClick = {
                        isInstalling = true
                        // Simulate install
                        installed = !installed
                        isInstalling = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (installed) NovaRed.copy(0.2f) else NovaBlue.copy(0.2f),
                        contentColor = if (installed) NovaRed else NovaBlue
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(if (installed) "Remove" else "Install", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
