package com.example.myworkoutplan.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.ui.data.DataStoreManager
import com.example.myworkoutplan.ui.theme.DynamicColorOption
import com.example.myworkoutplan.ui.settings.SettingsViewModel
import com.example.myworkoutplan.ui.settings.SettingsViewModelFactory
import com.example.myworkoutplan.ui.theme.ThemeOptions


@Composable
fun SettingsScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModelFactory = remember { SettingsViewModelFactory(DataStoreManager(context)) }
    val settingsViewModel: SettingsViewModel = viewModel(factory = viewModelFactory)

    val selectedThemeOption = settingsViewModel.selectedTheme
    val selectedDynamicColorOption = settingsViewModel.dynamicColorOption
    Column {
        Text(
            text = "Settings",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn {
                item {
                    Text(
                        "Choose Theme",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                items(ThemeOptions.entries.size) { index ->
                    val option = ThemeOptions.entries[index]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedThemeOption == option,
                            onClick = { settingsViewModel.setThemeOption(option) }
                        )
                        Text(
                            text = option.name.replace("_", " ").lowercase()
                                .replaceFirstChar { it.uppercase() }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Text(
                        "Use Dynamic Colors",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                items(DynamicColorOption.entries.size) { index ->
                    val option = DynamicColorOption.entries[index]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedDynamicColorOption == option,
                            onClick = { settingsViewModel.updateDynamicColorOption(option) }
                        )
                        Text(
                            text = option.name.lowercase().replaceFirstChar { it.uppercase() }
                        )
                    }
                }
            }
        }
    }
}
