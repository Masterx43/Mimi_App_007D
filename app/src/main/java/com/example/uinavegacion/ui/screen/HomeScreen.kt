package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.ui.theme.Rosado
import com.example.uinavegacion.ui.theme.UINavegacionTheme
import com.example.uinavegacion.R
import com.example.uinavegacion.data.local.storage.UserPreferences
import com.example.uinavegacion.ui.theme.Blanco
import com.example.uinavegacion.ui.theme.LilaOscuro
import com.example.uinavegacion.ui.theme.LilaPri
import com.example.uinavegacion.ui.theme.textoNegro

@Composable // Pantalla Home (sin formularios, solo navegación/diseño)
fun HomeScreen(
    onGoLogin: () -> Unit,   // Acción a Login
    onGoRegister: () -> Unit, // Acción a Registro
    onGoReserve:() -> Unit
) {
    val context = LocalContext.current

    val bg = MaterialTheme.colorScheme.surfaceVariant
    val userPrefs =remember { UserPreferences(context) }
    val isLoggedIn by userPrefs.isLoogedIn.collectAsStateWithLifecycle(false)

    Box( // Contenedor a pantalla completa
        modifier = Modifier
            .fillMaxSize() // Ocupa todo
            .background(Blanco) // Aplica fondo
            .padding(16.dp), // Margen interior
        contentAlignment = Alignment.Center // Centra contenido
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally // Centra hijos
        ) {
            // Cabecera como Row (ejemplo de estructura)
            Row(
                verticalAlignment = Alignment.CenterVertically, // Centra vertical
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Text( // Título Home
                        text = "¡Bienvenidos a MiMi!",
                        style = MaterialTheme.typography.titleLarge, // Estilo título
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center, // Seminegrita
                        color = LilaPri
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Descubre todos nuestros servicios de belleza",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = textoNegro

                    )
                    Spacer(Modifier.width(8.dp)) // Separación horizontal

                }

            }

            Spacer(Modifier.height(20.dp)) // Separación

            // Tarjeta con un mini “hero”
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                       painter = painterResource(id = R.drawable.manicure),
                        contentDescription = "Manicure",
                       modifier = Modifier
                           .fillMaxWidth()
                           .height(180.dp),
                        contentScale = ContentScale.Crop // Ajusta la imagen sin deformarla
                    )

                    Spacer(Modifier.height(12.dp))


                    Text(
                        "Servicio de Manicure y Pedicure",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = LilaOscuro
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Cuidado profesional para tus manos y pies. " +
                                "Disfruta de una experiencia relajante con manicure y pedicure que incluye limpieza, " +
                                "hidratación y acabado impecable para lucir siempre radiante.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(12.dp)) // Separación
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.tratamientos_capilares),
                        contentDescription = "Coloracion y tratamientos capilares",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop // Ajusta la imagen sin deformarla
                    )

                    Spacer(Modifier.height(12.dp))


                    Text(
                        "Servicio de Coloracion y tratamientos capilares",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = LilaOscuro
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Renueva tu estilo con servicios de coloración y tratamientos capilares. " +
                                "Cuida tu cabello mientras obtienes el tono perfecto, brillo duradero e hidratación profunda.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(12.dp)) // Separación
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.brushingystyling),
                        contentDescription = "Brushing y styling",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop // Ajusta la imagen sin deformarla
                    )

                    Spacer(Modifier.height(12.dp))


                    Text(
                        "Servicio de Brushing y styling",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = LilaOscuro
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Logra el look ideal con nuestros servicios de brushing y styling. " +
                                "Desde un liso perfecto hasta ondas definidas, realza tu estilo con acabado profesional y duradero.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(24.dp)) // Separación

            Text(
                "¡Reserve nuestros servicios en Agendar!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = LilaOscuro
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    UINavegacionTheme {
        HomeScreen(
            onGoLogin = {},
            onGoRegister = {},
            onGoReserve = {}
        )
    }
}
