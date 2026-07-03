package com.novaterm.app.ui.navigation

/**
 * Sealed class representing all navigation destinations in NovaTerm.
 */
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object Terminal : Screen("terminal")
    data object Editor : Screen("editor")
    data object Files : Screen("files")
    data object Git : Screen("git")
    data object Ssh : Screen("ssh")
    data object Packages : Screen("packages")
    data object Downloads : Screen("downloads")
    data object Docs : Screen("docs")
    data object Settings : Screen("settings")
    data object Browser : Screen("browser")
    data object Monitor : Screen("monitor")
    data object Notes : Screen("notes")
    data object Workspace : Screen("workspace")
    data object About : Screen("about")
}
