package com.example.myworkoutplan.features.auth.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.features.auth.viewmodel.EmailVerificationViewModel

@Composable
fun EmailVerificationWaitingScreen(
    onResend: () -> Unit,
    onCancel: () -> Unit,
    viewModel: EmailVerificationViewModel = viewModel()
) {
    val resendEnabled by viewModel.resendEnabled.collectAsState()
    val secondsLeft by viewModel.secondsLeft.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Waiting for email verification...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please check your inbox and click the verification link.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.resendEmail()
                    onResend()
                },
                enabled = resendEnabled,
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    if (resendEnabled) "Resend Verification Email"
                    else "Resend Verification (${secondsLeft}s)"
                )
            }
        }

        TextButton(
            onClick = onCancel,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("Cancel", color = MaterialTheme.colorScheme.error)
        }
    }
}