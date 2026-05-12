package com.paraminnovation.nammamela.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paraminnovation.nammamela.R
import com.paraminnovation.nammamela.ui.components.ShimmerBox
import com.paraminnovation.nammamela.ui.theme.BrandRed
import com.paraminnovation.nammamela.ui.theme.ChipLavender
import com.paraminnovation.nammamela.ui.theme.ChipLavenderInk
import com.paraminnovation.nammamela.ui.theme.ChipMint
import com.paraminnovation.nammamela.ui.theme.ChipMintInk
import com.paraminnovation.nammamela.ui.theme.ChipPeach
import com.paraminnovation.nammamela.ui.theme.ChipPeachInk
import com.paraminnovation.nammamela.ui.theme.ChipSky
import com.paraminnovation.nammamela.ui.theme.ChipSkyInk
import com.paraminnovation.nammamela.ui.theme.Elev
import com.paraminnovation.nammamela.ui.theme.NeutralBg
import com.paraminnovation.nammamela.ui.theme.ScrimDark
import com.paraminnovation.nammamela.ui.theme.ScrimTransparent
import com.paraminnovation.nammamela.ui.theme.Space
import com.paraminnovation.nammamela.ui.theme.SurfaceWhite
import com.paraminnovation.nammamela.ui.theme.TextPrimary
import com.paraminnovation.nammamela.ui.theme.TextSecondary
import com.paraminnovation.nammamela.ui.viewmodel.AppViewModel

@Composable
fun PlayScreen(
    viewModel: AppViewModel,
    onBookSeatsClick: () -> Unit,
    onManagerClick: () -> Unit
) {
    val show by viewModel.show.collectAsState()
    val isLoaded = show != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // === HERO ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.play_poster),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // top scrim
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xCC000000), ScrimTransparent)
                            )
                        )
                )
                // bottom scrim — heavier
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(ScrimTransparent, ScrimDark)
                            )
                        )
                )

                // Floating header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = Space.m, vertical = Space.s),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "NAMMA MELA",
                            color = SurfaceWhite,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            "Rural Karnataka",
                            color = SurfaceWhite.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    GlassButton(onClick = onManagerClick, label = "MANAGER")
                }

                // "LIVE TONIGHT" pill (bigger, with a pulsing dot)
                Surface(
                    color = BrandRed,
                    shape = CircleShape,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .padding(start = Space.l, top = 72.dp)
                        .align(Alignment.TopStart)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Space.m, vertical = Space.xs + 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SurfaceWhite)
                        )
                        Spacer(Modifier.size(Space.s))
                        Text(
                            "LIVE TONIGHT",
                            color = SurfaceWhite,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            // === DETAIL SHEET ===
            Surface(
                color = SurfaceWhite,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                shadowElevation = Elev.card,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp)
            ) {
                Column(modifier = Modifier.padding(Space.xl)) {

                    // === TITLE BLOCK ===
                    if (isLoaded) {
                        Text(
                            show?.name.orEmpty(),
                            style = MaterialTheme.typography.displayMedium,
                            color = TextPrimary
                        )
                    } else {
                        ShimmerBox(Modifier.fillMaxWidth(0.8f).height(28.dp))
                    }
                    Spacer(Modifier.size(Space.m))

                    // === FACT CHIPS ROW ===
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Space.s),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FactChip(show?.language ?: "Kannada", ChipMint, ChipMintInk)
                        FactChip(show?.duration ?: "2h 30m", ChipSky, ChipSkyInk)
                        FactChip("★ 4.5", ChipPeach, ChipPeachInk)
                    }

                    Spacer(Modifier.size(Space.xxl))

                    // === INFO 2-UP CARDS ===
                    Row(horizontalArrangement = Arrangement.spacedBy(Space.m)) {
                        InfoCard(
                            label = "SHOW TIME",
                            value = show?.time ?: "7:30 PM",
                            emoji = "⏰",
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            label = "DATE",
                            value = "Today",
                            emoji = "📅",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.size(Space.xxl))

                    // === ABOUT CARD ===
                    Eyebrow("ABOUT THE SHOW")
                    Spacer(Modifier.size(Space.s))
                    AnimatedVisibility(
                        visible = isLoaded,
                        enter = fadeIn() + slideInVertically { it / 4 }
                    ) {
                        Text(
                            show?.description.orEmpty(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    }
                    if (!isLoaded) {
                        repeat(3) {
                            ShimmerBox(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp)
                                    .height(14.dp)
                            )
                        }
                    }

                    Spacer(Modifier.size(Space.xxl))

                    // === WHY YOU'LL LOVE IT — TAG CHIPS ===
                    Eyebrow("WHY YOU'LL LOVE IT")
                    Spacer(Modifier.size(Space.m))
                    Column(verticalArrangement = Arrangement.spacedBy(Space.s)) {
                        FeatureRow("🎭", "Live performance with traditional costumes", ChipPeach, ChipPeachInk)
                        FeatureRow("🎶", "Original soundtrack performed live on stage", ChipMint, ChipMintInk)
                        FeatureRow("🪔", "Authentic rural Karnataka cultural experience", ChipLavender, ChipLavenderInk)
                    }

                    // Reserve space for the sticky CTA + nav bar
                    Spacer(Modifier.size(180.dp))
                }
            }
        }

        // === STICKY CTA ===
        Surface(
            color = SurfaceWhite,
            shadowElevation = Elev.stickyBar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = Space.m)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space.l, vertical = Space.m),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "STARTS AT",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Spacer(Modifier.size(2.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            "₹50",
                            style = MaterialTheme.typography.displaySmall,
                            color = TextPrimary
                        )
                        Spacer(Modifier.size(4.dp))
                        Text(
                            "onwards",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                HaloButton(onClick = onBookSeatsClick, label = "Book Seats →")
            }
        }
    }
}

@Composable
private fun GlassButton(onClick: () -> Unit, label: String) {
    Surface(
        color = Color(0x55000000),
        shape = CircleShape,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            label,
            color = SurfaceWhite,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = Space.m, vertical = Space.s)
        )
    }
}

@Composable
private fun FactChip(text: String, bg: Color, ink: Color) {
    Surface(color = bg, shape = CircleShape) {
        Text(
            text,
            color = ink,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = Space.m, vertical = Space.xs + 2.dp)
        )
    }
}

@Composable
private fun InfoCard(label: String, value: String, emoji: String, modifier: Modifier = Modifier) {
    Surface(
        color = SurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = Elev.card,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(Space.l)) {
            Text(emoji, style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.size(Space.s))
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Spacer(Modifier.size(2.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        }
    }
}

@Composable
private fun Eyebrow(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelMedium,
        color = TextSecondary
    )
}

@Composable
private fun FeatureRow(emoji: String, text: String, bg: Color, ink: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(color = bg, shape = CircleShape) {
            Text(
                emoji,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
        Spacer(Modifier.size(Space.m))
        Text(text, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
    }
}

@Composable
private fun HaloButton(onClick: () -> Unit, label: String) {
    Surface(
        color = BrandRed,
        shape = CircleShape,
        shadowElevation = Elev.ctaHalo,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            label,
            color = SurfaceWhite,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = Space.xxl, vertical = Space.m + 2.dp)
        )
    }
}
