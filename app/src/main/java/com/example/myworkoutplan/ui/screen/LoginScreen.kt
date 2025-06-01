package com.example.myworkoutplan.ui.screen

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.MainActivity
import com.example.myworkoutplan.WorkoutActivity
import com.example.myworkoutplan.ui.components.FirebaseAuth.FirebaseEvent
import com.example.myworkoutplan.ui.components.FirebaseAuth.FirebaseViewModel
import com.example.myworkoutplan.ui.data.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
    val firebaseViewModel: FirebaseViewModel = viewModel()
    val firebaseState by firebaseViewModel.state.collectAsState()
    val onEvent: (FirebaseEvent) -> Unit = firebaseViewModel::onEvent
    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    var agreeToTerms by remember { mutableStateOf(false) }

    var showTermsDialog by remember { mutableStateOf(false) }
    if (showTermsDialog) {
        TermsAndPrivacyDialog(onDismiss = { showTermsDialog = false })
    }

    LaunchedEffect(Unit) {
        firebaseViewModel.checkLoggedInState()
    }
    LaunchedEffect(firebaseState.isError, firebaseState.error) {
        if (firebaseState.isError && firebaseState.error.isNotBlank()) {
            Toast.makeText(context, firebaseState.error, Toast.LENGTH_LONG).show()
            firebaseViewModel.onEvent(FirebaseEvent.ResetError)
        }
    }
    LaunchedEffect(firebaseState.isLoggedIn) {
        if (firebaseState.isLoggedIn) {
            dataStore.setFirstLaunchDone()
            context.startActivity(Intent(context, MainActivity::class.java))
            (context as? Activity)?.finish()
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (firebaseState.isLoading) {
            // Full-screen loading overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Get Started Now",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Please log in to your account to continue.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Email Field
            TextField(
                value = firebaseState.email,
                onValueChange = { onEvent(FirebaseEvent.SetUserEmail(it)) },
                label = { Text("Email address") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            TextField(
                value = firebaseState.password,
                onValueChange = { onEvent(FirebaseEvent.SetUserPassword(it)) },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            )

            // Forgot Password Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onEvent(FirebaseEvent.ResetPassword) }) {
                    Text("Forgot Password?")
                }
            }

            // Terms and Privacy Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Checkbox(
                    checked = agreeToTerms,
                    onCheckedChange = { agreeToTerms = it }
                )
                Text(
                    text = "I agree to the ",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Terms & Privacy",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.clickable {
                        showTermsDialog = true
                    },
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Login Button
            Button(
                onClick = {
                    if (agreeToTerms) {
                        onEvent(FirebaseEvent.LoginUser)
                    } else {
                        Toast.makeText(context, "Please agree to the terms.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Log in")
            }

            // Signup Link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ")
                Text(
                    text = "Sign Up",
                    modifier = Modifier.clickable {},
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text("  Or  ", style = MaterialTheme.typography.bodySmall)
                Divider(modifier = Modifier.weight(1f))
            }

            // Social Login Buttons (placeholders)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch{
                            dataStore.setFirstLaunchDone()
                        }
                        context.startActivity(Intent(context, MainActivity::class.java))
                        (context as? Activity)?.finish()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Continue as a Guest")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /* TODO: Apple Login */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Login with Google")
                }
            }
        }
    }
}

@Composable
fun TermsAndPrivacyDialog(
    onDismiss: () -> Unit,
    title: String = "Terms & Privacy",
    termsText: String = """
        Terms & Privacy Policy

        By using this app, you agree to the following:

        • Information We Collect: We may collect personal information such as your name, email address, device information, and usage data to provide and improve our services.
        • How We Use Your Information: Your data is used for core app functionality, account management, analytics, and to personalize your experience.
        • Data Sharing: We do not sell your personal information. Your data may be shared with trusted third-party services for authentication, analytics, and data storage, in accordance with their privacy policies.
        • Security: We use industry-standard security measures to protect your data, including encryption and secure transmission protocols.
        • User Rights: You have the right to access, update, or delete your personal information. Contact us at [your support email] for any requests regarding your data.
        • Policy Changes: We may update this policy from time to time. Significant changes will be communicated within the app or via email.

        For more details, please review our full Privacy Policy available on our website or app store listing.
    """.trimIndent()
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(termsText) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        dismissButton = {}
    )
}