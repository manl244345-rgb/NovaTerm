package com.novaterm.app.ui.screens.settings

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

data class SettingSection(val title: String, val icon: ImageVector, val color: androidx.compose.ui.graphics.Color, val items: List<SettingItem>)
sealed class SettingItem {
    data class Toggle(val title: String, val description: String, val key: String, val default: Boolean = false) : SettingItem()
    data class Action(val title: String, val description: String, val icon: ImageVector? = null) : SettingItem()
    data class Navigation(val title: String, val description: String, val destination: String = "") : SettingItem()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var darkTheme by remember { mutableStateOf(true) }
    var dynamicColor by remember { mutableStateOf(false) }
    var beginnerMode by remember { mutableStateOf(false) }
    var autoSave by remember { mutableStateOf(true) }
    var showLineNumbers by remember { mutableStateOf(true) }
    var confirmDelete by remember { mutableStateOf(true) }
    var showHiddenFiles by remember { mutableStateOf(false) }
    var terminalBell by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
            )
        },
        containerColor = NovaBlack
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // Appearance
            item {
                SettingGroup(title = "Appearance", icon = Icons.Default.Palette, color = NovaPurple) {
                    ToggleSetting("Dark Theme", "Use dark background", darkTheme) { darkTheme = it }
                    ToggleSetting("Dynamic Colors", "Use system accent colors (Android 12+)", dynamicColor) { dynamicColor = it }
                    ActionSetting("Accent Color", "Electric Blue", Icons.Default.Circle)
                    ActionSetting("Font Family", "System Default", Icons.Default.TextFields)
                    ActionSetting("Animation Speed", "Normal", Icons.Default.Animation)
                }
            }

            // Terminal
            item {
                SettingGroup(title = "Terminal", icon = Icons.Default.Terminal, color = NovaGreen) {
                    ToggleSetting("Beginner Mode", "Show command explanations and suggestions", beginnerMode) { beginnerMode = it }
                    ToggleSetting("Terminal Bell", "Play sound on bell escape sequence", terminalBell) { terminalBell = it }
                    ActionSetting("Terminal Font", "Monospace", Icons.Default.Code)
                    ActionSetting("Font Size", "13px", Icons.Default.FormatSize)
                    ActionSetting("Cursor Style", "Block", Icons.Default.TextCursor)
                    ActionSetting("Scrollback Lines", "10000", Icons.Default.UnfoldMore)
                    ActionSetting("Default Shell", "/system/bin/sh", Icons.Default.Terminal)
                }
            }

            // Editor
            item {
                SettingGroup(title = "Editor", icon = Icons.Default.Code, color = NovaBlue) {
                    ToggleSetting("Auto Save", "Save files automatically on change", autoSave) { autoSave = it }
                    ToggleSetting("Line Numbers", "Show line numbers in editor", showLineNumbers) { showLineNumbers = it }
                    ActionSetting("Tab Width", "4 spaces", Icons.Default.SpaceBar)
                    ActionSetting("Indentation", "Spaces", Icons.Default.FormatIndentIncrease)
                    ActionSetting("File Encoding", "UTF-8", Icons.Default.Language)
                    ActionSetting("Line Endings", "LF (Unix)", Icons.Default.TextFormat)
                }
            }

            // Files
            item {
                SettingGroup(title = "Files", icon = Icons.Default.FolderOpen, color = NovaAmber) {
                    ToggleSetting("Show Hidden Files", "Display files starting with a dot", showHiddenFiles) { showHiddenFiles = it }
                    ToggleSetting("Confirm Delete", "Ask before deleting files", confirmDelete) { confirmDelete = it }
                    ActionSetting("Default View", "List", Icons.Default.ViewList)
                    ActionSetting("Sort By", "Name (A-Z)", Icons.Default.SortByAlpha)
                    ActionSetting("Thumbnail Cache", "128 MB", Icons.Default.Image)
                }
            }

            // Git
            item {
                SettingGroup(title = "Git", icon = Icons.Default.Hub, color = NovaPink) {
                    ActionSetting("Default Username", "Not set", Icons.Default.Person)
                    ActionSetting("Default Email", "Not set", Icons.Default.Email)
                    ActionSetting("Default Branch", "main", Icons.Default.AccountTree)
                    ActionSetting("SSH Key", "Not configured", Icons.Default.Key)
                }
            }

            // SSH
            item {
                SettingGroup(title = "SSH", icon = Icons.Default.Lock, color = NovaCyan) {
                    ActionSetting("Known Hosts", "Manage trusted hosts", Icons.Default.VerifiedUser)
                    ActionSetting("SSH Keys", "Manage SSH keys", Icons.Default.Key)
                    ActionSetting("Connection Timeout", "30 seconds", Icons.Default.Timer)
                }
            }

            // Privacy
            item {
                SettingGroup(title = "Privacy & Security", icon = Icons.Default.Security, color = NovaRed) {
                    ToggleSetting("Biometric Lock", "Require biometric to open NovaTerm", false) {}
                    ActionSetting("Clear History", "Remove all command history", Icons.Default.DeleteForever)
                    ActionSetting("Export Backup", "Backup settings and data", Icons.Default.Backup)
                    ActionSetting("Restore Backup", "Restore from backup file", Icons.Default.Restore)
                }
            }

            // About
            item {
                SettingGroup(title = "About", icon = Icons.Default.Info, color = NovaBlue) {
                    ActionSetting("Version", "1.0.0 (1)", null)
                    ActionSetting("Open Source Licenses", "View third-party licenses", Icons.Default.Article)
                    ActionSetting("Privacy Policy", "View privacy policy", Icons.Default.Policy)
                    ActionSetting("Send Feedback", "Report a bug or suggestion", Icons.Default.Feedback)
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun SettingGroup(title: String, icon: ImageVector, color: androidx.compose.ui.graphics.Color, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
            Text(title, style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, fontWeight = FontWeight.SemiBold)
        }
        Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp)) {
            Column { content() }
        }
    }
}

@Composable
private fun ToggleSetting(title: String, description: String, value: Boolean, onToggle: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = NovaTextPrimary)
            Text(description, style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
        }
        Switch(checked = value, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedThumbColor = androidx.compose.ui.graphics.Color.White, checkedTrackColor = NovaBlue))
    }
    HorizontalDivider(color = NovaSurfaceVariant, thickness = 0.5.dp, modifier = Modifier.padding(start = 16.dp))
}

@Composable
private fun ActionSetting(title: String, value: String, icon: ImageVector?) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = NovaTextPrimary)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(value, style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
            Icon(Icons.Default.ChevronRight, null, tint = NovaTextMuted, modifier = Modifier.size(18.dp))
        }
    }
    HorizontalDivider(color = NovaSurfaceVariant, thickness = 0.5.dp, modifier = Modifier.padding(start = 16.dp))
}
