package com.example.uinavegacion.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.uinavegacion.ui.theme.LilaOscuro
import com.example.uinavegacion.R
import com.example.uinavegacion.ui.theme.LilaPri


@OptIn(ExperimentalMaterial3Api::class)
@Composable // Composable reutilizable: barra superior
fun AppTopBar(
    onOpenDrawer: () -> Unit, // Abre el drawer (hamburguesa)
    onHome: () -> Unit,       // Navega a Home
    onLogin: () -> Unit,      // Navega a Login
    onRegister: () -> Unit,    // Navega a Registro
    onReserve: () -> Unit
) {
    //lo que hace es crear una variable de estado recordada que le dice a la interfaz
    // si el menú desplegable de 3 puntitos debe estar visible (true) o oculto (false).
    var showMenu by remember { mutableStateOf(false) } // Estado del menú overflow

    Surface(
        tonalElevation = 4.dp,
        color = LilaPri,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(
            bottomStart = 32.dp,
            bottomEnd = 32.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)

    ) {

        CenterAlignedTopAppBar( // Barra alineada al centro
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White
            ),
            title = { // Slot del título
                Button(onClick = onHome,
                    colors= ButtonDefaults.buttonColors(
                        containerColor = LilaPri),
                    modifier = Modifier.height(80.dp)) {

                    Image(
                        painter = painterResource(id = R.drawable.mimi_logo),
                        contentDescription = "Logo Mimi",
                        modifier = Modifier
                            .fillMaxHeight() // ajusta tamaño del logo
                            .padding( top = 3.dp)


                    )
                }
            },
            navigationIcon = { // Ícono a la izquierda (hamburguesa)
                IconButton(onClick = onOpenDrawer) { // Al presionar, abre drawer
                    Icon(imageVector = Icons.Filled.Menu,
                        contentDescription = "Menú",
                        tint = Color.White) // Ícono
                }
            },
            actions = { // Acciones a la derecha (íconos + overflow)
                IconButton(onClick = onHome) { // Ir a Home
                    Icon(Icons.Filled.Home,
                        contentDescription = "Home",
                        tint = Color.White)// Ícono Home
                }
                IconButton(onClick = onReserve) {
                    Icon(Icons.Filled.DateRange,
                        contentDescription = "Agendar",
                        tint = Color.White)
                }
                DropdownMenu(
                    expanded = showMenu, // Si está abierto
                    onDismissRequest = { showMenu = false } // Cierra al tocar fuera
                ) {
                    DropdownMenuItem( // Opción Home
                        text = { Text("Home") }, // Texto opción
                        onClick = { showMenu = false; onHome() } // Navega y cierra
                    )
                    DropdownMenuItem( // Opción Login
                        text = { Text("Login") },
                        onClick = { showMenu = false; onLogin() }
                    )
                    DropdownMenuItem( // Opción Registro
                        text = { Text("Registro") },
                        onClick = { showMenu = false; onRegister() }
                    )
                    DropdownMenuItem(
                        text = { Text("Agendar") },
                        onClick = { showMenu = false; onReserve() }
                    )
                    }
            }
        )
    }

}