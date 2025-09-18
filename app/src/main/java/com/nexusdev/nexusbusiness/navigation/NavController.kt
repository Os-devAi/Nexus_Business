package com.nexusdev.nexusbusiness.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexusdev.nexusbusiness.ui.screens.detalles.DetallesScreen
import com.nexusdev.nexusbusiness.ui.screens.home.HomeScreen

@Composable
fun NavController(
    modifier: Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(modifier = modifier, nav = navController)
        }
        composable("detalles/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            if (id != null) {
                DetallesScreen(id = id, modifier = modifier)
            }
        }
    }
}