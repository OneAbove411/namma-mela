package com.paraminnovation.nammamela.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paraminnovation.nammamela.ui.theme.AvatarPalette
import com.paraminnovation.nammamela.ui.theme.AvatarTextPalette

/**
 * Deterministic tinted avatar: same name → same color forever.
 */
@Composable
fun TintedAvatar(
    name: String,
    size: Dp = 40.dp
) {
    // Use Kotlin/Java hashCode (32-bit FNV-like distribution) for better spread.
    // `kotlin.math.absoluteValue` avoids negative indices when hashCode is negative.
    val idx = kotlin.math.abs(name.trim().lowercase().hashCode()) % AvatarPalette.size
    val bg = AvatarPalette[idx]
    val fg = AvatarTextPalette[idx]
    val letter = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = fg,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
