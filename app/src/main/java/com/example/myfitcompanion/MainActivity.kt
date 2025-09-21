package com.example.myfitcompanion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myfitcompanion.navigation.MyFitNavigation
import com.example.myfitcompanion.ui.theme.MyFitCompanionTheme
import com.example.myfitcompanion.ui.theme.darkBackground
import com.example.myfitcompanion.utils.AuthViewModel
import com.example.myfitcompanion.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFitCompanionTheme { MyFitApp() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyFitApp() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<AuthViewModel>()
    val isUserLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val isAdmin by viewModel.isAdmin.collectAsStateWithLifecycle()

    Log.d("MainActivity", "isUserLoggedIn: $isUserLoggedIn, isAdmin: $isAdmin")

    Surface {
        Scaffold(containerColor = darkBackground,
            bottomBar = {
                BottomNavigationBar(navController, !isAdmin && isUserLoggedIn)
            }, content = { padding ->
                MyFitNavigation(navController, padding, isAdmin)
            }
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, shouldShow: Boolean) {
    if(shouldShow) {
        NavigationBar(
            containerColor = Color(0xFF2C2B2B)
        ) {

            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentRoute = navBackStackEntry?.destination?.route

            Constants.bottomNavItems.forEach { navItem ->

                NavigationBarItem(

                    selected = currentRoute == navItem.route,

                    onClick = {
                        navController.navigate(navItem.route)
                    },

                    icon = {
                        Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                    },

                    label = {
                        Text(text = navItem.label)
                    },
                    alwaysShowLabel = false,

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White, // Icon color when selected
                        unselectedIconColor = Color.White, // Icon color when not selected
                        selectedTextColor = Color.White, // Label color when selected
                        indicatorColor = Color(0xFF195334) // Highlight color for selected item
                    )
                )
            }
        }
    }
}