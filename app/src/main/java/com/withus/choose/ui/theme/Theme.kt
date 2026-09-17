package com.withus.choose.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SomaticDarkColorScheme = darkColorScheme(
    primary = GoldWarm,
    onPrimary = ObsidianBlack,
    primaryContainer = ObsidianElevated,
    onPrimaryContainer = GoldWarm,
    secondary = ParasympatheticEmerald,
    onSecondary = ObsidianBlack,
    secondaryContainer = ObsidianSurface,
    onSecondaryContainer = ParasympatheticEmerald,
    tertiary = SympatheticRed,
    onTertiary = ObsidianBlack,
    background = ObsidianBlack,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = ObsidianElevated,
    onSurfaceVariant = TextSecondary,
    outline = ObsidianBorder
)

@Composable
fun SomaticTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = ObsidianBlack.toArgb()
                it.navigationBarColor = ObsidianBlack.toArgb()
                val controller = WindowCompat.getInsetsController(it, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = SomaticDarkColorScheme,
        typography = Typography,
        content = content
    )
}
