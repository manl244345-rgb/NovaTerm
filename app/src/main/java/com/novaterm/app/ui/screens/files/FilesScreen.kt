package com.novaterm.app.ui.screens.files

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.novaterm.app.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

enum class ViewMode { List, Grid }
enum class SortBy { Name, Size, Date, Type }

data class FileItem(val file: File)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(navController: NavController) {
    var currentPath by remember { mutableStateOf(File(System.getProperty("user.home") ?: "/sdcard")) }
    var viewMode by remember { mutableStateOf(ViewMode.List) }
    var sortBy by remember { mutableStateOf(SortBy.Name) }
    var showHidden by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    val files by remember(currentPath, sortBy, showHidden) {
        derivedStateOf {
            val rawFiles = currentPath.listFiles() ?: emptyArray()
            rawFiles
                .filter { showHidden || !it.name.startsWith(".") }
                .sortedWith(compareBy({ !it.isDirectory }, when (sortBy) {
                    SortBy.Name -> { f: File -> f.name.lowercase() }
                    SortBy.Size -> { f: File -> (-f.length()).toString() }
                    SortBy.Date -> { f: File -> (-f.lastModified()).toString() }
                    SortBy.Type -> { f: File -> f.extension.lowercase() }
                }))
                .map { FileItem(it) }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        if (isSearching) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search files...", color = NovaTextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NovaBlue, unfocusedBorderColor = NovaTextMuted, cursorColor = NovaBlue),
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(currentPath.name.ifEmpty { "Files" }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NovaTextPrimary)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (isSearching) { isSearching = false; searchQuery = "" }
                            else if (currentPath.parentFile != null) currentPath = currentPath.parentFile!!
                            else navController.popBackStack()
                        }) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary)
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearching = !isSearching }) {
                            Icon(Icons.Default.Search, "Search", tint = NovaTextSecondary)
                        }
                        IconButton(onClick = { viewMode = if (viewMode == ViewMode.List) ViewMode.Grid else ViewMode.List }) {
                            Icon(if (viewMode == ViewMode.List) Icons.Default.GridView else Icons.Default.ViewList, "Toggle view", tint = NovaTextSecondary)
                        }
                        IconButton(onClick = { showHidden = !showHidden }) {
                            Icon(if (showHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility, "Hidden files", tint = if (showHidden) NovaBlue else NovaTextSecondary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
                )
                // Breadcrumb path
                BreadcrumbBar(path = currentPath, onNavigate = { currentPath = it })
                HorizontalDivider(color = NovaCardVariant)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* New file/folder dialog */ },
                containerColor = NovaBlue
            ) {
                Icon(Icons.Default.Add, "New", tint = androidx.compose.ui.graphics.Color.White)
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        val displayFiles = if (searchQuery.isNotEmpty())
            files.filter { it.file.name.contains(searchQuery, ignoreCase = true) }
        else files

        if (displayFiles.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.FolderOff, null, tint = NovaTextMuted, modifier = Modifier.size(64.dp))
                    Text("Empty directory", color = NovaTextSecondary)
                }
            }
        } else if (viewMode == ViewMode.List) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(bottom = 80.dp)) {
                items(displayFiles) { item ->
                    FileListItem(item = item, onClick = {
                        if (item.file.isDirectory) currentPath = item.file
                    })
                    HorizontalDivider(color = NovaCardVariant.copy(alpha = 0.5f))
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayFiles) { item ->
                    FileGridItem(item = item, onClick = {
                        if (item.file.isDirectory) currentPath = item.file
                    })
                }
            }
        }
    }
}

@Composable
private fun BreadcrumbBar(path: File, onNavigate: (File) -> Unit) {
    val parts = mutableListOf<File>()
    var current: File? = path
    while (current != null) { parts.add(0, current); current = current.parentFile }

    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Home, null, tint = NovaTextMuted, modifier = Modifier.size(14.dp).clickable { onNavigate(parts.first()) })
        parts.drop(1).forEachIndexed { index, file ->
            Text(" / ", color = NovaTextMuted, style = MaterialTheme.typography.labelSmall)
            Text(file.name, color = if (index == parts.size - 2) NovaBlue else NovaTextSecondary, style = MaterialTheme.typography.labelSmall, modifier = Modifier.clickable { onNavigate(file) })
        }
    }
}

@Composable
private fun FileListItem(item: FileItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(fileIcon(item.file), null, tint = fileColor(item.file), modifier = Modifier.size(24.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.file.name, style = MaterialTheme.typography.bodyMedium, color = NovaTextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                if (item.file.isDirectory) "${item.file.listFiles()?.size ?: 0} items"
                else formatFileSize(item.file.length()),
                style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary
            )
        }
        Text(
            SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(item.file.lastModified())),
            style = MaterialTheme.typography.labelSmall, color = NovaTextMuted
        )
    }
}

@Composable
private fun FileGridItem(item: FileItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = NovaCard),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(fileIcon(item.file), null, tint = fileColor(item.file), modifier = Modifier.size(36.dp))
            Spacer(Modifier.height(4.dp))
            Text(item.file.name, style = MaterialTheme.typography.labelSmall, color = NovaTextPrimary, maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

private fun fileIcon(file: File): ImageVector = when {
    file.isDirectory -> Icons.Default.Folder
    file.extension in listOf("py", "js", "ts", "kt", "java", "go", "rs") -> Icons.Default.Code
    file.extension in listOf("md", "txt", "log") -> Icons.Default.Description
    file.extension in listOf("jpg", "png", "gif", "svg", "webp") -> Icons.Default.Image
    file.extension in listOf("zip", "tar", "gz", "bz2", "xz") -> Icons.Default.FolderZip
    file.extension in listOf("sh", "bash") -> Icons.Default.Terminal
    else -> Icons.Default.InsertDriveFile
}

private fun fileColor(file: File) = when {
    file.isDirectory -> NovaAmber
    file.extension in listOf("py", "js", "ts", "kt", "java") -> NovaBlue
    file.extension in listOf("md", "txt") -> NovaTextSecondary
    file.extension in listOf("jpg", "png", "gif") -> NovaPurple
    file.extension in listOf("zip", "tar", "gz") -> NovaCyan
    file.extension == "sh" -> NovaGreen
    else -> NovaTextMuted
}

private fun formatFileSize(bytes: Long): String = when {
    bytes < 1024 -> "$bytes B"
    bytes < 1024 * 1024 -> "${bytes / 1024} KB"
    bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
    else -> "${bytes / (1024 * 1024 * 1024)} GB"
}
