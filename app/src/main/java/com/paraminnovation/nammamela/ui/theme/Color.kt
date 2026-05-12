package com.paraminnovation.nammamela.ui.theme

import androidx.compose.ui.graphics.Color

// === Brand (BookMyShow red) ===
val BrandRed = Color(0xFFE23744)
val BrandRedDark = Color(0xFFB71C2C)
val BrandRedSoft = Color(0xFFFFE5E8)   // tinted background, like Blinkit's pastel chips
val BrandGold = Color(0xFFFFB300)
val BrandGoldSoft = Color(0xFFFFF4D6)

// === Blinkit-style category pastels (used as solid chip backgrounds) ===
val ChipMint = Color(0xFFDDF5E5)
val ChipMintInk = Color(0xFF0C7E3F)
val ChipSky = Color(0xFFDDEEFF)
val ChipSkyInk = Color(0xFF0F5BA8)
val ChipPeach = Color(0xFFFFE7D6)
val ChipPeachInk = Color(0xFFB8531B)
val ChipLavender = Color(0xFFEEE3FF)
val ChipLavenderInk = Color(0xFF6536B4)

// === Neutrals (Blinkit-style clean greys) ===
val NeutralBg = Color(0xFFFAFAFA)
val SurfaceWhite = Color(0xFFFFFFFF)
val Divider = Color(0xFFEDEDED)
val TextPrimary = Color(0xFF1A1A1A)
val TextSecondary = Color(0xFF666666)
val TextTertiary = Color(0xFF9A9A9A)

// === Seat states ===
val SeatAvailable = Color(0xFFF0F0F0)
val SeatAvailableBorder = Color(0xFFD0D0D0)
val SeatSelected = Color(0xFF2EB872)        // green like BMS "selected"
val SeatSelectedBorder = Color(0xFF1F8A53)
val SeatReserved = Color(0xFFBDBDBD)

// === Misc ===
val ScrimDark = Color(0xCC000000)            // 80% black for hero gradient bottom
val ScrimTransparent = Color(0x00000000)
val SuccessGreen = Color(0xFF2EB872)
val ShimmerBase = Color(0xFFEFEFEF)
val ShimmerHighlight = Color(0xFFF7F7F7)

// Deterministic palette for Fan Wall avatars (tinted, soft)
val AvatarPalette = listOf(
    Color(0xFFFFD6D9), // pink
    Color(0xFFFFE2C2), // peach
    Color(0xFFFFF4B8), // soft yellow
    Color(0xFFD7F5DC), // mint
    Color(0xFFD0E8FF), // sky
    Color(0xFFE5D9FF), // lavender
    Color(0xFFFFE0F0), // rose
    Color(0xFFD9F2EE)  // teal
)

val AvatarTextPalette = listOf(
    Color(0xFFB02A35),
    Color(0xFFA0590B),
    Color(0xFF7A6300),
    Color(0xFF1F8A53),
    Color(0xFF1A5BA0),
    Color(0xFF5232A0),
    Color(0xFFA02060),
    Color(0xFF146657)
)
