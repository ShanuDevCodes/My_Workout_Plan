package com.example.myworkoutplan.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.ui.components.items
import com.example.myworkoutplan.ui.navigation.NavigationViewModel
import com.example.myworkoutplan.ui.navigation.PlansNavigator
import com.example.myworkoutplan.ui.screen.HomeScreen
import com.example.myworkoutplan.ui.screen.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeUI(viewModel: NavigationViewModel = viewModel()) {
    val selectedItemIndex by viewModel.selectedItemIndex.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent
    ) {
        Row {
            // NavigationRail instead of Bottom Navigation
            NavigationRail(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                items.forEachIndexed { index, item ->
                    NavigationRailItem(
                        selected = selectedItemIndex == index,
                        onClick = { viewModel.onItemSelected(index) },
                        icon = {
                            BadgedBox(badge = { }) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        },
                        label = {
                            Text(
                                text = item.title,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    )
                }
            }

            // Main content area
            Scaffold(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentWindowInsets = WindowInsets.systemBars,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                when (selectedItemIndex) {
                                    0 -> "Home "
                                    1 -> "Plans"
                                    2 -> "Settings"
                                    else -> ""
                                },
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (selectedItemIndex) {
                        0 -> HomeScreen()
                        1 -> PlansNavigator()
                        2 -> SettingsScreen()
                    }
                }
            }
        }
    }
}