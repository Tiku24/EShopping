package com.example.eshopping.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val darkColorScheme = AppColorScheme(
    background = Color(239, 236, 236, 255), // Dark gray-black
    onBackground = Color(239, 236, 236, 255), // White text/icons on dark background
    primary = Color(0xFF000000), // Black buttons and UI elements
    onPrimary = Color(0xFFFFFFFF), // White text/icons on primary elements
    secondary = Color(0xFF837F7F), // Light gray for cards or containers
    onSecondary = Color(0xFF000000), // Black text/icons on secondary elements
    priceColor = Color(0xFFF08080)
)

private val lightColorScheme = AppColorScheme(
    background = Color(239, 236, 236, 255), // Pure white background
    onBackground = Color(239, 236, 236, 255), // Black text/icons for contrast
    primary = Color(0xFF000000), // Black buttons and main elements
    onPrimary = Color(0xFFFFFFFF), // White text/icons on primary elements
    secondary = Color(0xFF837F7F), // Light gray for cards and sections
    onSecondary = Color(0xFF000000), // Black text/icons on secondary elements
    priceColor = Color(0xFFF08080)
)

private val typography = AppTypography(
    titleLarge = TextStyle(
        fontFamily = MontSerrat,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleNormal = TextStyle(
        fontFamily = MontSerrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    paragraph = TextStyle(
        fontFamily = MontSerrat,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = MontSerrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    labelNormal = TextStyle(
        fontFamily = MontSerrat,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = MontSerrat,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    flashTitle = TextStyle(
        fontFamily = MontserratMedium,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    labelRow = TextStyle(
        fontFamily = MontSerrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    cartSubTitle = TextStyle(
        fontSize = 10.sp,
        fontFamily = MontSerrat,
        fontWeight = FontWeight.Normal
    )
)

private val shape = AppShape(
    container = RoundedCornerShape(15.dp),
    button = RoundedCornerShape(50)
)

private val size = AppSize(
    larger = 32.dp,
    large = 24.dp,
    medium = 16.dp,
    normal = 12.dp,
    small = 8.dp
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme
    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shape,
        LocalAppSize provides size,
        content = content
    )
}

object AppTheme {

    val colorScheme: AppColorScheme
        @Composable get() = LocalAppColorScheme.current
    val typography: AppTypography
        @Composable get() = LocalAppTypography.current
    val shape: AppShape
        @Composable get() = LocalAppShape.current
    val sizes: AppSize
        @Composable get() = LocalAppSize.current
}