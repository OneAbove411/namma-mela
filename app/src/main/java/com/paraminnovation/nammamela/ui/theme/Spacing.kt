package com.paraminnovation.nammamela.ui.theme

import androidx.compose.ui.unit.dp

// Blinkit-style spacing scale — generous, with predictable jumps.
object Space {
    val xs = 4.dp     // micro gaps inside chips
    val s = 8.dp      // chip-to-chip, icon-to-text
    val m = 12.dp     // between paragraph elements
    val l = 16.dp     // card internal padding (small side)
    val xl = 20.dp    // card internal padding (default)
    val xxl = 24.dp   // section-to-section
    val xxxl = 32.dp  // major section breaks
}

// Elevation tokens. Material 3 uses tonal elevation by default; here we use
// shadow elevation because Blinkit's surfaces feel "lifted" off the canvas.
object Elev {
    val card = 4.dp        // resting card
    val cardHover = 8.dp   // pressed/hover (we set this with animateDpAsState)
    val stickyBar = 16.dp  // sticky CTAs
    val ctaHalo = 12.dp    // primary buttons sit on a glow shadow
}
