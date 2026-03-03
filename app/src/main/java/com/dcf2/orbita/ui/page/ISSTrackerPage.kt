package com.dcf2.orbita.ui.page

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dcf2.orbita.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ISSTrackerPage(viewModel: MainViewModel, navController: NavController) {
    val issData = viewModel.issData

    val cameraPositionState = rememberCameraPositionState()
    val issMarkerState = rememberMarkerState()

    var showExtraInfo by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // CORREÇÃO: Ícone nativo instanciado na memória para evitar crash de concorrência!
    var issIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(issData) {
        if (issData != null) {
            val location = LatLng(issData.latitude, issData.longitude)
            issMarkerState.position = location

            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(location, 6.5f),
                durationMs = 1500
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF050B14))) {

        if (issData != null) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = MapType.SATELLITE),
                uiSettings = MapUiSettings(zoomControlsEnabled = false, compassEnabled = false)
            ) {
                MapEffect(Unit) {
                    if (issIcon == null) {
                        issIcon = createISSIcon()
                    }
                }
                issIcon?.let { icon ->
                    Marker(
                        state = issMarkerState,
                        title = "ISS (Estação Espacial Internacional)",
                        icon = icon,
                        onClick = {
                            showExtraInfo = true
                            true
                        }
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color(0xFF2D9CDB))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("A estabelecer contacto com a ISS...", color = Color.LightGray)
                }
            }
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
        }

        if (issData != null && !showExtraInfo) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isDaylight = issData.visibility == "daylight"
                    Icon(
                        imageVector = if (isDaylight) Icons.Default.WbSunny else Icons.Outlined.ModeNight,
                        contentDescription = null,
                        tint = if (isDaylight) Color.Yellow else Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isDaylight) "Luz solar" else "Sombra",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TelemetryCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.LocationOn,
                        title = "Coordenadas",
                        value = "${String.format(Locale.US, "%.2f", issData.latitude)}, ${String.format(Locale.US, "%.2f", issData.longitude)}"
                    )
                    TelemetryCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Speed,
                        title = "Velocidade",
                        value = "${String.format(Locale.US, "%.0f", issData.velocity)} km/h"
                    )
                }
            }
        }

        if (showExtraInfo && issData != null) {
            ModalBottomSheet(
                onDismissRequest = { showExtraInfo = false },
                sheetState = sheetState,
                containerColor = Color(0xFF121A28),
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Dados Técnicos (ISS)",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    InfoRow("Altitude Atual", "${String.format(Locale.US, "%.1f", issData.altitude)} km")
                    HorizontalDivider(color = Color(0xFF1E293B))

                    InfoRow(
                        "Visibilidade Solar",
                        if (issData.visibility == "daylight") "Luz do Sol ☀️" else "Sombra Escura 🌑"
                    )
                    HorizontalDivider(color = Color(0xFF1E293B))

                    InfoRow("Pegada (Footprint)", "${String.format(Locale.US, "%.1f", issData.footprint)}")
                    HorizontalDivider(color = Color(0xFF1E293B))

                    InfoRow("Timestamp", "${issData.timestamp}")

                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontSize = 16.sp)
        Text(value, color = Color(0xFF2D9CDB), fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TelemetryCard(modifier: Modifier = Modifier, icon: ImageVector, title: String, value: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.85f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF2D9CDB))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, color = Color(0xFF94A3B8), fontSize = 12.sp)
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

//Função desenha um círculo translúcido com Emoji em vez de vetor instável.
fun createISSIcon(): BitmapDescriptor {
    val size = 120
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Fundo escuro translúcido (tal como tinhas em Compose)
    val paint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.parseColor("#99000000") // Preto com alpha
        style = Paint.Style.FILL
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

    // Borda azul (o teu 2D9CDB)
    paint.color = android.graphics.Color.parseColor("#2D9CDB")
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 6f
    canvas.drawCircle(size / 2f, size / 2f, size / 2f - 3f, paint)

    // O Emoji de Foguetão (🚀) ou Estação
    val textPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("🛰️", size / 2f, size / 2f + 18f, textPaint) // Troquei para o emoji de Satélite, mas podes pôr o foguete de volta 🚀

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}