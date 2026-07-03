package com.novaterm.app.ui.screens.docs

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

data class DocCategory(val id: String, val name: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val articleCount: Int, val color: androidx.compose.ui.graphics.Color)
data class DocArticle(val id: String, val title: String, val category: String, val summary: String)

private val docCategories = listOf(
    DocCategory("linux", "Linux Commands", Icons.Default.Terminal, 120, NovaBlue),
    DocCategory("git", "Git", Icons.Default.Hub, 45, NovaPink),
    DocCategory("bash", "Bash Scripting", Icons.Default.Code, 60, NovaGreen),
    DocCategory("python", "Python", Icons.Default.Analytics, 85, NovaCyan),
    DocCategory("networking", "Networking", Icons.Default.Wifi, 30, NovaAmber),
    DocCategory("android", "Android Dev", Icons.Default.PhoneAndroid, 40, NovaPurple),
    DocCategory("markdown", "Markdown", Icons.Default.Description, 15, NovaBlue),
    DocCategory("databases", "Databases", Icons.Default.Storage, 35, NovaCyan),
)

private val recentArticles = listOf(
    DocArticle("1", "ls — List directory contents", "Linux Commands", "List information about files in the current directory."),
    DocArticle("2", "git commit — Record changes", "Git", "Create a new commit containing the current contents of the index."),
    DocArticle("3", "chmod — Change file permissions", "Linux Commands", "Change the access permissions of file system objects."),
    DocArticle("4", "grep — Search text patterns", "Linux Commands", "Search for patterns in files using regular expressions."),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<DocCategory?>(null) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Documentation", fontWeight = FontWeight.Bold, color = NovaTextPrimary) },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (selectedCategory != null) selectedCategory = null else navController.popBackStack()
                        }) { Icon(Icons.Default.ArrowBack, "Back", tint = NovaTextSecondary) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = NovaSurface)
                )
                OutlinedTextField(
                    value = searchQuery, onValueChange = { searchQuery = it },
                    placeholder = { Text("Search documentation...", color = NovaTextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = NovaTextMuted) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NovaBlue, unfocusedBorderColor = NovaTextMuted.copy(0.3f)),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        containerColor = NovaBlack
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (selectedCategory == null && searchQuery.isEmpty()) {
                item { Text("Categories", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary) }
                item {
                    val chunked = docCategories.chunked(2)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        chunked.forEach { row ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEach { cat ->
                                    DocCategoryCard(modifier = Modifier.weight(1f), category = cat, onClick = { selectedCategory = cat })
                                }
                                if (row.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
                item { Text("Recent Articles", style = MaterialTheme.typography.titleSmall, color = NovaTextSecondary) }
                items(recentArticles) { article -> DocArticleCard(article) }
            } else {
                val articles = recentArticles.filter {
                    searchQuery.isEmpty() || it.title.contains(searchQuery, true) || it.summary.contains(searchQuery, true)
                }
                if (articles.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No articles found", color = NovaTextSecondary)
                        }
                    }
                } else {
                    items(articles) { article -> DocArticleCard(article) }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun DocCategoryCard(modifier: Modifier, category: DocCategory, onClick: () -> Unit) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp), onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(category.icon, null, tint = category.color, modifier = Modifier.size(28.dp))
            Text(category.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = NovaTextPrimary)
            Text("${category.articleCount} articles", style = MaterialTheme.typography.labelSmall, color = NovaTextSecondary)
        }
    }
}

@Composable
private fun DocArticleCard(article: DocArticle) {
    Card(colors = CardDefaults.cardColors(containerColor = NovaCard), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(article.category, style = MaterialTheme.typography.labelSmall, color = NovaBlue)
            Text(article.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = NovaTextPrimary, fontFamily = MonoFontFamily)
            Text(article.summary, style = MaterialTheme.typography.bodySmall, color = NovaTextSecondary)
        }
    }
}
