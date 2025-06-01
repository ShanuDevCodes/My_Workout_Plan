package com.example.myworkoutplan.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.ui.components.FirebaseAuth.FirebaseEvent
import com.example.myworkoutplan.ui.components.FirebaseAuth.FirebaseState
import com.example.myworkoutplan.ui.components.FirebaseAuth.FirebaseViewModel
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutEvent
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignupScreen(){
    val firebaseViewModel: FirebaseViewModel = viewModel()
    val firebaseState by firebaseViewModel.state.collectAsState()
    val onEvent: (FirebaseEvent) -> Unit = firebaseViewModel::onEvent
    var isVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(firebaseState.isError, firebaseState.error) {
        if (firebaseState.isError && firebaseState.error.isNotBlank()) {
            Toast.makeText(context, firebaseState.error, Toast.LENGTH_LONG).show()
            firebaseViewModel.onEvent(FirebaseEvent.ResetError)
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                TextField(
                    value = firebaseState.name,
                    onValueChange = {name->
                        onEvent(FirebaseEvent.SetUserName(name))
                    },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                TextField(
                    value = firebaseState.email,
                    onValueChange = {email->
                        onEvent(FirebaseEvent.SetUserEmail(email))
                    },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                TextField(
                    value = firebaseState.password,
                    onValueChange = { password ->
                        onEvent(FirebaseEvent.SetUserPassword(password))
                    },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        onEvent(FirebaseEvent.RegisterUser)
                        isVisible = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Signup")
                }
                if (isVisible) {
                    if (firebaseState.isLoggedIn) {
                        Text("Signup Successful")
                    }
                }
            }
        }
    }
}