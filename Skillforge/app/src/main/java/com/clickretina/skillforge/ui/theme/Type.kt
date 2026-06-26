package com.clickretina.skillforge.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.clickretina.skillforge.R

/**
 * Plus Jakarta Sans is loaded as a downloadable Google Font (no .ttf bundled).
 * The certificates live in res/values/font_certs.xml. If the font provider is
 * unavailable on a device, Compose transparently falls back to the system font.
 */
private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val jakarta = GoogleFont("Plus Jakarta Sans")

val PlusJakartaSans = FontFamily(
    Font(googleFont = jakarta, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = jakarta, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = jakarta, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = jakarta, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = jakarta, fontProvider = provider, weight = FontWeight.ExtraBold)
)

private val base = Typography()

val SkillforgeTypography = Typography(
    displayLarge = base.displayLarge.withFont(),
    displayMedium = base.displayMedium.withFont(),
    displaySmall = base.displaySmall.withFont(),
    headlineLarge = base.headlineLarge.withFont(),
    headlineMedium = base.headlineMedium.withFont(),
    headlineSmall = base.headlineSmall.withFont(),
    titleLarge = base.titleLarge.copy(fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold),
    titleMedium = base.titleMedium.copy(fontFamily = PlusJakartaSans, fontWeight = FontWeight.Bold),
    titleSmall = base.titleSmall.withFont(),
    bodyLarge = base.bodyLarge.copy(fontFamily = PlusJakartaSans, fontSize = 15.sp),
    bodyMedium = base.bodyMedium.withFont(),
    bodySmall = base.bodySmall.withFont(),
    labelLarge = base.labelLarge.withFont(),
    labelMedium = base.labelMedium.withFont(),
    labelSmall = base.labelSmall.withFont()
)

private fun TextStyle.withFont(): TextStyle = copy(fontFamily = PlusJakartaSans)
