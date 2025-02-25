package com.example.eshopping.utils

import androidx.compose.ui.graphics.Color


val colorMap = mapOf(
    "red" to Color.Red,
    "blue" to Color.Blue,
    "black" to Color.Black,
    "white" to Color.White,
    "green" to Color.Green,
    "yellow" to Color.Yellow,
    "gray" to Color.Gray,
    "cyan" to Color.Cyan
)

fun getColorFromName(colorName: String): Color {
    return colorMap[colorName] ?: Color.Gray // Default to Gray if not found
}