package com.github.play.playtube

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlayTubeHome(onDownloadClick = { url ->
                        ejecutarDescargaYtDlp(url)
                    })
                }
            }
        }
    }

    private fun ejecutarDescargaYtDlp(url: String) {
        val scope = kotlinx.coroutines.CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val request = YoutubeDLRequest(url)
                val directorio = getExternalFilesDir(null)?.absolutePath
                request.addOption("-o", "$directorio/%(title)s.%(ext)s")
                
                YoutubeDL.getInstance().execute(request) { progress, etaInSeconds, line ->
                    // Progreso de descarga
                }
                
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Descarga completada", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayTubeHome(onDownloadClick: (String) -> Unit) {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PlayTube", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { /* Abrir buscador */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Subscriptions, contentDescription = "Suscripciones") },
                    label = { Text("Suscripciones") },
                    selected = selectedItem == 1,
                    onClick = { selectedItem = 1 }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Video Card Demo
            item {
                VideoCard(
                    title = "Video de Prueba - PlayTube",
                    channel = "Mi Canal",
                    thumbnailUrl = "https://picsum.photos/600/340",
                    onDownload = {
                        onDownloadClick("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
                    }
                )
            }
        }
    }
}

@Composable
fun VideoCard(
    title: String,
    channel: String,
    thumbnailUrl: String,
    onDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    Text(channel, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                IconButton(onClick = onDownload) {
                    Icon(Icons.Default.Download, contentDescription = "Descargar")
                }
            }
        }
    }
}