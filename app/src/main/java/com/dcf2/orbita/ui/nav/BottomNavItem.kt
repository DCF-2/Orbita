package com.dcf2.orbita.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star // Usado para o Observatório
import androidx.compose.material.icons.filled.Edit // Usado para o Diário
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String) {

    // 1. Tela Principal (Feed)
    object Home : BottomNavItem("Home", Icons.Default.Home, "home")

    // 2. Tela Explorar (Notícias/Curiosidades) - NOVA
    object Explorar : BottomNavItem("Explorar", Icons.Default.Search, "explorar")

    // 3. Tela Observatório (Antigo Mapa) - NOVA
    object Observatorio : BottomNavItem("Observatório", Icons.Default.Star, "observatorio")

    // 4. Tela Diário (Suas anotações)
    object Diario : BottomNavItem("Diário", Icons.Default.Edit, "diario")

    // Rota para o Perfil (Não aparece no menu de baixo, mas precisa da rota definida)
    object Perfil : BottomNavItem("Perfil", Icons.Default.Home, "perfil_route")
}