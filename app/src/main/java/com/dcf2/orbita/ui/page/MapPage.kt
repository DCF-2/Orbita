package com.dcf2.orbita.ui.page

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.BitmapDrawable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.core.content.ContextCompat
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.dcf2.orbita.ui.nav.BottomNavItem
import com.dcf2.orbita.viewmodel.MainViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapPage(viewModel: MainViewModel, navController: NavHostController) {
    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasLocationPermission = isGranted }
    )

    val recife = LatLng(-8.0631, -34.8711)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(recife, 12f)
    }

    val observacoes = viewModel.posts.toList()

    // Removido a Column e o Text para o mapa ocupar todo o espaço do Scaffold!
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
    ) {
        if (hasLocationPermission) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true,
                    mapType = MapType.HYBRID
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true,
                    zoomControlsEnabled = false
                )
            ) {
                observacoes.forEach { post ->
                    if (post.latitude != null && post.longitude != null) {
                        val location = LatLng(post.latitude, post.longitude)
                        val markerState = rememberMarkerState(key = post.id, position = location)
                        val iconState = rememberCustomMarkerIcon(post.fotoUrl, context)

                        Marker(
                            state = markerState,
                            title = post.titulo,
                            snippet = "Por: ${post.userName} (Clique para ir ao Feed)",
                            icon = iconState.value ?: BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                            onInfoWindowClick = {
                                // Navega para o feed quando a caixa de info do pino for clicada
                                navController.navigate(BottomNavItem.Home.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFF2994A),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Precisamos da sua localização.",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB))
                ) {
                    Text("Permitir Acesso")
                }
            }
        }
    }
}

@Composable
fun rememberCustomMarkerIcon(url: String?, context: Context): State<BitmapDescriptor?> {
    val descriptorState = remember(url) { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(url) {
        if (url == null) return@LaunchedEffect

        val imageLoader = Coil.imageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .size(130, 130)
            .allowHardware(false)
            .build()

        val result = (imageLoader.execute(request) as? SuccessResult)?.drawable
        if (result is BitmapDrawable) {
            val bitmap = result.bitmap
            val circularBitmap = getCircularBitmapWithBorder(bitmap, 130)
            descriptorState.value = BitmapDescriptorFactory.fromBitmap(circularBitmap)
        }
    }

    return descriptorState
}

fun getCircularBitmapWithBorder(bitmap: Bitmap, size: Int): Bitmap {
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
    }

    paint.color = android.graphics.Color.WHITE
    paint.style = Paint.Style.FILL
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

    val borderWidth = 8f
    val radius = (size / 2f) - borderWidth

    val imageOutput = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val imageCanvas = Canvas(imageOutput)

    paint.color = android.graphics.Color.BLACK
    imageCanvas.drawCircle(size / 2f, size / 2f, radius, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, size, size, false)
    imageCanvas.drawBitmap(scaledBitmap, 0f, 0f, paint)

    paint.xfermode = null
    canvas.drawBitmap(imageOutput, 0f, 0f, paint)

    return output
}