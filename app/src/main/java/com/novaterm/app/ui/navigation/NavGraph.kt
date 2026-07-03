package com.novaterm.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.novaterm.app.ui.screens.about.AboutScreen
import com.novaterm.app.ui.screens.browser.BrowserScreen
import com.novaterm.app.ui.screens.docs.DocsScreen
import com.novaterm.app.ui.screens.downloads.DownloadsScreen
import com.novaterm.app.ui.screens.editor.EditorScreen
import com.novaterm.app.ui.screens.files.FilesScreen
import com.novaterm.app.ui.screens.git.GitScreen
import com.novaterm.app.ui.screens.home.HomeScreen
import com.novaterm.app.ui.screens.monitor.SystemMonitorScreen
import com.novaterm.app.ui.screens.notes.NotesScreen
import com.novaterm.app.ui.screens.onboarding.OnboardingScreen
import com.novaterm.app.ui.screens.packages.PackagesScreen
import com.novaterm.app.ui.screens.settings.SettingsScreen
import com.novaterm.app.ui.screens.splash.SplashScreen
import com.novaterm.app.ui.screens.ssh.SshScreen
import com.novaterm.app.ui.screens.terminal.TerminalScreen
import com.novaterm.app.ui.screens.workspace.WorkspaceScreen

@Composable
fun NavGraph(startDestination: String = Screen.Splash.route) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Terminal.route) {
            TerminalScreen(navController = navController)
        }

        composable(Screen.Editor.route) {
            EditorScreen(navController = navController)
        }

        composable(Screen.Files.route) {
            FilesScreen(navController = navController)
        }

        composable(Screen.Git.route) {
            GitScreen(navController = navController)
        }

        composable(Screen.Ssh.route) {
            SshScreen(navController = navController)
        }

        composable(Screen.Packages.route) {
            PackagesScreen(navController = navController)
        }

        composable(Screen.Downloads.route) {
            DownloadsScreen(navController = navController)
        }

        composable(Screen.Docs.route) {
            DocsScreen(navController = navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        composable(Screen.Browser.route) {
            BrowserScreen(navController = navController)
        }

        composable(Screen.Monitor.route) {
            SystemMonitorScreen(navController = navController)
        }

        composable(Screen.Notes.route) {
            NotesScreen(navController = navController)
        }

        composable(Screen.Workspace.route) {
            WorkspaceScreen(navController = navController)
        }

        composable(Screen.About.route) {
            AboutScreen(navController = navController)
        }
    }
}
