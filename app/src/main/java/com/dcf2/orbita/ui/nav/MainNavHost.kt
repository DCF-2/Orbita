package com.dcf2.orbita.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dcf2.orbita.MainViewModel
import com.dcf2.orbita.ui.DiarioPage
import com.dcf2.orbita.ui.HomePage
import com.dcf2.orbita.ui.MapPage

@Composable
fun MainNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = Route.Home, modifier = modifier) {
        composable<Route.Home> {
            HomePage(viewModel = viewModel)
        }
        composable<Route.Map> {
            MapPage(viewModel = viewModel)
        }
        composable<Route.Diario> {
            DiarioPage(viewModel = viewModel)
        }
    }
}