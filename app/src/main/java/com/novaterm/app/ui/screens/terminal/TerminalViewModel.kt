package com.novaterm.app.ui.screens.terminal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novaterm.app.terminal.ShellExecutor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TerminalViewModel @Inject constructor(
    private val shellExecutor: ShellExecutor
) : ViewModel() {

    private val _output = MutableStateFlow<List<TerminalLine>>(emptyList())
    val output: StateFlow<List<TerminalLine>> = _output.asStateFlow()

    private val _currentInput = MutableStateFlow("")
    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _currentDirectory = MutableStateFlow(shellExecutor.getCurrentDirectory())
    val currentDirectory: StateFlow<String> = _currentDirectory.asStateFlow()

    private val commandHistory = mutableListOf<String>()

    init {
        addLine(TerminalLine("NovaTerm v1.0.0 — Linux Workstation for Android", LineType.Info))
        addLine(TerminalLine("Type 'help' for available commands.", LineType.Info))
        addLine(TerminalLine("", LineType.Output))
    }

    fun onInputChanged(value: String) {
        _currentInput.value = value
    }

    fun executeCommand() {
        val command = _currentInput.value.trim()
        if (command.isEmpty()) return

        commandHistory.add(command)
        addLine(TerminalLine(command, LineType.Command))
        _currentInput.value = ""
        _isRunning.value = true

        viewModelScope.launch {
            try {
                val result = shellExecutor.execute(command)
                if (result.output.isNotEmpty()) {
                    result.output.lines().forEach { line ->
                        addLine(TerminalLine(line, LineType.Output))
                    }
                }
                if (result.error.isNotEmpty()) {
                    result.error.lines().filter { it.isNotEmpty() }.forEach { line ->
                        addLine(TerminalLine(line, LineType.Error))
                    }
                }
                _currentDirectory.value = shellExecutor.getCurrentDirectory()
            } catch (e: Exception) {
                addLine(TerminalLine("Error: ${e.message}", LineType.Error))
            } finally {
                _isRunning.value = false
            }
        }
    }

    fun killCurrentProcess() {
        shellExecutor.killCurrent()
        _isRunning.value = false
        addLine(TerminalLine("^C", LineType.Error))
    }

    fun clearOutput() {
        _output.value = emptyList()
        addLine(TerminalLine("NovaTerm v1.0.0 — cleared", LineType.Info))
    }

    private fun addLine(line: TerminalLine) {
        _output.value = _output.value + line
    }
}
