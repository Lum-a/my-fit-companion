package com.example.myfitcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.myfitcompanion.ui.theme.MyFitCompanionTheme

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFitCompanionTheme {
                SignUpScreen()
            }
        }
    }
}


@Composable
fun SignUpScreen() {
    Surface {
        Text("Sign Up Screen")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    MyFitCompanionTheme {
        SignUpScreen()
    }
}



