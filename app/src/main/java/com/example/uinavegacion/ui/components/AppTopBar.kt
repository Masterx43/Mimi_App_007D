package com.example.uinavegacion.ui.components


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.uinavegacion.ui.theme.LilaOscuro
import com.example.uinavegacion.R
import com.example.uinavegacion.data.local.storage.UserPreferences
import com.example.uinavegacion.ui.theme.LilaPri


@OptIn(ExperimentalMaterial3Api::class)
@Composable // Composable reutilizable: barra superior
fun AppTopBar(
    onHome: () -> Unit       // Navega a Home
) {


    Surface(
        color = LilaPri,
        tonalElevation = 8.dp, // sombra más suave y visible
        shadowElevation = 8.dp, // añade profundidad visual
        shape = RoundedCornerShape(
            bottomStart = 30.dp,
            bottomEnd = 30.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp) //
            .statusBarsPadding()

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
                        painter = painterResource(id = R.drawable.logotopbarmimi),
                        contentDescription = "Logo Mimi",
                        modifier = Modifier
                            .fillMaxHeight() // ajusta tamaño del logo
                            .padding( horizontal = 8.dp)


                    )
                }
            },
            actions = { // Acciones a la derecha (íconos + overflow)
                IconButton(onClick = onHome) { // Ir a Home
                    Icon(Icons.Filled.Home,
                        contentDescription = "Home",
                        tint = Color.White)// Ícono Home
                }
            }
        )
    }

}