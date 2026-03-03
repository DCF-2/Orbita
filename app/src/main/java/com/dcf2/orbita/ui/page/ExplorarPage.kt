package com.dcf2.orbita.ui.page

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dcf2.orbita.api.nasa.NeoModel
import com.dcf2.orbita.model.ApodModel
import com.dcf2.orbita.model.DonkiModel
import com.dcf2.orbita.viewmodel.MainViewModel
import java.util.Locale

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun ExplorarPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {
    var selectedDonki by remember { mutableStateOf<DonkiModel?>(null) }
    var selectedNeo by remember { mutableStateOf<NeoModel?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchSpaceWeather()
        viewModel.fetchNearEarthObjects()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            ISSTrackerCard(viewModel) { navController.navigate("iss_detalhes") }
        }

        item {
            SectionTitle("🌌 Eventos Astronômicos")
            if (viewModel.spaceWeatherEvents.isEmpty() && viewModel.nearEarthObjects.isEmpty()) {
                Text("Buscando eventos no espaço...", color = Color.Gray, fontSize = 12.sp)
            } else {
                // Uma única lista em sequência contendo os alertas solares e asteroides
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Primeiro renderiza os eventos solares
                    items(viewModel.spaceWeatherEvents) { evento ->
                        SpaceWeatherCard(evento) { selectedDonki = evento }
                    }
                    // Em sequência, renderiza os asteroides
                    items(viewModel.nearEarthObjects) { neo ->
                        NeoCard(neo) { selectedNeo = neo }
                    }
                }
            }
        }

        item { SectionTitle("🌌 Diário Cósmico") }
        
        if (viewModel.apodList.isEmpty() && viewModel.isLoadingApod) {
            item { LoadingBox() }
        } else {
            items(viewModel.apodList) { apod ->
                ApodCard(apod)
            }
            item { VerMaisButton(viewModel) }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }

    if (selectedDonki != null) {
        DonkiDialog(selectedDonki!!) { selectedDonki = null }
    }

    if (selectedNeo != null) {
        NeoDialog(selectedNeo!!) { selectedNeo = null }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
fun SpaceWeatherCard(evento: DonkiModel, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.width(240.dp).height(120.dp).clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WbSunny, null, tint = Color(0xFFF2C94C), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                // Evita NPE caso a NASA não retorne a data da erupção
                Text(evento.startTime?.substringBefore("T") ?: "Data Desconhecida", color = Color(0xFFF2C94C), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Erupção Solar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            // Evita NPE caso a nota seja nula
            Text(evento.note ?: "Sem observações detalhadas.", color = Color.Gray, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun NeoCard(neo: NeoModel, onClick: () -> Unit) {
    val hazardous = neo.is_potentially_hazardous_asteroid
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.width(240.dp).height(120.dp).clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, null, tint = if (hazardous) Color.Red else Color.Green, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (hazardous) "PERIGOSO" else "SEGURO", color = if (hazardous) Color.Red else Color.Green, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(neo.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
            Text("Monitorização ativa", color = Color.Gray, fontSize = 11.sp)
        }
    }
}

@Composable
fun DonkiDialog(evento: DonkiModel, onDismiss: () -> Unit) {
    val context = LocalContext.current // Necessário para abrir o link
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
                Text("☀️ Evento Solar", color = Color(0xFFF2C94C), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Nota da NASA:", color = Color.Gray, fontSize = 12.sp)
                Text(evento.note ?: "Nenhuma nota fornecida para este evento.", color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(24.dp))

                // Botão para ver a notícia no painel espacial da NASA
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://iswa.gsfc.nasa.gov/IswaSystemWebApp/"))
                    context.startActivity(intent)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver Notícia Completa")
                }

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Fechar", color = Color.Gray) }
            }
        }
    }
}

@Composable
fun NeoDialog(neo: NeoModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
                Text("☄️ Detalhes do Asteroide", color = Color(0xFF2D9CDB), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(neo.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                DetailRow("Perigoso?", if (neo.is_potentially_hazardous_asteroid) "Sim" else "Não")
                DetailRow("Diâmetro Máx:", "%.2f km".format(neo.estimated_diameter.kilometers.estimated_diameter_max))

                Spacer(modifier = Modifier.height(24.dp))

                // Botão atualizado com o texto solicitado
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(neo.nasa_jpl_url))
                    context.startActivity(intent)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver Notícia Completa")
                }

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Fechar", color = Color.Gray) }
            }
        }
    }
}

@Composable
fun LoadingBox() {
    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFFF2994A))
    }
}

@Composable
fun VerMaisButton(viewModel: MainViewModel) {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), contentAlignment = Alignment.Center) {
        Button(onClick = { viewModel.fetchNasaApod() }) { Text("Carregar Mais") }
    }
}

@Composable
fun ISSTrackerCard(viewModel: MainViewModel, onClick: () -> Unit) {
    val issData = viewModel.issData
    val user = viewModel.usuarioLogado

    // Estado para guardar o texto de distância
    var distanciaText by remember { mutableStateOf("Calculando distância...") }

    // Calcula a distância sempre que a ISS ou o Usuário mudarem de posição
    LaunchedEffect(issData, user) {
        val userLat = user?.latitude
        val userLng = user?.longitude
        val issLat = issData?.latitude
        val issLng = issData?.longitude

        if (userLat != null && userLng != null && issLat != null && issLng != null) {
            val results = FloatArray(1)
            android.location.Location.distanceBetween(userLat, userLng, issLat, issLng, results)
            val distKm = results[0] / 1000 // Converter metros para Km
            distanciaText = "A ${String.format(Locale.US, "%.0f", distKm)} km de você"
        } else if (issData != null) {
            distanciaText = "Velocidade: ${String.format(Locale.US, "%.0f", issData.velocity)} km/h"
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.RocketLaunch, null, tint = Color(0xFF2D9CDB), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Rastreador ISS", color = Color.White, fontWeight = FontWeight.Bold)
                // Usando a variável de estado que calcula a distância
                Text(distanciaText, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
@Composable
fun ApodCard(apod: ApodModel) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth().clickable {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://apod.nasa.gov/apod/")))
        }
    ) {
        Column {
            AsyncImage(model = apod.imagemUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().height(180.dp), contentScale = ContentScale.Crop)
            Column(modifier = Modifier.padding(16.dp)) {
                Text(apod.titulo, color = Color.White, fontWeight = FontWeight.Bold)
                Text(apod.explicacao ?: "Sem explicação disponível.", color = Color.Gray, fontSize = 13.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}