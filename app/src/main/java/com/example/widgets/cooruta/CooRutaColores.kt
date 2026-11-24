package com.example.widgets.cooruta

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.glance.material3.ColorProviders

/**
 * Paleta de CooRuta (verde azulado) para el widget, en claro y oscuro.
 * Se usa con GlanceTheme gracias a glance-material3.
 */
val CooRutaColores = ColorProviders(
    light = lightColorScheme(
        primary = Color(0xFF006B5D),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF7FF8E1),
        onPrimaryContainer = Color(0xFF00201B),
        secondaryContainer = Color(0xFFCDE8E0),
        onSecondaryContainer = Color(0xFF06201A),
        background = Color(0xFFF5FBF8),
        onBackground = Color(0xFF171D1B),
        surface = Color(0xFFF5FBF8),
        onSurface = Color(0xFF171D1B),
        surfaceVariant = Color(0xFFDBE5E1),
        onSurfaceVariant = Color(0xFF3F4946),
        outline = Color(0xFF6F7976)
    ),
    dark = darkColorScheme(
        primary = Color(0xFF61DBC5),
        onPrimary = Color(0xFF00382F),
        primaryContainer = Color(0xFF005045),
        onPrimaryContainer = Color(0xFF7FF8E1),
        secondaryContainer = Color(0xFF334B45),
        onSecondaryContainer = Color(0xFFCDE8E0),
        background = Color(0xFF0E1513),
        onBackground = Color(0xFFDEE4E1),
        surface = Color(0xFF0E1513),
        onSurface = Color(0xFFDEE4E1),
        surfaceVariant = Color(0xFF3F4946),
        onSurfaceVariant = Color(0xFFBEC9C5),
        outline = Color(0xFF899390)
    )
)
