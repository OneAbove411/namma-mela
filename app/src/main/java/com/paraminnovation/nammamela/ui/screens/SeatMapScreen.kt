package com.paraminnovation.nammamela.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraminnovation.nammamela.payment.RazorpayHandler
import com.paraminnovation.nammamela.ui.theme.BrandRed
import com.paraminnovation.nammamela.ui.theme.BrandRedSoft
import com.paraminnovation.nammamela.ui.theme.NeutralBg
import com.paraminnovation.nammamela.ui.theme.SeatAvailable
import com.paraminnovation.nammamela.ui.theme.SeatAvailableBorder
import com.paraminnovation.nammamela.ui.theme.SeatReserved
import com.paraminnovation.nammamela.ui.theme.SeatSelected
import com.paraminnovation.nammamela.ui.theme.SeatSelectedBorder
import com.paraminnovation.nammamela.ui.theme.SurfaceWhite
import com.paraminnovation.nammamela.ui.theme.TextPrimary
import com.paraminnovation.nammamela.ui.theme.TextSecondary
import com.paraminnovation.nammamela.ui.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatMapScreen(viewModel: AppViewModel) {
    val seats by viewModel.seats.collectAsState()
    val selected by viewModel.selectedSeats.collectAsState()
    val razorpayKey by viewModel.razorpayKey.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val context = LocalContext.current

    var summaryOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val total = viewModel.selectedTotal()
    val grouped = seats.groupBy { it.section }
        .toSortedMap(compareBy { sectionOrder(it) })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 88.dp)
        ) {
            // Header
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 12.dp)) {
                Text(
                    "BOOKING",
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandRed
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    "Pick your seats",
                    style = MaterialTheme.typography.displayMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.size(2.dp))
                Text(
                    "Tap to select, tap again to remove",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            // Legend (above sections, like BMS)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LegendItem("Available", SeatAvailable, SeatAvailableBorder)
                LegendItem("Selected", SeatSelected, SeatSelectedBorder)
                LegendItem("Sold", SeatReserved, SeatReserved)
            }

            Spacer(Modifier.size(20.dp))

            // Sections — Premium (back of hall) at top, Economy (front, near stage) at bottom
            grouped.forEach { (section, sectionSeats) ->
                val price = sectionSeats.firstOrNull()?.price ?: 0
                val sectionSelectedCount = sectionSeats.count { selected.contains(it.id) }
                SectionHeader(
                    section = section,
                    price = price,
                    selectedCount = sectionSelectedCount
                )
                Spacer(Modifier.size(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Reverse: highest-numbered row at top (back), lowest at bottom (near stage).
                    // e.g. Premium renders P3, P2, P1 — and P1 sits just above Standard.
                    val rows = sectionSeats.groupBy { it.rowLabel }
                        .toSortedMap(compareByDescending { it })
                    rows.forEach { (rowLabel, rowSeats) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                rowLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary,
                                modifier = Modifier.width(24.dp)
                            )
                            rowSeats.sortedBy { it.seatNumber }.forEach { s ->
                                val state = when {
                                    s.status == "reserved" -> SeatState.RESERVED
                                    selected.contains(s.id) -> SeatState.SELECTED
                                    else -> SeatState.AVAILABLE
                                }
                                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                    Seat(label = s.seatNumber.toString(), state = state) {
                                        viewModel.toggleSeat(s.id)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.size(24.dp))
            }

            // Stage at the bottom — "All eyes this way" points down to it
            Spacer(Modifier.size(8.dp))
            CurvedStage()
            Spacer(Modifier.size(8.dp))
            Text(
                "All eyes this way please",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(Modifier.size(24.dp))
        }

        // Sticky CTA
        AnimatedVisibility(
            visible = selected.isNotEmpty(),
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                color = SurfaceWhite,
                shadowElevation = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "${selected.size} SEAT${if (selected.size == 1) "" else "S"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Spacer(Modifier.size(2.dp))
                        Text(
                            "₹$total",
                            style = MaterialTheme.typography.displaySmall,
                            color = TextPrimary
                        )
                    }
                    Surface(
                        color = BrandRed,
                        shape = CircleShape,
                        shadowElevation = 12.dp,
                        modifier = Modifier.clickable { summaryOpen = true }
                    ) {
                        Text(
                            "Continue →",
                            color = SurfaceWhite,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 26.dp, vertical = 14.dp)
                        )
                    }
                }
            }
        }
    }

    if (summaryOpen) {
        ModalBottomSheet(
            onDismissRequest = { summaryOpen = false },
            sheetState = sheetState,
            containerColor = SurfaceWhite
        ) {
            BookingSummarySheet(
                seatIds = selected.toList(),
                total = total,
                onPay = {
                    val key = razorpayKey
                    if (key.isNullOrBlank()) {
                        Toast.makeText(
                            context,
                            "Set a Razorpay test key in Manager Mode first.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@BookingSummarySheet
                    }
                    summaryOpen = false
                    try {
                        viewModel.beginPayment()
                        RazorpayHandler.startPayment(
                            activity = context as Activity,
                            testKey = key,
                            amountInPaise = total * 100,
                            description = "Namma Mela: ${selected.size} seat(s)",
                            customerName = userName,
                            receiptId = "nm_${System.currentTimeMillis()}"
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, "Payment error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}

private fun sectionOrder(section: String) = when (section) {
    "PREMIUM" -> 0
    "STANDARD" -> 1
    "ECONOMY" -> 2
    else -> 9
}

@Composable
private fun CurvedStage() {
    // Stage sits below the seats. The arch curves DOWNWARD (away from audience)
    // so it visually represents the screen/stage edge bowing toward the viewer.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 20.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(0f, size.height * 0.4f)
                quadraticBezierTo(
                    size.width / 2f, size.height * 1.4f,
                    size.width, size.height * 0.4f
                )
            }
            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFE23744), Color(0xFFFFB300)),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f)
                ),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f)
            )
        }
        // Stage label sits at the bottom of the curve, on the screen side.
        Text(
            "STAGE",
            modifier = Modifier.align(Alignment.BottomCenter),
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary
        )
    }
}

