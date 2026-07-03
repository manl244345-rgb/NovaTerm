package com.novaterm.app.ui.screens.ssh

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

data class SshHost(val id: String, val nickname: String, val hostname: String, val port: Int, val username: String, val isFavorite: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SshScreen(navController: NavController) {
    var showAddDialog by remember { mutableStateOf(false) }
    var hosts by remember {
        mutableStateOf(
            listOf(
                SshHost("1", "Production Server", "192.168.1.100", 22, "admin", true),
                SshHost("2", "Dev Box", "dev.example.com", 2222, "developer"),
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SSH Manager", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) } },
                actions = {
                    IconButton(onClick = { /* Key manager */ }) { Icon(Icons.Default.Key, "Keys", tint = NovaTextSecondary) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = NovaBlue) {
                Icon(Icons.Default.Add, "Add host", tint = androidx.compose.ui.graphics.Color.White)
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        if (hosts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.Lock, null, tint = NovaTextMuted, modifier = Modifier.size(64.dp))
                    Text("No SSH Hosts", style = MaterialTheme.typography.titleMedium, color = NovaTextSecondary)
                    Text("Add a host to connect via SSH", color = NovaTextMuted, style = MaterialTheme.typography.bodySmall)
                    Button(onClick = { showAddDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = NovaBlue)) {
                        Text("Add Host")
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Text("Saved Hosts", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary, modifier = Modifier.padding(bottom = 4.dp)) }
                items(hosts) { host -> SshHostCard(host = host, onConnect = {}) }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showAddDialog) AddSshHostDialog(onDismiss = { showAddDialog = false }, onAdd = { host -> hosts = hosts + host; showAddDialog = false })
}

@Composable
private fun SshHostCard(host: SshHost, onConnect: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NovaCard),
        shape = RoundedCornerShape(12.dp),
        onClick = onConnect
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(8.dp).offset(x = 12.dp, y = 12.dp)) {}
                Icon(Icons.Default.Computer, null, tint = NovaBlue, modifier = Modifier.size(32.dp))
                if (host.isFavorite) {
                    Icon(Icons.Default.Star, null, tint = NovaAmber, modifier = Modifier.size(14.dp).align(Alignment.TopEnd))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(host.nickname, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = NovaTextPrimary)
                Text("${host.username}@${host.hostname}:${host.port}", style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary, fontFamily = MonoFontFamily)
            }
            Button(
                onClick = onConnect,
                colors = ButtonDefaults.buttonColors(containerColor = NovaBlue.copy(alpha = 0.2f), contentColor = NovaBlue),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Connect", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun AddSshHostDialog(onDismiss: () -> Unit, onAdd: (SshHost) -> Unit) {
    var nickname by remember { mutableStateOf("") }
    var hostname by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("22") }
    var username by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add SSH Host", fontWeight = FontWeight.Bold) },
        containerColor = NovaCard,
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = nickname, onValueChange = { nickname = it }, label = { Text("Nickname") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = hostname, onValueChange = { hostname = it }, label = { Text("Hostname / IP") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = port, onValueChange = { port = it }, label = { Text("Port") }, modifier = Modifier.width(80.dp), singleLine = true)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (hostname.isNotBlank() && username.isNotBlank()) {
                        onAdd(SshHost(System.currentTimeMillis().toString(), nickname.ifBlank { hostname }, hostname, port.toIntOrNull() ?: 22, username))
                    }
                },
                enabled = hostname.isNotBlank() && username.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NovaBlue)
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", color = NovaTextSecondary) } }
    )
}
