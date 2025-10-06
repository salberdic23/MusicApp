package com.example.musicapp

import android.os.Bundle
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.example.musicapp.ui.theme.MusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MusicPlayerScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MusicPlayerScreen(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var volume by remember { mutableStateOf(0.5f) }
    val duration = 180f

    LaunchedEffect(key1 = isPlaying) {
        if (isPlaying) {
            while (isActive && progress < 1f) {
                delay(1000)
                progress += 1f / duration
            }
            if (progress >= 1f) isPlaying = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cover),
            contentDescription = "Fondo borroso",
            modifier = Modifier.fillMaxSize().alpha(0.5f),
            contentScale = ContentScale.Crop
        )

        if (isLandscape) {
            // 🌄 Landscape layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cover),
                        contentDescription = "Album Cover",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Vetements", fontSize = 16.sp, color = Color.Black)
                    Text("Eladio Carrion, Myke Towers", fontSize = 16.sp, color = Color.Black)
                }

                Column(
                    modifier = Modifier.weight(2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.height(160.dp).fillMaxWidth()) {
                        Text(
                            text = """
                                Yeah
                                Si vieran el contrato de New Balance
                                Entendieras por qué no uso Nike ya
                                Si el miedo va en la gaveta tengo gaveteros como Ikea
                                Están a paso de tortuga no Leonardo ni Mikey na
                            """.trimIndent(),
                            fontSize = 16.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Slider(
                        value = progress,
                        onValueChange = { progress = it },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Transparent,
                            activeTrackColor = Color(0xFFE89449)
                        ),
                        modifier = Modifier.width(300.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val currentTime = (progress * duration).toInt()
                        val remainingTime = (duration - currentTime).toInt()
                        Text("${currentTime / 60}:${(currentTime % 60).toString().padStart(2, '0')}", fontWeight = FontWeight.Bold)
                        Text("-${remainingTime / 60}:${(remainingTime % 60).toString().padStart(2, '0')}", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {}) {
                            Icon(painterResource(R.drawable.ic_previous), contentDescription = "Previous")
                        }
                        IconButton(onClick = { isPlaying = !isPlaying }) {
                            val icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                            Icon(painterResource(icon), contentDescription = "Play/Pause")
                        }
                        IconButton(onClick = {}) {
                            Icon(painterResource(R.drawable.ic_arrow1), contentDescription = "Next")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {}) {
                            Icon(painterResource(R.drawable.zerrenda), contentDescription = "Lista")
                        }
                        IconButton(onClick = {}) {
                            Icon(painterResource(R.drawable.share), contentDescription = "Compartir")
                        }
                        IconButton(onClick = {}) {
                            Icon(painterResource(R.drawable.letrak), contentDescription = "Letra")
                        }
                    }
                }

                // Volumen vertical
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Slider(
                        value = volume,
                        onValueChange = { volume = it },
                        modifier = Modifier
                            .height(300.dp)
                            .rotate(-90f),
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color(0xFFE89449)
                        )
                    )
                }
            }
        } else {
            // 📱 Portrait layout (igual que antes)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cover),
                    contentDescription = "Album Cover",
                    modifier = Modifier.size(250.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Vetements", fontSize = 16.sp, color = Color.Black)
                Text("Eladio Carrion, Myke Towers", fontSize = 16.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(24.dp))

                Slider(
                    value = progress,
                    onValueChange = { progress = it },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Transparent,
                        activeTrackColor = Color(0xFFE89449)
                    ),
                    modifier = Modifier.width(240.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val currentTime = (progress * duration).toInt()
                    val remainingTime = (duration - currentTime).toInt()
                    Text("${currentTime / 60}:${(currentTime % 60).toString().padStart(2, '0')}", fontWeight = FontWeight.Bold)
                    Text("-${remainingTime / 60}:${(remainingTime % 60).toString().padStart(2, '0')}", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = {}) {
                        Icon(painterResource(R.drawable.ic_previous), contentDescription = "Previous")
                    }
                    IconButton(onClick = { isPlaying = !isPlaying }) {
                        val icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                        Icon(painterResource(icon), contentDescription = "Play/Pause")
                    }
                    IconButton(onClick = {}) {
                        Icon(painterResource(R.drawable.ic_arrow1), contentDescription = "Next")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = {}) {
                        Icon(painterResource(R.drawable.zerrenda), contentDescription = "Lista")
                    }
                    IconButton(onClick = {}) {
                        Icon(painterResource(R.drawable.share), contentDescription = "Compartir")
                    }
                    IconButton(onClick = {}) {
                        Icon(painterResource(R.drawable.letrak), contentDescription = "Letra")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Volumen en retrato
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Slider(
                        value = volume,
                        onValueChange = { volume = it },
                        modifier = Modifier
                            .width(300.dp),
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color(0xFFE89449)
                        )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicPlayerScreen(modifier = Modifier.padding())
}