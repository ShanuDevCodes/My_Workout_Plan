package com.example.myworkoutplan.features.auth.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.MainActivity
import com.example.myworkoutplan.R
import com.example.myworkoutplan.core.DataStoreManager
import com.example.myworkoutplan.data.remote.firebaseauth.FirebaseEvent
import com.example.myworkoutplan.data.remote.firebaseauth.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    onLoginClicked: () -> Unit
) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseViewModel: FirebaseViewModel = viewModel()
    val firebaseState by firebaseViewModel.state.collectAsState()
    val onEvent: (FirebaseEvent) -> Unit = firebaseViewModel::onEvent
    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    var agreeToTerms by remember { mutableStateOf(false) }
    var checkingVerification by remember { mutableStateOf(false) }
    var isFirstLaunch by remember { mutableStateOf<Boolean?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

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
            checkingVerification = true
            while(checkingVerification){
                auth.currentUser?.reload()
                if (auth.currentUser == null){
                    checkingVerification = false
                    return@LaunchedEffect
                }
                if (auth.currentUser?.isEmailVerified == true) {
                    checkingVerification = false
                    break
                }
                delay(20)
            }
            isFirstLaunch = dataStore.isFirstLaunch.first()
            if (isFirstLaunch == true) {
                dataStore.setFirstLaunchDone()
                context.startActivity(Intent(context, MainActivity::class.java))
            }
            (context as? Activity)?.finish()
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Login to your account",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Spacer(modifier = Modifier.padding(18.dp))

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
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            val description =
                                if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = description)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                                Toast.makeText(
                                    context,
                                    "Please agree to the terms.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Login")
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
                            modifier = Modifier.clickable {
                                onLoginClicked()
                            },
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
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text("  Or  ", style = MaterialTheme.typography.bodySmall)
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    // Social Login Buttons (placeholders)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    isFirstLaunch = dataStore.isFirstLaunch.first()
                                    if (isFirstLaunch == true) {
                                        dataStore.setFirstLaunchDone()
                                        context.startActivity(
                                            Intent(
                                                context,
                                                MainActivity::class.java
                                            )
                                        )
                                    }
                                }
                                (context as? Activity)?.finish()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Person Logo",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Continue as a Guest")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { /* TODO: Apple Login */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.google),
                                contentDescription = "Google Logo",
                                modifier = Modifier
                                    .size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Login with Google")
                        }
                    }
                }
            }
        }
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
        if (checkingVerification) {
            EmailVerificationWaitingScreen(
                onResend = {
                    // Send verification email
                    onEvent(FirebaseEvent.SendEmailVerification)
                    Toast.makeText(
                        context,
                        "Verification email resent! Please check your inbox.",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onCancel = {
                    firebaseViewModel.onEvent(FirebaseEvent.DeleteUser)
                    firebaseViewModel.onEvent(FirebaseEvent.LogoutUser)
                    Toast.makeText(
                        context,
                        "LogIn canceled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}