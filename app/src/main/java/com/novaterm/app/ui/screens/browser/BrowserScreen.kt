package com.novaterm.app.ui.screens.browser

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.novaterm.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(navController: NavController) {
    var currentUrl by remember { mutableStateOf("https://devdocs.io") }
    var addressBarText by remember { mutableStateOf(currentUrl) }
    var isLoading by remember { mutableStateOf(false) }
    var loadingProgress by remember { mutableStateOf(0) }
    var pageTitle by remember { mutableStateOf("DevDocs") }
    var webView: WebView? by remember { mutableStateOf(null) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(pageTitle, style = MaterialTheme.typography.titleSmall, color = NovaTextPrimary, maxLines = 1) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary)
                        }
                    },
                    actions = {
                        IconButton(onClick = { webView?.reload() }) {
                            Icon(if (isLoading) Icons.Default.Close else Icons.Default.Refresh, "Reload", tint = NovaTextSecondary)
                        }
                        IconButton(onClick = { /* Bookmark */ }) {
                            Icon(Icons.Default.BookmarkBorder, "Bookmark", tint = NovaTextSecondary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
                )

                // Address bar
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Lock, null, tint = NovaGreen, modifier = Modifier.size(16.dp))
                    OutlinedTextField(
                        value = addressBarText,
                        onValueChange = { addressBarText = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(onGo = {
                            val url = if (addressBarText.startsWith("http")) addressBarText
                            else if (addressBarText.contains(".") && !addressBarText.contains(" ")) "https://$addressBarText"
                            else "https://duckduckgo.com/?q=${addressBarText.replace(" ", "+")}"
                            currentUrl = url
                            addressBarText = url
                            webView?.loadUrl(url)
                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NovaBlue.copy(0.5f),
                            unfocusedBorderColor = NovaTextMuted.copy(0.3f),
                            cursorColor = NovaBlue
                        ),
                        textStyle = MaterialTheme.typography.bodySmall.copy(color = NovaTextPrimary)
                    )
                    IconButton(onClick = { webView?.goBack() }, enabled = webView?.canGoBack() == true) {
                        Icon(Icons.Default.ChevronLeft, "Back", tint = if (webView?.canGoBack() == true) NovaTextSecondary else NovaTextMuted)
                    }
                    IconButton(onClick = { webView?.goForward() }, enabled = webView?.canGoForward() == true) {
                        Icon(Icons.Default.ChevronRight, "Forward", tint = if (webView?.canGoForward() == true) NovaTextSecondary else NovaTextMuted)
                    }
                }

                if (isLoading) {
                    LinearProgressIndicator(
                        progress = { loadingProgress / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = NovaBlue,
                        trackColor = NovaSurface
                    )
                }
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = false
                        useWideViewPort = true
                        loadWithOverviewMode = true
                    }
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            isLoading = true
                            url?.let { addressBarText = it }
                        }
                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading = false
                            pageTitle = view?.title ?: "Browser"
                        }
                    }
                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            loadingProgress = newProgress
                        }
                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            pageTitle = title ?: "Browser"
                        }
                    }
                    loadUrl(currentUrl)
                    webView = this
                }
            },
            modifier = Modifier.fillMaxSize().padding(padding)
        )
    }
}
