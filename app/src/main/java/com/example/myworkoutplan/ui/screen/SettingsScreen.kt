package com.example.myworkoutplan.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.ui.components.DynamicColorOption
import com.example.myworkoutplan.ui.components.SettingsViewModel
import com.example.myworkoutplan.ui.components.ThemeOptions
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import com.example.myworkoutplan.ui.components.DataStoreManager
import com.example.myworkoutplan.ui.components.SettingsViewModelFactory


@Composable
fun SettingsScreen() {
    // Use remember to store only the factory and ViewModel
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModelFactory = remember { SettingsViewModelFactory(DataStoreManager(context)) }
    val settingsViewModel: SettingsViewModel = viewModel(factory = viewModelFactory)

    val selectedThemeOption = settingsViewModel.selectedTheme
    val selectedDynamicColorOption = settingsViewModel.dynamicColorOption

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Text(
                "Choose Theme",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            ThemeOptions.values().forEach { option ->
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Use Dynamic Colors",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            DynamicColorOption.values().forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selectedDynamicColorOption == option,
                        onClick = { settingsViewModel.updateDynamicColorOption(option) }
                    )
                    Text(text = option.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }
    }
}
