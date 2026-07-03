package com.novaterm.app.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.novaterm.app.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val accent: androidx.compose.ui.graphics.Color
)

private val onboardingPages = listOf(
    OnboardingPage(
        Icons.Default.Terminal,
        "Welcome to NovaTerm",
        "The most complete Linux workstation and developer environment for Android. Professional-grade tools in your pocket.",
        NovaBlue
    ),
    OnboardingPage(
        Icons.Default.Code,
        "Powerful Terminal",
        "Full-featured terminal emulator with multi-tab support, split views, ANSI colors, and a comprehensive shell environment.",
        NovaCyan
    ),
    OnboardingPage(
        Icons.Default.Edit,
        "Code Editor",
        "Syntax highlighting for 30+ languages, code folding, search & replace, minimap, and seamless terminal integration.",
        NovaPurple
    ),
    OnboardingPage(
        Icons.Default.FolderOpen,
        "File Manager",
        "Desktop-class file manager with grid and list views, archive support, bookmarks, and quick navigation.",
        NovaGreen
    ),
    OnboardingPage(
        Icons.Default.Hub,
        "Git & SSH",
        "Full Git client with visual diff, branch management, and a professional SSH manager with SFTP support.",
        NovaAmber
    ),
    OnboardingPage(
        Icons.Default.Inventory,
        "Nova Packages",
        "Integrated package manager. Install Python, Node.js, Rust, Go, and hundreds of developer tools with one tap.",
        NovaPink
    ),
    OnboardingPage(
        Icons.Default.Palette,
        "Themes & Customization",
        "Fully customizable themes, terminal colors, fonts, and layouts. Make NovaTerm truly yours.",
        NovaBlue
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovaBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Skip button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.End
            ) {
                if (pagerState.currentPage < onboardingPages.lastIndex) {
                    TextButton(onClick = onFinish) {
                        Text("Skip", color = NovaTextSecondary)
                    }
                }
            }

            // Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPageContent(page = onboardingPages[page])
            }

            // Indicators + buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 48.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Page indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(onboardingPages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateDpAsState(if (isSelected) 24.dp else 8.dp, label = "indicator")
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) onboardingPages[pagerState.currentPage].accent
                                    else NovaSurfaceVariant
                                )
                        )
                    }
                }

                // Navigation buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (pagerState.currentPage > 0) {
                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, NovaTextMuted)
                        ) {
                            Text("Back", color = NovaTextSecondary)
                        }
                    }

                    Button(
                        onClick = {
                            if (pagerState.currentPage < onboardingPages.lastIndex) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                onFinish()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = onboardingPages[pagerState.currentPage].accent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            if (pagerState.currentPage < onboardingPages.lastIndex) "Next" else "Get Started",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(page.accent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = page.accent
            )
        }

        Spacer(Modifier.height(40.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = NovaTextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = NovaTextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
    }
}