@Composable
private fun LegendItem(label: String, fill: Color, stroke: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(fill)
                .border(1.dp, stroke, RoundedCornerShape(4.dp))
        )
        Spacer(Modifier.size(6.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
    }
}

@Composable
private fun SectionHeader(section: String, price: Int, selectedCount: Int = 0) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            section,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )
        Spacer(Modifier.size(10.dp))
        Surface(color = BrandRedSoft, shape = CircleShape) {
            Text(
                "₹$price",
                style = MaterialTheme.typography.labelLarge,
                color = BrandRed,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
        if (selectedCount > 0) {
            Spacer(Modifier.weight(1f))
            Surface(color = SeatSelected, shape = CircleShape, shadowElevation = 2.dp) {
                Text(
                    "$selectedCount SELECTED",
                    style = MaterialTheme.typography.labelSmall,
                    color = SurfaceWhite,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

private enum class SeatState { AVAILABLE, SELECTED, RESERVED }

@Composable
private fun Seat(label: String, state: SeatState, onClick: () -> Unit) {
    val targetSize = when (state) {
        SeatState.SELECTED -> 32.dp
        else -> 28.dp
    }
    val animatedSize by animateDpAsState(
        targetValue = targetSize,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "seat-size"
    )

    val (bg, border) = when (state) {
        SeatState.AVAILABLE -> SeatAvailable to SeatAvailableBorder
        SeatState.SELECTED -> SeatSelected to SeatSelectedBorder
        SeatState.RESERVED -> SeatReserved to SeatReserved
    }

    Box(
        modifier = Modifier
            .size(animatedSize)
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .clickable(enabled = state != SeatState.RESERVED, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (state == SeatState.SELECTED) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Selected",
                tint = SurfaceWhite,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = if (state == SeatState.RESERVED) SurfaceWhite else TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun BookingSummarySheet(
    seatIds: List<String>,
    total: Int,
    onPay: () -> Unit
) {
    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 28.dp)) {
        Text(
            "Review your booking",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.size(16.dp))

        SummaryRow("Seats", seatIds.joinToString(", "))
        Spacer(Modifier.size(10.dp))
        SummaryRow("Number of seats", "${seatIds.size}")
        Spacer(Modifier.size(10.dp))
        SummaryRow("Convenience fee", "₹0", subtle = true)
        Spacer(Modifier.size(16.dp))
        Surface(color = NeutralBg, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Total", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Text("₹$total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.size(16.dp))
        Surface(
            color = BrandRed,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onPay)
        ) {
            Box(
                modifier = Modifier.padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Pay ₹$total",
                    color = SurfaceWhite,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, subtle: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, modifier = Modifier.weight(1f))
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (subtle) FontWeight.Normal else FontWeight.SemiBold,
            color = if (subtle) TextSecondary else TextPrimary
        )
    }
}
