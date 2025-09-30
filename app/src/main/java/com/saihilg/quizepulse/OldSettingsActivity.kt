package com.saihilg.quizepulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.saihilg.quizepulse.ui.theme.QuizePulseTheme

// This is the Activity that hosts the Settings screen
class OldSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Makes the app use the full screen and go under status/navigation bars
        enableEdgeToEdge()

        // This sets the Compose content for the Activity instead of using XML
        setContent {
            // Apply your app’s theme (colors, typography)
            QuizePulseTheme {
                // Call the composable function that builds the UI
                SettingsScreen()
            }
        }
    }
}

// The main Composable function for the Settings screen
@Composable
fun SettingsScreen() {
    // State variables to hold user input and settings toggles
    var username by remember { mutableStateOf(TextFieldValue("Player")) } // Username text field
    var soundEnabled by remember { mutableStateOf(true) }                  // Sound toggle
    var musicEnabled by remember { mutableStateOf(true) }                  // Music toggle
    var difficulty by remember { mutableStateOf("Medium") }                // Difficulty selection

    // List of difficulty levels
    val difficulties = listOf("Easy", "Medium", "Hard")

    // Outer container that fills the screen and has a vertical gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A237E), Color(0xFF00BCD4)) // Gradient from dark blue to cyan
                )
            )
            .padding(16.dp) // Padding around the screen edges
    ) {
        // Column to arrange elements vertically
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(40.dp)) // Top spacing

            // Screen title
            Text(
                text = "Settings",
                fontSize = 32.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp)) // Space below title

            // Username input field
            OutlinedTextField(
                value = username, // Current text
                onValueChange = { username = it }, // Update state when user types
                label = { Text("Username") }, // Field label
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp)) // Space between fields

            // Row for Sound toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sound", color = Color.White, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f)) // Push toggle to the right
                Switch(
                    checked = soundEnabled, // Current state
                    onCheckedChange = { soundEnabled = it }, // Update state when toggled
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Space between toggles

            // Row for Music toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Music", color = Color.White, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f)) // Push toggle to the right
                Switch(
                    checked = musicEnabled, // Current state
                    onCheckedChange = { musicEnabled = it }, // Update state when toggled
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                )
            }

            Spacer(modifier = Modifier.height(20.dp)) // Space before difficulty

            // Difficulty section
            Text(text = "Difficulty", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Row of buttons for difficulty selection
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            )
            {
                difficulties.forEach { level ->
                    Button(
                        onClick = { difficulty = level }, // Update selected difficulty
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (difficulty == level) Color.White else Color(0xFF3949AB),
                            contentColor = if (difficulty == level) Color(0xFF1A237E) else Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(level)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Space before Save button

            // Save button
            Button(
                onClick = { /* Save settings logic */ }, // Add logic to save settings here
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                     shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Settings",
                    fontSize = 16.sp,
                    color = Color.White)
            }

            Spacer(modifier = Modifier.weight(1f)) // Push content toward top
        }
    }
}

// Preview function to see the screen in Android Studio without running the app
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
