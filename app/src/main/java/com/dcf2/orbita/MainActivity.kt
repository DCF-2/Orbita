package com.dcf2.orbita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dcf2.orbita.ui.NovaObservacaoDialog
import com.dcf2.orbita.ui.nav.BottomNavBar
import com.dcf2.orbita.ui.nav.MainNavHost
import com.dcf2.orbita.ui.theme.OrbitaTheme // Certifique-se de ter um tema ou use MaterialTheme
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: MainViewModel by viewModels()

        setContent {
            OrbitaTheme {
                val navController = rememberNavController()
                var showDialog by remember { mutableStateOf(false) }

                if (showDialog) {
                    NovaObservacaoDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { titulo, desc ->
                            viewModel.addObservacao(titulo, desc)
                            showDialog = false
                        }
                    )
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar(navController) },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showDialog = true },
                            containerColor =  Color(0xFFF2994A) // Laranja Orbita
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Novo Post")
                        }
                    }
                ) { innerPadding ->
                    MainNavHost(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}