package com.paraminnovation.nammamela.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paraminnovation.nammamela.data.entity.TicketEntity
import com.paraminnovation.nammamela.ui.components.DashedDivider
import com.paraminnovation.nammamela.ui.theme.BrandRed
import com.paraminnovation.nammamela.ui.theme.BrandRedDark
import com.paraminnovation.nammamela.ui.theme.BrandRedSoft
import com.paraminnovation.nammamela.ui.theme.Divider
import com.paraminnovation.nammamela.ui.theme.NeutralBg
import com.paraminnovation.nammamela.ui.theme.SuccessGreen
import com.paraminnovation.nammamela.ui.theme.SurfaceWhite
import com.paraminnovation.nammamela.ui.theme.TextPrimary
import com.paraminnovation.nammamela.ui.theme.TextSecondary
import com.paraminnovation.nammamela.ui.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketScreen(
    viewModel: AppViewModel,
    ticketId: Long,
    onDone: () -> Unit
) {
    val ticket by viewModel.ticketById(ticketId).collectAsState(initial = null)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BrandRed, BrandRedDark)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0x33FFFFFF),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(onClick = onDone)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Done",
                            tint = SurfaceWhite
                        )
                    }
                }
            }

            // Success badge
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = true, enter = scaleIn() + fadeIn()) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(SurfaceWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Spacer(Modifier.size(12.dp))
                Text(
                    "Booking confirmed",
                    color = SurfaceWhite,
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    "Your seats are reserved",
                    color = SurfaceWhite.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.size(24.dp))

            // Ticket card
            ticket?.let {
                TicketCard(it)
            } ?: LoadingTicketPlaceholder()

            Spacer(Modifier.size(24.dp))

            // CTA
            Surface(
                color = SurfaceWhite,
                shape = CircleShape,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickable(onClick = onDone)
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Done",
                        color = BrandRed,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.size(24.dp))
        }
    }
}

@Composable
private fun TicketCard(t: TicketEntity) {
    Surface(
        color = SurfaceWhite,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 12.dp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Column {
            // Header strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandRedSoft)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Namma Mela", style = MaterialTheme.typography.labelSmall, color = BrandRedDark)
                    Text(
                        t.playName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(color = BrandRed, shape = CircleShape) {
                    Text(
                        t.language,
                        color = SurfaceWhite,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Punch indents on the dashed line
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Notch(left = true)
                    Box(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                        DashedDivider()
                    }
                    Notch(left = false)
                }
            }

            // Body
            Column(modifier = Modifier.padding(16.dp)) {
                TicketRow("Seats", t.seatList)
                Spacer(Modifier.size(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Date", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text(formatDate(t.createdAt), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Time", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text(t.showTime, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Seats", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text("${t.seatCount}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.size(16.dp))
                Surface(color = NeutralBg, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Total paid", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f), color = TextSecondary)
                        Text("₹${t.totalAmount}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.size(16.dp))

                // QR placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeutralBg),
                    contentAlignment = Alignment.Center
                ) {
                    QrPlaceholder()
                }

                Spacer(Modifier.size(10.dp))
                Text(
                    "Show this ticket at the venue entrance",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (!t.paymentId.isNullOrBlank()) {
                    Spacer(Modifier.size(6.dp))
                    Text(
                        "Payment ID: ${t.paymentId}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingTicketPlaceholder() {
    Surface(
        color = SurfaceWhite,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(360.dp)
    ) {}
}

@Composable
private fun Notch(left: Boolean) {
    // a half-circle cutout effect by using a colored circle on the gradient bg
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(BrandRedDark) // matches gradient bottom of the page bg
    )
}

@Composable
private fun TicketRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun QrPlaceholder() {
    // simple 6x6 grid mock — not a real QR
    Column {
        val pattern = listOf(
            "1,1,1,0,1,1",
            "1,0,1,1,0,1",
            "1,1,0,1,1,0",
            "0,1,1,0,1,1",
            "1,0,1,1,0,1",
            "1,1,0,1,1,1"
        )
        pattern.forEach { row ->
            Row {
                row.split(",").forEach { cell ->
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(1.dp)
                            .background(if (cell == "1") TextPrimary else Color.Transparent)
                    )
                }
            }
        }
    }
}

private fun formatDate(ts: Long): String =
    SimpleDateFormat("d MMM, yyyy", Locale.getDefault()).format(Date(ts))
