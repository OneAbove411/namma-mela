package com.paraminnovation.nammamela.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.paraminnovation.nammamela.data.entity.CastEntity
import com.paraminnovation.nammamela.data.entity.ShowEntity
import com.paraminnovation.nammamela.ui.theme.NeutralBg
import com.paraminnovation.nammamela.ui.theme.SurfaceWhite
import com.paraminnovation.nammamela.ui.theme.TextSecondary
import com.paraminnovation.nammamela.ui.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun ManagerScreen(viewModel: AppViewModel, onExit: () -> Unit) {
    var unlocked by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    if (!unlocked) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Manager Mode", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Default PIN on first launch: 1234 — change it below after entering.", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.padding(top = 16.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = { if (it.length <= 8) pin = it.filter(Char::isDigit) },
                singleLine = true,
                label = { Text("PIN") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
            if (pinError != null) {
                Text(pinError!!, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.padding(top = 8.dp))
            Row {
                Button(onClick = {
                    scope.launch {
                        if (viewModel.verifyPin(pin)) {
                            unlocked = true
                            pinError = null
                        } else {
                            pinError = "Incorrect PIN."
                        }
                    }
                }) { Text("Unlock") }
                Spacer(Modifier.padding(start = 8.dp))
                OutlinedButton(onClick = onExit) { Text("Cancel") }
            }
        }
        return
    }

    ManagerForm(viewModel = viewModel, onExit = onExit)
}

@Composable
private fun ManagerForm(viewModel: AppViewModel, onExit: () -> Unit) {
    val show by viewModel.show.collectAsState()
    val cast by viewModel.cast.collectAsState()
    val razorpayKey by viewModel.razorpayKey.collectAsState()

    // Local form state, seeded from DB
    var playName by remember(show) { mutableStateOf(show?.name ?: "") }
    var playDuration by remember(show) { mutableStateOf(show?.duration ?: "") }
    var playTime by remember(show) { mutableStateOf(show?.time ?: "") }
    var playLanguage by remember(show) { mutableStateOf(show?.language ?: "") }
    var playDesc by remember(show) { mutableStateOf(show?.description ?: "") }

    var leadName by remember(cast) { mutableStateOf(cast?.leadName ?: "") }
    var comedianName by remember(cast) { mutableStateOf(cast?.comedianName ?: "") }
    var singerName by remember(cast) { mutableStateOf(cast?.singerName ?: "") }

    var razorpayInput by remember(razorpayKey) { mutableStateOf(razorpayKey ?: "") }

    var newPin by remember { mutableStateOf("") }
    var showResetConfirm by remember { mutableStateOf(false) }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Reset all seats?") },
            text = { Text("All bookings will be cleared. This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetAllSeats()
                    showResetConfirm = false
                }) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) { Text("Cancel") }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 120.dp)
        ) {
            Text(
                "Manager Mode",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Edit show info, cast, payment, and security",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(Modifier.size(16.dp))

            SectionCard("Tonight's Play") {
                FieldRow("Play name", playName) { playName = it }
                FieldRow("Duration", playDuration) { playDuration = it }
                FieldRow("Show time", playTime) { playTime = it }
                FieldRow("Language", playLanguage) { playLanguage = it }
                FieldRow("Description", playDesc, singleLine = false) { playDesc = it }
            }

            SectionCard("Cast") {
                FieldRow("Lead actor", leadName) { leadName = it }
                FieldRow("Comedian", comedianName) { comedianName = it }
                FieldRow("Singer", singerName) { singerName = it }
            }

            SectionCard("Razorpay test key") {
                Text(
                    "Paste your test-mode key from razorpay.com (starts with rzp_test_). " +
                    "Required for the Pay & Book button to work.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(Modifier.size(8.dp))
                OutlinedTextField(
                    value = razorpayInput,
                    onValueChange = { razorpayInput = it },
                    singleLine = true,
                    label = { Text("rzp_test_...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SectionCard("Change manager PIN") {
                OutlinedTextField(
                    value = newPin,
                    onValueChange = { if (it.length <= 8) newPin = it.filter(Char::isDigit) },
                    singleLine = true,
                    label = { Text("New PIN (4–8 digits)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SectionCard("Seat management") {
                Button(
                    onClick = { showResetConfirm = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C2C)),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Reset all seats") }
            }
        }

        // Sticky bottom Save/Cancel bar — sits clear above the bottom nav
        Surface(
            color = SurfaceWhite,
            shadowElevation = 12.dp,
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onExit,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancel") }
                Button(
                    onClick = {
                        viewModel.updateShow(
                            ShowEntity(
                                name = playName.ifBlank { "Untitled" },
                                description = playDesc,
                                duration = playDuration,
                                time = playTime,
                                language = playLanguage
                            )
                        )
                        viewModel.updateCast(
                            CastEntity(
                                leadName = leadName,
                                comedianName = comedianName,
                                singerName = singerName
                            )
                        )
                        if (razorpayInput.isNotBlank()) viewModel.setRazorpayKey(razorpayInput)
                        if (newPin.length in 4..8) viewModel.setPin(newPin)
                        onExit()
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Save") }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Spacer(Modifier.size(12.dp))
    Surface(
        color = SurfaceWhite,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.size(10.dp))
            content()
        }
    }
}

@Composable
private fun FieldRow(
    label: String,
    value: String,
    singleLine: Boolean = true,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = singleLine,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
