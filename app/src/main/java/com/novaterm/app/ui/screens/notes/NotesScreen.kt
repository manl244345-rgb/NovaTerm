package com.novaterm.app.ui.screens.notes

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
import java.text.SimpleDateFormat
import java.util.*

data class Note(val id: String, val title: String, val content: String, val createdAt: Long = System.currentTimeMillis(), val tags: List<String> = emptyList())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(navController: NavController) {
    var notes by remember {
        mutableStateOf(listOf(
            Note("1", "Project Ideas", "# Project Ideas\n\n- Build a CLI tool in Rust\n- Try Flutter for mobile\n- Learn Kubernetes\n", tags = listOf("ideas")),
            Note("2", "SSH Setup Notes", "## Server Setup\n\n```bash\nssh-keygen -t ed25519\n```\n", tags = listOf("ssh", "setup")),
        ))
    }
    var showNewNoteDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showNewNoteDialog = true }, containerColor = NovaBlue) {
                Icon(Icons.Default.Add, "New note", tint = androidx.compose.ui.graphics.Color.White)
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        if (notes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Note, null, tint = NovaTextMuted, modifier = Modifier.size(64.dp))
                    Text("No notes yet", color = NovaTextSecondary)
                    Button(onClick = { showNewNoteDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = NovaBlue)) { Text("Create Note") }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notes) { note ->
                    NoteCard(note = note, onClick = { editingNote = note }, onDelete = { notes = notes.filter { it.id != note.id } })
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showNewNoteDialog || editingNote != null) {
        NoteEditorDialog(
            note = editingNote,
            onDismiss = { showNewNoteDialog = false; editingNote = null },
            onSave = { title, content ->
                if (editingNote != null) {
                    notes = notes.map { if (it.id == editingNote!!.id) it.copy(title = title, content = content) else it }
                } else {
                    notes = notes + Note(System.currentTimeMillis().toString(), title, content)
                }
                showNewNoteDialog = false; editingNote = null
            }
        )
    }
}

@Composable
private fun NoteCard(note: Note, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp), onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(note.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = NovaTextPrimary, modifier = Modifier.weight(1f))
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, "Delete", tint = NovaTextMuted, modifier = Modifier.size(16.dp))
                }
            }
            Text(note.content.take(100).replace("#", "").trim(), style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary, maxLines = 2)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                note.tags.forEach { tag ->
                    SuggestionChip(onClick = {}, label = { Text(tag, style = MaterialTheme.typography.labelSmall) }, colors = SuggestionChipDefaults.suggestionChipColors(containerColor = NovaBlue.copy(0.1f), labelColor = NovaBlue))
                }
                Spacer(Modifier.weight(1f))
                Text(SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(note.createdAt)), style = MaterialTheme.typography.labelSmall, color = NovaTextMuted)
            }
        }
    }
}

@Composable
private fun NoteEditorDialog(note: Note?, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (note != null) "Edit Note" else "New Note", fontWeight = FontWeight.Bold) },
        containerColor = NovaCard,
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth(), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NovaBlue))
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Content (Markdown supported)") }, modifier = Modifier.fillMaxWidth().height(200.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NovaBlue))
            }
        },
        confirmButton = { Button(onClick = { if (title.isNotBlank()) onSave(title, content) }, colors = ButtonDefaults.buttonColors(containerColor = NovaBlue)) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", color = NovaTextSecondary) } }
    )
}
