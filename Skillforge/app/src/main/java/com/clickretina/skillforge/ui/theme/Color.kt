package com.clickretina.skillforge.ui.theme

import androidx.compose.ui.graphics.Color

// Exact tokens pulled from the Skillforge design (light "cream + teal" theme).
val Cream = Color(0xFFFBFAF8)        // app background
val CardWhite = Color(0xFFFFFFFF)    // cards
val CardBorder = Color(0xFFECEBE6)   // hairline card borders
val SearchHint = Color(0xFFA6A49C)

val Teal = Color(0xFF2DD4BF)         // primary accent / Android category
val TealStrong = Color(0xFF14B8A6)   // level label / primary
val TealLink = Color(0xFF0FB5A4)     // "See all" links
val Emerald = Color(0xFF34D399)      // Backend category
val Amber = Color(0xFFFBBF24)        // Design category

val TextPrimary = Color(0xFF1A1A1A)
val TextSecondary = Color(0xFF6B6B62)
val TextMuted = Color(0xFF9A9890)
val StarGold = Color(0xFFF5A623)

val LockGrey = Color(0xFF9A9890)
val SurfaceDim = Color(0xFFF1F0EC)

/** Parses a "#RRGGBB" string from the API into a Compose [Color], with a teal fallback. */
fun parseHexColor(hex: String): Color = try {
    Color(android.graphics.Color.parseColor(hex))
} catch (e: IllegalArgumentException) {
    Teal
}
