package com.saihilg.quizepulse

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saihilg.quizepulse.ui.theme.QuizePulseTheme

class OldMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizePulseTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    var name by remember { mutableStateOf(TextFieldValue("Jiara Martins")) }
    var email by remember { mutableStateOf(TextFieldValue("hello@reallygreatsite.com")) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A237E), Color(0xFF00BCD4))))
            //code attribute for color gradient background
            //https://medium.com/@bimurat.mukhtar/how-to-implement-linear-gradient-with-any-angle-in-jetpack-compose-3ded798c81f5
            // Author:Mukhtar Bimurat
            ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Login",
                fontSize = 32.sp,
                color = Color.White
            )
            Text(
                text = "Sign in to continue.",
                fontSize = 16.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(40.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("NAME") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White


            ),
                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("EMAIL") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )
            Button(
                onClick = { /* Static login logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Login", color = Color.White, fontSize = 16.sp)
            }
            Text(
                text = "Need an account? Register here.",
                color = Color.White,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        val intent = Intent(context, RegisterActivity::class.java)
                        context.startActivity(intent)
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    QuizePulseTheme {
        LoginScreen()
    }
}
