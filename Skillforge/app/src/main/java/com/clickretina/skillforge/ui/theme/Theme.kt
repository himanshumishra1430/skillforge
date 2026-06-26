package com.clickretina.skillforge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// The brief asks for a fixed light "cream + teal" look, so we intentionally
// ship a single light color scheme rather than following the system dark mode.
private val SkillforgeColors = lightColorScheme(
    primary = TealStrong,
    onPrimary = Color.White,
    secondary = Teal,
    background = Cream,
    onBackground = TextPrimary,
    surface = CardWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDim,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder,
    error = Color(0xFFD2543B)
)

@Composable
fun SkillforgeTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SkillforgeColors,
        typography = SkillforgeTypography,
        content = content
    )
}
