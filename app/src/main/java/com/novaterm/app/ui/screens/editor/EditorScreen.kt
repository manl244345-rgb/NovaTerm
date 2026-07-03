package com.novaterm.app.ui.screens.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.novaterm.app.ui.theme.*

data class EditorTab(val id: String, val name: String, val language: String, var content: String, var isModified: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(navController: NavController) {
    var tabs by remember {
        mutableStateOf(
            listOf(
                EditorTab("1", "main.py", "python", "#!/usr/bin/env python3\n# NovaTerm Code Editor\n\ndef main():\n    print(\"Hello from NovaTerm!\")\n\nif __name__ == \"__main__\":\n    main()\n"),
                EditorTab("2", "README.md", "markdown", "# My Project\n\nA project created with NovaTerm.\n\n## Getting Started\n\nEdit this file to get started.\n")
            )
        )
    }
    var selectedTabId by remember { mutableStateOf("1") }
    var showLineNumbers by remember { mutableStateOf(true) }
    var fontSize by remember { mutableStateOf(13) }

    val selectedTab = tabs.find { it.id == selectedTabId } ?: tabs.first()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text("Editor", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NovaTextPrimary)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary)
                        }
                    },
                    actions = {
                        IconButton(onClick = { fontSize = (fontSize - 1).coerceAtLeast(8) }) {
                            Icon(Icons.Default.ZoomOut, "Decrease font", tint = NovaTextSecondary)
                        }
                        IconButton(onClick = { fontSize = (fontSize + 1).coerceAtMost(24) }) {
                            Icon(Icons.Default.ZoomIn, "Increase font", tint = NovaTextSecondary)
                        }
                        IconButton(onClick = { showLineNumbers = !showLineNumbers }) {
                            Icon(
                                if (showLineNumbers) Icons.Default.ViewHeadline else Icons.Default.FormatListNumbered,
                                "Toggle line numbers",
                                tint = if (showLineNumbers) NovaBlue else NovaTextSecondary
                            )
                        }
                        IconButton(onClick = { /* Save */ }) {
                            Icon(Icons.Default.Save, "Save", tint = if (selectedTab.isModified) NovaAmber else NovaTextSecondary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
                )
                // Tab bar
                LazyRow(
                    modifier = Modifier.fillMaxWidth().background(NovaSurface),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(tabs) { tab ->
                        EditorTabItem(
                            tab = tab,
                            isSelected = tab.id == selectedTabId,
                            onClick = { selectedTabId = tab.id },
                            onClose = {
                                val remaining = tabs.filter { it.id != tab.id }
                                tabs = remaining
                                if (selectedTabId == tab.id) selectedTabId = remaining.firstOrNull()?.id ?: ""
                            }
                        )
                    }
                    item {
                        IconButton(onClick = {
                            val newId = (tabs.size + 1).toString()
                            tabs = tabs + EditorTab(newId, "untitled-$newId.txt", "text", "")
                            selectedTabId = newId
                        }) {
                            Icon(Icons.Default.Add, "New tab", tint = NovaTextSecondary, modifier = Modifier.size(18.dp))
                        }
                    }
                }
                HorizontalDivider(color = NovaCardVariant, thickness = 1.dp)
            }
        },
        bottomBar = {
            // Status bar
            Surface(color = NovaSurface) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(selectedTab.language.uppercase(), style = MaterialTheme.typography.labelSmall, color = NovaBlue)
                    Text("UTF-8", style = MaterialTheme.typography.labelSmall, color = NovaTextMuted)
                    Text("LF", style = MaterialTheme.typography.labelSmall, color = NovaTextMuted)
                    Text("${fontSize}px", style = MaterialTheme.typography.labelSmall, color = NovaTextMuted)
                    Spacer(Modifier.weight(1f))
                    if (selectedTab.isModified) Text("● Modified", style = MaterialTheme.typography.labelSmall, color = NovaAmber)
                }
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        if (tabs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(Icons.Default.Code, null, tint = NovaTextMuted, modifier = Modifier.size(64.dp))
                    Text("No files open", color = NovaTextSecondary, style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = {
                        val newId = "1"
                        tabs = listOf(EditorTab(newId, "untitled.txt", "text", ""))
                        selectedTabId = newId
                    }) { Text("New File") }
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (showLineNumbers) {
                    // Line numbers
                    val lines = selectedTab.content.lines()
                    LazyColumn(
                        modifier = Modifier.width(48.dp).fillMaxHeight().background(NovaSurface),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(lines) { index, _ ->
                            Text(
                                text = "${index + 1}",
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                color = NovaTextMuted,
                                fontSize = fontSize.sp,
                                fontFamily = MonoFontFamily,
                                lineHeight = (fontSize * 1.6).sp
                            )
                        }
                    }
                    VerticalDivider(color = NovaCardVariant, modifier = Modifier.fillMaxHeight())
                }
                // Code editing area
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    BasicTextField(
                        value = selectedTab.content,
                        onValueChange = { newContent ->
                            tabs = tabs.map { if (it.id == selectedTabId) it.copy(content = newContent, isModified = true) else it }
                        },
                        modifier = Modifier.fillMaxSize().padding(8.dp).verticalScroll(rememberScrollState()),
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = MonoFontFamily,
                            fontSize = fontSize.sp,
                            color = NovaTextPrimary,
                            lineHeight = (fontSize * 1.6).sp
                        ),
                        cursorBrush = SolidColor(NovaCyan)
                    )
                }
            }
        }
    }
}

@Composable
private fun EditorTabItem(tab: EditorTab, isSelected: Boolean, onClick: () -> Unit, onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .background(if (isSelected) NovaBlack else NovaSurface)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (tab.isModified) {
            Box(modifier = Modifier.size(6.dp).background(NovaAmber, RoundedCornerShape(3.dp)))
        }
        Text(
            tab.name,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) NovaTextPrimary else NovaTextSecondary,
            fontFamily = MonoFontFamily
        )
        IconButton(onClick = onClose, modifier = Modifier.size(16.dp)) {
            Icon(Icons.Default.Close, "Close tab", tint = NovaTextMuted, modifier = Modifier.size(12.dp))
        }
    }
}
