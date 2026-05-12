package com.paraminnovation.nammamela.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import com.paraminnovation.nammamela.ui.components.ShimmerBox
import com.paraminnovation.nammamela.ui.components.ShimmerCircle
import com.paraminnovation.nammamela.ui.components.TintedAvatar
import com.paraminnovation.nammamela.ui.theme.BrandRed
import com.paraminnovation.nammamela.ui.theme.NeutralBg
import com.paraminnovation.nammamela.ui.theme.SurfaceWhite
import com.paraminnovation.nammamela.ui.theme.TextPrimary
import com.paraminnovation.nammamela.ui.theme.TextSecondary
import com.paraminnovation.nammamela.ui.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FanWallScreen(viewModel: AppViewModel) {
    val comments by viewModel.comments.collectAsState()
    val userName by viewModel.userName.collectAsState()

    var draft by remember { mutableStateOf("") }
    var showNameDialog by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }

    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = { Text("Your name") },
            text = {
                Column {
                    Text("This name will appear on your comments.")
                    Spacer(Modifier.size(8.dp))
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        singleLine = true,
                        label = { Text("Name") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nameInput.isNotBlank()) viewModel.setUserName(nameInput)
                    showNameDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) { Text("Cancel") }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 8.dp)) {
                Text(
                    "COMMUNITY",
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandRed
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    "Fan Wall",
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(Modifier.size(2.dp))
                Text(
                    "Share your applause for tonight's show",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            // Feed
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (comments.isEmpty()) {
                    items(3) { CommentSkeleton() }
                } else {
                    items(comments) { c ->
                        AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically { it / 6 }) {
                            ChatBubble(name = c.authorName, text = c.text, timestamp = c.timestamp)
                        }
                    }
                }
            }
        }

        // Sticky compose bar
        Surface(
            color = SurfaceWhite,
            shadowElevation = 16.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Column {
                if (!userName.isNullOrBlank()) {
                    Text(
                        "Posting as ${userName}",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = NeutralBg,
                        shape = CircleShape,
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = draft,
                            onValueChange = { draft = it },
                            placeholder = { Text("Leave your applause…", color = TextSecondary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                    Surface(
                        color = BrandRed,
                        shape = CircleShape,
                        shadowElevation = 12.dp,
                        modifier = Modifier
                            .size(56.dp)
                            .clickable {
                                if (userName.isNullOrBlank()) {
                                    showNameDialog = true
                                } else if (draft.isNotBlank()) {
                                    viewModel.postComment(draft)
                                    draft = ""
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                if (userName.isNullOrBlank()) "👤" else "↑",
                                color = SurfaceWhite,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(name: String, text: String, timestamp: Long) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        TintedAvatar(name = name, size = 44.dp)
        Spacer(Modifier.size(12.dp))
        Surface(
            color = SurfaceWhite,
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomEnd = 18.dp, bottomStart = 18.dp),
            shadowElevation = 3.dp,
            modifier = Modifier.weight(1f)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        name,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        formatTime(timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                Spacer(Modifier.size(4.dp))
                Text(text, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
            }
        }
    }
}

@Composable
private fun CommentSkeleton() {
    Row(modifier = Modifier.fillMaxWidth()) {
        ShimmerCircle(size = 40.dp)
        Spacer(Modifier.size(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            ShimmerBox(Modifier.fillMaxWidth(0.4f).height(14.dp))
            Spacer(Modifier.size(6.dp))
            ShimmerBox(Modifier.fillMaxWidth().height(12.dp))
            Spacer(Modifier.size(4.dp))
            ShimmerBox(Modifier.fillMaxWidth(0.6f).height(12.dp))
        }
    }
}

private fun formatTime(ts: Long): String {
    val fmt = SimpleDateFormat("d MMM, h:mm a", Locale.getDefault())
    return fmt.format(Date(ts))
}
