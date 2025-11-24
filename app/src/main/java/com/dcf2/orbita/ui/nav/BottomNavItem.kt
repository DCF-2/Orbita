package com.dcf2.orbita.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.serialization.Serializable

// Rotas (usando Serializable como no WeatherApp)
sealed interface Route {
    @Serializable
    data object Home : Route
    @Serializable
    data object Map : Route
    @Serializable
    data object Diario : Route // Equivalente ao "List" do WeatherApp
}

// Itens do Menu Inferior
sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Route
) {
    data object HomeButton : BottomNavItem("Feed", Icons.Default.Home, Route.Home)
    data object MapButton : BottomNavItem("Mapa", Icons.Default.Map, Route.Map)
    data object DiarioButton : BottomNavItem("Diário", Icons.Default.Book, Route.Diario)
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.HomeButton,
        BottomNavItem.MapButton,
        BottomNavItem.DiarioButton
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination

        items.forEach { item ->
            // Lógica simplificada para verificar a rota atual (por string do nome da classe)
            // Em projetos reais complexos, a verificação pode ser mais robusta
            val isSelected = currentRoute?.route?.contains(item.route::class.simpleName.toString()) == true

            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        // Evita empilhar telas se clicar várias vezes
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}