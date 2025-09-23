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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Box(modifier = modifier.fillMaxSize()) {
        // Fondo borroso
        Image(
            painter = painterResource(id = R.drawable.cover),
            contentDescription = "Fondo borroso",
            modifier = Modifier.fillMaxSize().alpha(0.5f),
            contentScale = ContentScale.Crop
        )

        // Contenido centrado
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

            // Letras solo en horizontal
            if (isLandscape) {
                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier.height(200.dp).width(400.dp)) {
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
            }
        }

        // Botones y barra de reproducción en la parte inferior
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Barra de reproducción
            Slider(
                value = 0.3f,
                onValueChange = {},
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
                Text("0:00", fontWeight = FontWeight.Bold)
                Text("-0:00", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de reproducción
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.ic_previous), contentDescription = "Previous")
                }
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.ic_play), contentDescription = "Play/Pause")
                }
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.ic_arrow1), contentDescription = "Next")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones adicionales
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

        // Volumen vertical (en el lateral derecho)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(60.dp)
                .align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center
        ) {
            Slider(
                value = 0.5f,
                onValueChange = {},
                valueRange = 0f..1f,
                modifier = Modifier
                    .height(200.dp)
                    .rotate(-90f),
                colors = SliderDefaults.colors(
                    activeTrackColor = Color(0xFFE89449)
                )
            )
        }
    }
}