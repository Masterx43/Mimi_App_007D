package com.example.uinavegacion.ui.theme

import android.R
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LilaClaro,
    onPrimary = GrisTexto,
    secondary = LilaOscuro,
    onSecondary = Blanco,
    tertiary = Rosado,
    onTertiary = Blanco,
    background = BackDark,
    surface = SurDark,
    onSurface = Blanco

)

private val LightColorScheme = lightColorScheme(
    primary = LilaOscuro,
    onPrimary = Blanco,
    secondary = LilaClaro,
    onSecondary = GrisTexto,
    tertiary = Rosado,
    onTertiary = Blanco,
    background = LilaClaro,
    surface = LilaClaro,
    onSurface = GrisTexto,
    error = ErrorColor,
    onError = Blanco


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun UINavegacionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S  && dynamicColor-> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}