package com.novaterm.app.ui.screens.downloads

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

enum class DownloadStatus { Downloading, Paused, Completed, Failed }

data class Download(val id: String, val name: String, val url: String, val size: String, val progress: Float, val status: DownloadStatus, val speed: String = "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(navController: NavController) {
    var showAddDialog by remember { mutableStateOf(false) }
    val downloads by remember {
        mutableStateOf(
            listOf(
                Download("1", "python-3.11.tar.gz", "https://python.org/...", "24 MB", 1.0f, DownloadStatus.Completed),
                Download("2", "node-v20-linux.tar.xz", "https://nodejs.org/...", "58 MB", 0.65f, DownloadStatus.Downloading, "2.3 MB/s"),
                Download("3", "rust-1.75-x86.tar.gz", "https://rust-lang.org/...", "320 MB", 0.0f, DownloadStatus.Failed),
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Downloads", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) } },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.DeleteSweep, "Clear completed", tint = NovaTextSecondary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = NovaBlue) {
                Icon(Icons.Default.Add, "Add download", tint = androidx.compose.ui.graphics.Color.White)
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        if (downloads.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Download, null, tint = NovaTextMuted, modifier = Modifier.size(64.dp))
                    Text("No downloads", color = NovaTextSecondary)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(downloads) { download -> DownloadCard(download) }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showAddDialog) {
        var url by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("New Download", fontWeight = FontWeight.Bold) },
            containerColor = NovaCard,
            text = {
                OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("URL") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NovaBlue))
            },
            confirmButton = { Button(onClick = { showAddDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = NovaBlue)) { Text("Download") } },
            dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("Cancel", color = NovaTextSecondary) } }
        )
    }
}

@Composable
private fun DownloadCard(download: Download) {
    Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    when (download.status) {
                        DownloadStatus.Completed -> Icons.Default.CheckCircle
                        DownloadStatus.Failed -> Icons.Default.Error
                        DownloadStatus.Paused -> Icons.Default.PauseCircle
                        DownloadStatus.Downloading -> Icons.Default.Downloading
                    },
                    null,
                    tint = when (download.status) {
                        DownloadStatus.Completed -> NovaGreen
                        DownloadStatus.Failed -> NovaRed
                        DownloadStatus.Paused -> NovaAmber
                        DownloadStatus.Downloading -> NovaBlue
                    },
                    modifier = Modifier.size(24.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(download.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = NovaTextPrimary, maxLines = 1)
                    Text(download.size, style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
                }
                if (download.status == DownloadStatus.Downloading) {
                    Text(download.speed, style = MaterialTheme.typography.labelSmall, color = NovaCyan, fontFamily = MonoFontFamily)
                }
                Row {
                    when (download.status) {
                        DownloadStatus.Downloading -> IconButton(onClick = {}) { Icon(Icons.Default.Pause, null, tint = NovaTextSecondary, modifier = Modifier.size(18.dp)) }
                        DownloadStatus.Paused -> IconButton(onClick = {}) { Icon(Icons.Default.PlayArrow, null, tint = NovaBlue, modifier = Modifier.size(18.dp)) }
                        DownloadStatus.Failed -> IconButton(onClick = {}) { Icon(Icons.Default.Refresh, null, tint = NovaAmber, modifier = Modifier.size(18.dp)) }
                        DownloadStatus.Completed -> IconButton(onClick = {}) { Icon(Icons.Default.FolderOpen, null, tint = NovaTextSecondary, modifier = Modifier.size(18.dp)) }
                    }
                    IconButton(onClick = {}) { Icon(Icons.Default.Delete, null, tint = NovaTextMuted, modifier = Modifier.size(18.dp)) }
                }
            }
            if (download.status == DownloadStatus.Downloading || download.status == DownloadStatus.Paused) {
                LinearProgressIndicator(
                    progress = { download.progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = if (download.status == DownloadStatus.Paused) NovaAmber else NovaBlue,
                    trackColor = NovaSurfaceVariant
                )
                Text("${(download.progress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = NovaTextMuted)
            }
        }
    }
}
