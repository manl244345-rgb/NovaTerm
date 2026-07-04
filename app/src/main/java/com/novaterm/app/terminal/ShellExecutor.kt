package com.novaterm.app.terminal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class CommandResult(val output: String, val error: String, val exitCode: Int)

/**
 * Executes shell commands within the app's sandbox environment.
 * Uses ProcessBuilder for real command execution.
 */
@Singleton
class ShellExecutor @Inject constructor() {

    private var currentDirectory: File = File(System.getProperty("user.home") ?: "/data/data/com.novaterm.app")
    private var currentProcess: Process? = null
    private val commandHistory = mutableListOf<String>()

    fun getCurrentDirectory(): String = currentDirectory.absolutePath

    suspend fun execute(command: String): CommandResult = withContext(Dispatchers.IO) {
        commandHistory.add(command)

        // Handle built-in commands
        when {
            command == "help" -> return@withContext handleHelp()
            command == "clear" -> return@withContext CommandResult("", "", 0)
            command.startsWith("cd ") -> return@withContext handleCd(command.removePrefix("cd ").trim())
            command == "cd" -> return@withContext handleCd(System.getProperty("user.home") ?: currentDirectory.absolutePath)
            command == "pwd" -> return@withContext CommandResult(currentDirectory.absolutePath, "", 0)
            command == "history" -> return@withContext CommandResult(commandHistory.joinToString("\n"), "", 0)
            command == "exit" -> return@withContext CommandResult("Use the back button to close the terminal.", "", 0)
        }

        return@withContext try {
            val processBuilder = ProcessBuilder()
                .command("sh", "-c", command)
                .directory(currentDirectory)
                .redirectErrorStream(false)

            processBuilder.environment().apply {
                put("HOME", currentDirectory.absolutePath)
                put("TERM", "xterm-256color")
                put("PATH", "${System.getenv("PATH")}:/system/bin:/system/xbin")
                put("PWD", currentDirectory.absolutePath)
            }

            val process = processBuilder.start()
            currentProcess = process

            // Drain stdout and stderr concurrently to prevent pipe-buffer deadlock:
            // if either stream fills its OS buffer while we're blocking on the other,
            // the child process stalls and waitFor() hangs indefinitely.
            val (output, error) = coroutineScope {
                val outDeferred = async { process.inputStream.bufferedReader().readText() }
                val errDeferred = async { process.errorStream.bufferedReader().readText() }
                Pair(outDeferred.await(), errDeferred.await())
            }
            val exitCode = process.waitFor()
            currentProcess = null

            CommandResult(output.trimEnd(), error.trimEnd(), exitCode)
        } catch (e: Exception) {
            CommandResult("", "Error executing command: ${e.message}", 1)
        }
    }

    private fun handleCd(path: String): CommandResult {
        val target = when {
            path.startsWith("/") -> File(path)
            path == "~" -> File(System.getProperty("user.home") ?: currentDirectory.absolutePath)
            path == ".." -> currentDirectory.parentFile ?: currentDirectory
            path == "." -> currentDirectory
            else -> File(currentDirectory, path)
        }

        return if (target.exists() && target.isDirectory) {
            currentDirectory = target.canonicalFile
            CommandResult("", "", 0)
        } else {
            CommandResult("", "cd: $path: No such directory", 1)
        }
    }

    private fun handleHelp(): CommandResult {
        val help = """
NovaTerm Built-in Commands:
  help          — Show this help message
  clear         — Clear the terminal output
  cd <path>     — Change directory
  pwd           — Print working directory
  history       — Show command history
  exit          — Exit terminal (use back button)

Standard Shell Commands (via sh):
  ls, cat, mkdir, rm, cp, mv, chmod, grep, find, ps, kill
  echo, env, export, which, whereis, touch, stat
  
Nova Package Manager:
  nova install <package>   — Install a package
  nova remove <package>    — Remove a package
  nova search <keyword>    — Search packages
  nova list                — List installed packages
  nova update              — Update package lists
  nova doctor              — Check environment health

For full Linux command reference, see the Documentation section.
        """.trimIndent()
        return CommandResult(help, "", 0)
    }

    fun killCurrent() {
        currentProcess?.destroy()
        currentProcess = null
    }

    fun getHistory(): List<String> = commandHistory.toList()
}
