package com.example.uinavegacion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EstadoChip(estado: String) {

    val color = when (estado) {
        "Pendiente" -> Color(0xFFFFC107)
        "Completado" -> Color(0xFF4CAF50)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(estado, color = Color.White)
    }
}
