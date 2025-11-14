package com.example.learnnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyappNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.screenA) {
        composable(Routes.screenA) {
            ScreenA(navController)
        }
        composable("${Routes.screenB}/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            ScreenB(navController, name?: "ningun nombre recibido")
        }
        composable("${Routes.ScreenC}/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            ScreenC(name?: "ningun nombre recibido")
        }
    }
}