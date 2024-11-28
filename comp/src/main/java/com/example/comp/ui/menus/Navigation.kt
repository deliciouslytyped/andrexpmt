package com.example.comp.ui.menus

import androidx.compose.animation.EnterTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.comp.ui.game.DraggablePlayArea

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        // The animation is currently janky
        // https://stackoverflow.com/questions/77729213/why-page-transitions-are-so-slow-with-navcontroller
        enterTransition = {
            // you can change whatever you want transition
            EnterTransition.None
        }
    ) {
        composable(route = Screen.Home.route){
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Game.route){
            DraggablePlayArea()
        }
    }
}

open class Screen(val route: String) {
    object Home : Screen("home")
    object Game : Screen("game")
}