package com.dcf2.orbita.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dcf2.orbita.MainViewModel
import com.dcf2.orbita.ui.DiarioPage
import com.dcf2.orbita.ui.ExplorarPage
import com.dcf2.orbita.ui.HomePage
import com.dcf2.orbita.ui.ObservatorioPage
import com.dcf2.orbita.ui.PerfilPage

@Composable
fun MainNavHost(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = BottomNavItem.Home.route) {

        // Rota da Home
        composable(BottomNavItem.Home.route) {
            HomePage(viewModel = viewModel)
        }

        // Rota Explorar
        composable(BottomNavItem.Explorar.route) {
            ExplorarPage(viewModel = viewModel)
        }

        // Rota Observatório (CORRIGIDO AQUI)
        // Antes estava ObservatorioPage(), agora passamos o viewModel
        composable(BottomNavItem.Observatorio.route) {
            ObservatorioPage(viewModel = viewModel)
        }

        // Rota Diário
        composable(BottomNavItem.Diario.route) {
            DiarioPage(viewModel = viewModel)
        }

        // Rota Perfil
        composable("perfil_route") {
            PerfilPage(viewModel = viewModel)
        }
    }
}