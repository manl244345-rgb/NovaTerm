package com.novaterm.app.ui.screens.terminal

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.novaterm.app.ui.theme.*

data class TerminalLine(
    val text: String,
    val type: LineType = LineType.Output
)

enum class LineType { Command, Output, Error, Info }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    navController: NavController,
    viewModel: TerminalViewModel = hiltViewModel()
) {
    val output by viewModel.output.collectAsState()
    val currentInput by viewModel.currentInput.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val currentDirectory by viewModel.currentDirectory.collectAsState()
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(output.size) {
        if (output.isNotEmpty()) listState.animateScrollToItem(output.lastIndex)
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Terminal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NovaTextPrimary, fontFamily = MonoFontFamily)
                        Text(currentDirectory, style = MaterialTheme.typography.labelSmall, color = NovaCyan, fontFamily = MonoFontFamily, maxLines = 1)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary)
                    }
                },
                actions = {
                    if (isRunning) {
                        IconButton(onClick = { viewModel.killCurrentProcess() }) {
                            Icon(Icons.Default.Stop, "Stop", tint = NovaRed)
                        }
                    }
                    IconButton(onClick = { viewModel.clearOutput() }) {
                        Icon(Icons.Default.DeleteSweep, "Clear", tint = NovaTextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
            )
        },
        containerColor = NovaBlack
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Terminal output
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth().background(NovaBlack).padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(output) { line ->
                    TerminalLineView(line)
                }
                if (isRunning) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 2.dp, color = NovaGreen)
                            Text("Running...", color = NovaGreen, fontSize = 12.sp, fontFamily = MonoFontFamily)
                        }
                    }
                }
            }

            // Input bar
            Surface(color = NovaSurface) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp).navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("❯", color = NovaGreen, fontFamily = MonoFontFamily, fontSize = 14.sp)
                    OutlinedTextField(
                        value = currentInput,
                        onValueChange = viewModel::onInputChanged,
                        modifier = Modifier.weight(1f).focusRequester(focusRequester),
                        placeholder = { Text("Enter command...", color = NovaTextMuted, fontFamily = MonoFontFamily, fontSize = 13.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            imeAction = ImeAction.Send,
                            autoCorrectEnabled = false
                        ),
                        keyboardActions = KeyboardActions(onSend = { viewModel.executeCommand() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NovaBlue.copy(alpha = 0.5f),
                            unfocusedBorderColor = NovaTextMuted.copy(alpha = 0.3f),
                            cursorColor = NovaGreen,
                            focusedTextColor = TerminalWhite,
                            unfocusedTextColor = TerminalWhite
                        ),
                        textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFontFamily, fontSize = 13.sp),
                        shape = RoundedCornerShape(8.dp)
                    )
                    IconButton(
                        onClick = { viewModel.executeCommand() },
                        enabled = currentInput.isNotBlank() && !isRunning
                    ) {
                        Icon(Icons.Default.Send, "Execute", tint = if (currentInput.isNotBlank()) NovaGreen else NovaTextMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun TerminalLineView(line: TerminalLine) {
    val text = buildAnnotatedString {
        when (line.type) {
            LineType.Command -> {
                withStyle(SpanStyle(color = NovaCyan)) { append("❯ ") }
                withStyle(SpanStyle(color = TerminalWhite, fontWeight = FontWeight.Medium)) { append(line.text) }
            }
            LineType.Error -> withStyle(SpanStyle(color = TerminalRed)) { append(line.text) }
            LineType.Info -> withStyle(SpanStyle(color = NovaCyan)) { append(line.text) }
            LineType.Output -> withStyle(SpanStyle(color = NovaTextPrimary)) { append(line.text) }
        }
    }
    Text(text = text, fontFamily = MonoFontFamily, fontSize = 13.sp, lineHeight = 18.sp)
}
