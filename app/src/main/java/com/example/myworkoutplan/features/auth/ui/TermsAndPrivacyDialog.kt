package com.example.myworkoutplan.features.auth.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

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