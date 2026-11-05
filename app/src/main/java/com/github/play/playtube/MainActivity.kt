package com.github.play.playtube

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import java.io.File

data class VideoItem(
    val id: String,
    val title: String,
    val channel: String,
    val thumbnailUrl: String,
    val videoUrl: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Garantizar inicialización si el sistema mató la app
        try {
            YoutubeDL.getInstance().init(applicationContext)
        } catch (_: Exception) {}

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlayTubeApp(onDownloadClick = { url ->
                        descargarVideo(url)
                    })
                }
            }
        }
    }

    private fun descargarVideo(url: String) {
        val scope = kotlinx.coroutines.CoroutineScope(Dispatchers.IO)
        scope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Iniciando descarga...", Toast.LENGTH_SHORT).show()
            }
            try {
                // Guardar en la carpeta pública de descargas
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val targetDir = File(downloadsDir, "PlayTube")
                if (!targetDir.exists()) targetDir.mkdirs()

                val request = YoutubeDLRequest(url)
                request.addOption("-o", "${targetDir.absolutePath}/%(title)s.%(ext)s")
                request.addOption("-f", "best") // Mejor calidad directa progresiva

                YoutubeDL.getInstance().execute(request) { progress, etaInSeconds, line ->
                    // Callback de progreso
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "¡Descarga guardada en Downloads/PlayTube!", Toast.LENGTH_LONG).show()
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
fun PlayTubeApp(onDownloadClick: (String) -> Unit) {
    var currentTab by remember { mutableStateOf(0) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Datos de demostración
    val feedVideos = remember {
        listOf(
            VideoItem("1", "Big Buck Bunny 4K Oficial", "Blender Foundation", "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?w=600", "https://www.youtube.com/watch?v=aqz-KE-bpKQ"),
            VideoItem("2", "Tutorial de Android Jetpack Compose Expressive", "Android Devs", "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600", "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        )
    }

    val subscriptionVideos = remember {
        listOf(
            VideoItem("3", "Nuevo lanzamiento de tecnología 2026", "Tech News", "https://images.unsplash.com/photo-1518770660439-4636190af475?w=600", "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        )
    }

    Scaffold(
        topBar = {
            if (isSearchActive) {
                TopAppBar(
                    title = {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar en PlayTube...") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            isSearchActive = false
                            searchQuery = ""
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar búsqueda")
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = { Text("PlayTube", style = MaterialTheme.typography.titleLarge) },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Subscriptions, contentDescription = "Suscripciones") },
                    label = { Text("Suscripciones") },
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentTab) {
                0 -> {
                    // Pestaña Inicio
                    val listaFiltrada = if (searchQuery.isNotEmpty()) {
                        feedVideos.filter { it.title.contains(searchQuery, ignoreCase = true) }
                    } else feedVideos

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(listaFiltrada) { video ->
                            VideoCard(video = video, onDownload = { onDownloadClick(video.videoUrl) })
                        }
                    }
                }
                1 -> {
                    // Pestaña Suscripciones
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                "Tus canales favoritos",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(subscriptionVideos) { video ->
                            VideoCard(video = video, onDownload = { onDownloadClick(video.videoUrl) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoCard(
    video: VideoItem,
    onDownload: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        video.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        video.channel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
                FilledTonalIconButton(
                    onClick = onDownload,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Descargar")
                }
            }
        }
    }
}