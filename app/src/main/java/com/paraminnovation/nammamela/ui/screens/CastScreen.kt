package com.paraminnovation.nammamela.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paraminnovation.nammamela.R
import com.paraminnovation.nammamela.ui.theme.BrandRed
import com.paraminnovation.nammamela.ui.theme.ChipLavender
import com.paraminnovation.nammamela.ui.theme.ChipLavenderInk
import com.paraminnovation.nammamela.ui.theme.ChipMint
import com.paraminnovation.nammamela.ui.theme.ChipMintInk
import com.paraminnovation.nammamela.ui.theme.ChipPeach
import com.paraminnovation.nammamela.ui.theme.ChipPeachInk
import com.paraminnovation.nammamela.ui.theme.Elev
import com.paraminnovation.nammamela.ui.theme.NeutralBg
import com.paraminnovation.nammamela.ui.theme.ScrimDark
import com.paraminnovation.nammamela.ui.theme.ScrimTransparent
import com.paraminnovation.nammamela.ui.theme.Space
import com.paraminnovation.nammamela.ui.theme.SurfaceWhite
import com.paraminnovation.nammamela.ui.theme.TextPrimary
import com.paraminnovation.nammamela.ui.theme.TextSecondary
import com.paraminnovation.nammamela.ui.viewmodel.AppViewModel

private data class CastMember(
    val name: String,
    val role: String,
    val tag: String,
    val chipBg: Color,
    val chipInk: Color,
    val rating: String,
    val shows: String,
    val imageRes: Int
)

@Composable
fun CastScreen(viewModel: AppViewModel) {
    val cast by viewModel.cast.collectAsState()
    val members = listOf(
        CastMember(
            name = cast?.leadName ?: "Rajesh Kumar",
            role = cast?.leadRole ?: "as Lord Rama",
            tag = "LEAD",
            chipBg = ChipPeach,
            chipInk = ChipPeachInk,
            rating = "4.8",
            shows = "15+ shows",
            imageRes = R.drawable.actor_lead
        ),
        CastMember(
            name = cast?.comedianName ?: "Ganesh Rao",
            role = cast?.comedianRole ?: "as Hanuman's Friend",
            tag = "COMEDIAN",
            chipBg = ChipMint,
            chipInk = ChipMintInk,
            rating = "4.9",
            shows = "Fan favourite",
            imageRes = R.drawable.actor_comedian
        ),
        CastMember(
            name = cast?.singerName ?: "Lakshmi Devi",
            role = cast?.singerRole ?: "Classical Vocalist",
            tag = "SINGER",
            chipBg = ChipLavender,
            chipInk = ChipLavenderInk,
            rating = "4.7",
            shows = "Live music",
            imageRes = R.drawable.actor_singer
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBg)
    ) {
        // Header
        Column(modifier = Modifier.padding(start = Space.xl, end = Space.xl, top = Space.xxl, bottom = Space.m)) {
            Text(
                "TONIGHT'S CAST",
                style = MaterialTheme.typography.labelMedium,
                color = BrandRed
            )
            Spacer(Modifier.size(Space.xs))
            Text(
                "Meet the stars",
                style = MaterialTheme.typography.displayMedium,
                color = TextPrimary
            )
            Spacer(Modifier.size(2.dp))
            Text(
                "Tap a card to learn more about each performer",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }

        Spacer(Modifier.size(Space.m))

        // Carousel
        LazyRow(
            contentPadding = PaddingValues(horizontal = Space.xl),
            horizontalArrangement = Arrangement.spacedBy(Space.l)
        ) {
            items(members) { m -> CastCard(m) }
        }

        Spacer(Modifier.size(Space.xxl))

        // Swipe hint
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = SurfaceWhite,
                shape = CircleShape,
                shadowElevation = 2.dp
            ) {
                Text(
                    "← SWIPE TO SEE ALL ${members.size} →",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = Space.m, vertical = Space.s)
                )
            }
        }
    }
}

@Composable
private fun CastCard(m: CastMember) {
    Surface(
        color = SurfaceWhite,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = Elev.card,
        modifier = Modifier.width(260.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Image(
                    painter = painterResource(id = m.imageRes),
                    contentDescription = m.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                // bottom scrim
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .height(140.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(ScrimTransparent, ScrimDark)
                            )
                        )
                )
                // Role chip top-left (Blinkit pastel)
                Surface(
                    color = m.chipBg,
                    shape = CircleShape,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .padding(Space.m)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        m.tag,
                        color = m.chipInk,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = Space.m, vertical = Space.xs + 1.dp)
                    )
                }
                // Rating top-right
                Surface(
                    color = Color(0xCC000000),
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(Space.m)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        "★ ${m.rating}",
                        color = SurfaceWhite,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = Space.s + 2.dp, vertical = Space.xs + 1.dp)
                    )
                }
                // Name + role over scrim
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(Space.l)
                ) {
                    Text(
                        m.name,
                        color = SurfaceWhite,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        m.role,
                        color = SurfaceWhite.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            // Stats strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space.l, vertical = Space.m),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = m.chipBg, shape = CircleShape) {
                    Text(
                        m.shows.uppercase(),
                        color = m.chipInk,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = Space.m, vertical = Space.xs + 1.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    "★ ${m.rating}",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
