package com.dcf2.orbita.ui.page

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dcf2.orbita.viewmodel.IssViewModel

@Composable
fun ISSTrackerPage(viewModel: IssViewModel = viewModel()) {
    val issData by viewModel.issResponse.collectAsState() // Mudou de issPosition para issResponse
    val distancia by viewModel.distancia.collectAsState()
    val context = LocalContext.current

    // Gerenciador de Permissão de GPS
    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            obterLocalizacao(context, viewModel)
        }
    }

    LaunchedEffect(Unit) {
        locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Rastreador ISS", style = MaterialTheme.typography.headlineMedium, color = Color.White)

        Spacer(modifier = Modifier.height(32.dp))

        if (issData != null) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Latitude: ${issData!!.posicao.latitude}", color = Color.White)
                    Text("Longitude: ${issData!!.posicao.longitude}", color = Color.White)

                    Spacer(modifier = Modifier.height(16.dp))

                    if (distancia != null) {
                        Text(
                            "Distância de você: ${"%.2f".format(distancia)} km",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFF2994A)
                        )
                    } else {
                        Text("Aguardando seu GPS...", color = Color.Gray)
                        Button(onClick = { obterLocalizacao(context, viewModel) }) {
                            Text("Ativar GPS")
                        }
                    }
                }
            }
        } else {
            CircularProgressIndicator(color = Color(0xFFF2994A))
            Text("Conectando ao satélite...", color = Color.Gray, modifier = Modifier.padding(top=8.dp))
        }
    }
}

@SuppressLint("MissingPermission")
fun obterLocalizacao(context: Context, viewModel: IssViewModel) {
    try {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        location?.let {
            viewModel.atualizarLocalizacaoUsuario(it.latitude, it.longitude)
        }
    } catch (e: Exception) {
        // Tratar erro ou pedir permissão novamente
    }
}