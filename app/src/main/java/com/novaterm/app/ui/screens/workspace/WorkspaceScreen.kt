package com.novaterm.app.ui.screens.workspace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.novaterm.app.ui.theme.*

data class Workspace(val id: String, val name: String, val color: Color, val projectCount: Int, val lastOpened: String, val isActive: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceScreen(navController: NavController) {
    var workspaces by remember {
        mutableStateOf(listOf(
            Workspace("1", "Default", NovaBlue, 3, "Just now", true),
            Workspace("2", "Web Projects", NovaCyan, 5, "2 hours ago"),
            Workspace("3", "Android Dev", NovaGreen, 2, "Yesterday"),
        ))
    }
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workspaces", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }, containerColor = NovaBlue) {
                Icon(Icons.Default.Add, "New workspace", tint = Color.White)
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { Text("Your Workspaces", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, modifier = Modifier.padding(bottom = 4.dp)) }
            items(workspaces) { ws ->
                WorkspaceCard(workspace = ws, onActivate = {
                    workspaces = workspaces.map { it.copy(isActive = it.id == ws.id) }
                })
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    if (showCreateDialog) {
        var name by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("New Workspace", fontWeight = FontWeight.Bold) },
            containerColor = NovaCard,
            text = {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Workspace Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NovaBlue))
            },
            confirmButton = {
                Button(onClick = {
                    if (name.isNotBlank()) {
                        workspaces = workspaces + Workspace(System.currentTimeMillis().toString(), name, NovaBlue, 0, "Just now")
                        showCreateDialog = false
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = NovaBlue)) { Text("Create") }
            },
            dismissButton = { TextButton(onClick = { showCreateDialog = false }) { Text("Cancel", color = NovaTextSecondary) } }
        )
    }
}

@Composable
private fun WorkspaceCard(workspace: Workspace, onActivate: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = if (workspace.isActive) workspace.color.copy(alpha = 0.1f) else NovaCard),
        shape = RoundedCornerShape(12.dp),
        border = if (workspace.isActive) androidx.compose.foundation.BorderStroke(1.dp, workspace.color.copy(alpha = 0.5f)) else null,
        onClick = onActivate
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(workspace.color.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                Text(workspace.name.first().uppercase(), color = workspace.color, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(workspace.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = NovaTextPrimary)
                    if (workspace.isActive) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(NovaGreen))
                    }
                }
                Text("${workspace.projectCount} projects • Last opened ${workspace.lastOpened}", style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
            }
            if (!workspace.isActive) {
                Button(onClick = onActivate, colors = ButtonDefaults.buttonColors(containerColor = NovaBlue.copy(0.2f), contentColor = NovaBlue), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
                    Text("Switch", style = MaterialTheme.typography.labelSmall)
                }
            } else {
                Text("Active", style = MaterialTheme.typography.labelSmall, color = NovaGreen, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
