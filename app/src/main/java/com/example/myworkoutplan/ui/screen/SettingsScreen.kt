package com.example.myworkoutplan.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
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
import com.example.myworkoutplan.ui.components.DynamicColorOption
import com.example.myworkoutplan.ui.components.ThemeOptions


@Composable
fun SettingsScreen(
    selectedThemeOption: ThemeOptions,
    onThemeChange: (ThemeOptions) -> Unit,
    selectedDynamicColorOption: DynamicColorOption,
    onDynamicColorChange: (DynamicColorOption) -> Unit
) {
    Box(modifier = Modifier
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
                        onClick = { onThemeChange(option) }
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
                        onClick = { onDynamicColorChange(option) }
                    )
                    Text(text = option.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    var selectedTheme by rememberSaveable { mutableStateOf(ThemeOptions.SYSTEM_DEFAULT) }
    var dynamicColorOption by rememberSaveable { mutableStateOf(DynamicColorOption.ENABLED) }
    SettingsScreen(
        selectedThemeOption = selectedTheme,
        onThemeChange = { selectedTheme = it },
        selectedDynamicColorOption = dynamicColorOption,
        onDynamicColorChange = { dynamicColorOption = it }
    )
}
