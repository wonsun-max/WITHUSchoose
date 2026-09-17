package com.withus.choose.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Luxury Midnight & Obsidian palette (inspired by Oura & Calm)
val ObsidianBase = Color(0xFF08080C)
val ObsidianTop = Color(0xFF0F0F17)
val ObsidianSurface = Color(0xFF14141E)
val ObsidianCard = Color(0xFF181824)
val ObsidianElevated = Color(0xFF202030)
val ObsidianBorder = Color(0xFF2A2A3C)
val ObsidianBorderSubtle = Color(0x1FFFFFFF)

// Warm Amber & Gold Accents
val GoldWarm = Color(0xFFE5A967)
val GoldLight = Color(0xFFF2C28D)
val GoldSoft = Color(0xFFD39B58)
val GoldSubtle = Color(0x26E5A967)
val GoldGlow = Color(0x33E5A967)

// Bio-states (Human & Friendly)
val ParasympatheticEmerald = Color(0xFF38D39F)
val ParasympatheticSoft = Color(0xFF26B887)
val ParasympatheticBg = Color(0x1A38D39F)

val SympatheticRed = Color(0xFFFF6565)
val SympatheticSoft = Color(0xFFE04E4E)
val SympatheticBg = Color(0x1AFF6565)

// Typography & Text
val TextPrimary = Color(0xFFF6F6F8)
val TextSecondary = Color(0xFFA6A6B4)
val TextMuted = Color(0xFF727282)

// Pulse Waveform & Glows
val WaveformCyan = Color(0xFF52E5D0)
val WaveformGlow = Color(0x4052E5D0)

// Gradients
val AmbientBackgroundBrush = Brush.verticalGradient(
    colors = listOf(
        ObsidianTop,
        ObsidianBase,
        Color(0xFF060609)
    )
)

val GoldButtonBrush = Brush.horizontalGradient(
    colors = listOf(
        GoldWarm,
        GoldLight
    )
)

val CardGlowBrush = Brush.linearGradient(
    colors = listOf(
        Color(0x33E5A967),
        Color(0x05FFFFFF),
        Color(0x1A38D39F)
    )
)
