package com.example.viagemapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viagemapp.database.AppDatabase
import com.example.viagemapp.entity.Trip
import com.example.viagemapp.screens.AddTripScreen
import com.example.viagemapp.screens.LoginScreen
import com.example.viagemapp.screens.MenuScreen
import com.example.viagemapp.screens.RegisterScreen
import com.example.viagemapp.screens.SavedRoteirosScreen
import com.example.viagemapp.ui.theme.ViagemAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViagemAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("menu/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            MenuScreen(navController = navController, username = username)
        }
        composable("add_trip/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            AddTripScreen(navController = navController, username = username)
        }
        composable("edit_trip/{tripId}/{username}") { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId")?.toIntOrNull()
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val context = LocalContext.current
            val tripDao = AppDatabase.getDatabase(context).tripDao()
            var trip by remember { mutableStateOf<Trip?>(null) }


            LaunchedEffect(tripId) {
                trip = tripId?.let { tripDao.getById(it) }
            }

            trip?.let {
                AddTripScreen(navController = navController, username = username, existingTrip = it)
            }
        }

        composable("roteiros/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            SavedRoteirosScreen(username = username) {
                navController.popBackStack()
            }
        }

    }
}
