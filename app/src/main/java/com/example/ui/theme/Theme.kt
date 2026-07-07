package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF818CF8), // Indigo 400
    onPrimary = Color(0xFF1E1B4B), // Indigo 950
    primaryContainer = Color(0xFF312E81), // Indigo 900
    onPrimaryContainer = Color(0xFFE0E7FF), // Indigo 100
    secondary = Color(0xFF34D399), // Emerald 400
    onSecondary = Color(0xFF064E3B), // Emerald 950
    secondaryContainer = Color(0xFF065F46), // Emerald 800
    onSecondaryContainer = Color(0xFFD1FAE5), // Emerald 100
    background = Color(0xFF0F172A), // Slate 900
    onBackground = Color(0xFFF8FAFC), // Slate 50
    surface = Color(0xFF1E293B), // Slate 800 / dark cards
    onSurface = Color(0xFFF8FAFC), // Slate 50
    surfaceVariant = Color(0xFF334155), // Slate 700
    onSurfaceVariant = Color(0xFFCBD5E1), // Slate 300
    outline = Color(0xFF475569) // Slate 600
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFF4F46E5), // Indigo 600
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF), // Indigo 100
    onPrimaryContainer = Color(0xFF312E81), // Indigo 900
    secondary = Color(0xFF10B981), // Emerald 500 / 600
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1FAE5), // Emerald 100
    onSecondaryContainer = Color(0xFF065F46), // Emerald 800
    background = Color(0xFFF7F9FC), // Soft Slate-blue / light grey bg
    onBackground = Color(0xFF0F172A), // Slate 900
    surface = Color.White, // White cards
    onSurface = Color(0xFF0F172A), // Slate 900
    surfaceVariant = Color(0xFFF1F5F9), // Slate 100
    onSurfaceVariant = Color(0xFF475569), // Slate 600
    outline = Color(0xFFE2E8F0) // Slate 200 / border color
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
