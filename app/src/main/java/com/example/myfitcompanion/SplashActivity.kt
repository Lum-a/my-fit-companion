package com.example.myfitcompanion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.SignUpActivity
import com.example.myfitcompanion.ui.theme.MyFitCompanionTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFitCompanionTheme {
                SplashScreen(
                    onSignUpClick = {
                        startActivity(Intent(this, SignUpActivity::class.java))
                    },
                    onLoginClick = {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun SplashScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to MyApp",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onSignUpClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    MyFitCompanionTheme {
        SplashScreen(
            onSignUpClick = {},
            onLoginClick = {}
        )
    }
}