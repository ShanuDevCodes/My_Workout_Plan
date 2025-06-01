package com.example.myworkoutplan.ui.screen

import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.R
import com.example.myworkoutplan.SettingsActivity
import com.example.myworkoutplan.ui.components.FirebaseAuth.FirebaseEvent
import com.example.myworkoutplan.ui.components.FirebaseAuth.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseViewModel: FirebaseViewModel = viewModel()
    val firebaseState by firebaseViewModel.state.collectAsState()
    val onEvent: (FirebaseEvent) -> Unit = firebaseViewModel::onEvent
    val userName = auth.currentUser?.displayName?:"Guest"
    val userEmail = auth.currentUser?.email?:"Anonymous"
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    LaunchedEffect(firebaseState.isError, firebaseState.error) {
        if (firebaseState.isError && firebaseState.error.isNotBlank()) {
            Toast.makeText(context, firebaseState.error, Toast.LENGTH_LONG).show()
            firebaseViewModel.onEvent(FirebaseEvent.ResetError)
        }
    }
    if (isPortrait) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image with Edit Icon
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.size(110.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.rest),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                    )
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = userName.toString(),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(Modifier.height(8.dp))

                // Email badge
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                ) {

                    Text(
                        text = userEmail.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.padding(6.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(modifier = Modifier.padding(6.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            WeeklyGoalProgress(current = 4, goal = 5)
                        }

                        Spacer(Modifier.height(24.dp))
                        // Card with actions
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                StatItem(label = "Workouts Completed", value = "24")
                                Spacer(Modifier.height(12.dp))
                                StatItem(label = "Current Streak", value = "7 Days")
                                Spacer(Modifier.height(12.dp))
                                StatItem(label = "Highest Streak", value = "128 Days")
                                Spacer(Modifier.height(12.dp))
                                StatItem(label = "Time Spent", value = "14h 30m")
                                Spacer(Modifier.height(12.dp))
                                StatItem(label = "Weekly Goal", value = "4/5")
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            ProfileActionItem(
                                icon = Icons.Default.Edit,
                                label = "Edit Profile",
                                onClick = {}
                            )
                            ProfileActionItem(
                                icon = Icons.Default.DateRange,
                                label = "History",
                                onClick = {}
                            )
                            ProfileActionItem(
                                icon = Icons.Default.Info,
                                label = "Info",
                                onClick = {}
                            )
                            ProfileActionItem(
                                icon = Icons.Default.Settings,
                                label = "Settings",
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            SettingsActivity::class.java
                                        )
                                    )
                                }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            if (auth.currentUser != null) {
                                ProfileActionItem(
                                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                                    label = "Logout",
                                    onClick = {
                                        onEvent(FirebaseEvent.LogoutUser)
                                    },
                                    iconTint = Color(0xFFD32F2F),
                                    textColor = Color(0xFFD32F2F),
                                )
                            }else{
                                ProfileActionItem(
                                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                                    label = "Login",
                                    onClick = {

                                    },
                                    iconTint = Color(0xFFD32F2F),
                                    textColor = Color(0xFFD32F2F),
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }else{
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center // ✅ This centers the Row in the screen
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), // Full width Row
                verticalAlignment = Alignment.CenterVertically // ✅ Align children vertically in Row
            ) {
                // Left: Profile Section
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // ✅ Center content vertically in Column
                ) {
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier.size(110.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.rest),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                        )
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(32.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = userName.toString(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = userEmail.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Right: Action Section
                LazyColumn(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // ✅ Center LazyColumn content
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            WeeklyGoalProgress(current = 4, goal = 5)
                        }

                        Spacer(Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                StatItem(label = "Workouts Completed", value = "24")
                                Spacer(Modifier.height(12.dp))
                                StatItem(label = "Current Streak", value = "7 Days")
                                Spacer(Modifier.height(12.dp))
                                StatItem(label = "Highest Streak", value = "128 Days")
                                Spacer(Modifier.height(12.dp))
                                StatItem(label = "Time Spent", value = "14h 30m")
                                Spacer(Modifier.height(12.dp))
                                StatItem(label = "Weekly Goal", value = "4/5")
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            ProfileActionItem(
                                icon = Icons.Default.Edit,
                                label = "Edit Profile",
                                onClick = {
                                    if(auth.currentUser == null){
                                        Toast.makeText(context, "Login to edit your profile", Toast.LENGTH_LONG).show()
                                    }else{
                                        Toast.makeText(context, "Edit Profile", Toast.LENGTH_LONG).show()
                                    }
                                }
                            )
                            ProfileActionItem(
                                icon = Icons.Default.DateRange,
                                label = "History",
                                onClick = {}
                            )
                            ProfileActionItem(
                                icon = Icons.Default.Info,
                                label = "Info",
                                onClick = {}
                            )
                            ProfileActionItem(
                                icon = Icons.Default.Settings,
                                label = "Settings",
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            SettingsActivity::class.java
                                        )
                                    )
                                }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            if (auth.currentUser != null) {
                                ProfileActionItem(
                                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                                    label = "Logout",
                                    onClick = {
                                        onEvent(FirebaseEvent.LogoutUser)
                                    },
                                    iconTint = Color(0xFFD32F2F),
                                    textColor = Color(0xFFD32F2F),
                                )
                            }else{
                                ProfileActionItem(
                                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                                    label = "Login",
                                    onClick = {

                                    },
                                    iconTint = Color(0xFFD32F2F),
                                    textColor = Color(0xFFD32F2F),
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ProfileActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    ListItem(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        leadingContent = {
            Icon(icon, contentDescription = null, tint = iconTint)
        },
        headlineContent = {
            Text(label, color = textColor)
        },
        trailingContent = {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = iconTint
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = containerColor
        )
    )
}

@Composable
fun StatItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall, // smaller than headlineSmall
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), // smaller than headlineMedium
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun WeeklyGoalProgress(current: Int, goal: Int) {
    val progress = (current.toFloat() / goal).coerceIn(0f, 1f)
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Weekly Progress",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}