package com.dcf2.orbita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dcf2.orbita.ui.NovaObservacaoDialog
import com.dcf2.orbita.ui.nav.BottomNavItem
import com.dcf2.orbita.ui.nav.MainNavHost
import com.dcf2.orbita.ui.theme.OrbitaTheme
import com.dcf2.orbita.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        setContent {
            OrbitaTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}


//adicionar no mapa para que as pessoas digam quais os melhores locais para observação
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()

    // Estado para controlar se o diálogo de nova observação está visível
    var showDialog by remember { mutableStateOf(false) }


    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explorar,
        BottomNavItem.Observatorio,
        BottomNavItem.Mapa,
        BottomNavItem.Diario
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orbita") },
                actions = {
                    IconButton(onClick = { navController.navigate("perfil_route") }) {
                        Icon(Icons.Default.AccountCircle, "Perfil", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            // Só mostra o botão se estiver na Home ou no Diário
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute == BottomNavItem.Home.route || currentRoute == BottomNavItem.Diario.route) {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = Color(0xFFF2994A),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nova Observação")
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF121212)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = {
                            Text(
                                text = item.title,
                                maxLines = 1, // Força 1 linha
                                style = androidx.compose.ui.text.TextStyle(fontSize = 10.sp) // Fonte menor
                            )},
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Cyan,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color(0xFF1E1E1E)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
            MainNavHost(navController = navController, viewModel = viewModel)
        }


        if (showDialog) {
            NovaObservacaoDialog(
                onDismiss = { showDialog = false },
                onConfirm = { titulo, descricao ->
                    viewModel.addObservacao(titulo, descricao)
                    showDialog = false
                }
            )
        }
    }
}