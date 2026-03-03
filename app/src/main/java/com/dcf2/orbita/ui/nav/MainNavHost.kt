package com.dcf2.orbita.ui.nav

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dcf2.orbita.viewmodel.MainViewModel
import com.dcf2.orbita.ui.page.DiarioPage
import com.dcf2.orbita.ui.page.ExplorarPage
import com.dcf2.orbita.ui.page.HomePage
import com.dcf2.orbita.ui.page.MapPage
import com.dcf2.orbita.ui.page.ObservatorioPage
import com.dcf2.orbita.ui.page.PerfilPage
import com.dcf2.orbita.LoginActivity
import com.dcf2.orbita.ui.page.ISSTrackerPage
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainNavHost(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = BottomNavItem.Home.route) {

        // Rota da Home
        composable(BottomNavItem.Home.route) {
            HomePage(viewModel = viewModel)
        }

        // Rota Explorar
        composable(BottomNavItem.Explorar.route) {
            ExplorarPage(viewModel = viewModel, navController = navController)
        }

        // Rota Observatório
        composable(BottomNavItem.Observatorio.route) {
            ObservatorioPage(viewModel = viewModel)
        }

        // Rota Diário
        composable(BottomNavItem.Diario.route) {
            DiarioPage(viewModel = viewModel)
        }

        // Rota Mapa
        composable(BottomNavItem.Mapa.route) {
            MapPage(viewModel = viewModel, navController = navController)
        }

        // Rota ISS (CORRIGIDO: Recebe o navController para poder voltar)
        composable("iss_detalhes") {
            ISSTrackerPage(viewModel = viewModel, navController = navController)
        }

        // Rota Perfil
        composable("perfil_route") {
            val context = LocalContext.current

            PerfilPage(
                viewModel = viewModel,
                navController = navController,
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                }
            )
        }
    }
}