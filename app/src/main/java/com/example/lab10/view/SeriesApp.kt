package com.example.lab10.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab10.data.DeviceApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun SeriesApp() {
    // SERVICIO WEB: Restful-API (Devices)
    val urlBaseDevices = "https://api.restful-api.dev/"
    val retrofitDevices = Retrofit.Builder().baseUrl(urlBaseDevices)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val servicioDevices = retrofitDevices.create(DeviceApiService::class.java)

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.padding(top = 40.dp),
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        floatingActionButton = { BotonFAB(navController) },
        content = { paddingValues -> Contenido(paddingValues, navController, servicioDevices) }
    )
}

@Composable
fun BotonFAB(navController: NavHostController) {
    val cbeState by navController.currentBackStackEntryAsState()
    val rutaActual = cbeState?.destination?.route
    if (rutaActual == "devices") {
        FloatingActionButton(
            containerColor = Color.Blue,
            contentColor = Color.White,
            onClick = { navController.navigate("deviceNuevo") }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Device"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "DEVICES API",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun BarraInferior(navController: NavHostController) {
    NavigationBar(
        containerColor = Color.LightGray
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = navController.currentDestination?.route == "inicio",
            onClick = { navController.navigate("inicio") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.List, contentDescription = "Devices") },
            label = { Text("Devices API") },
            selected = navController.currentDestination?.route == "devices",
            onClick = { navController.navigate("devices") }
        )
    }
}

@Composable
fun Contenido(
    pv: PaddingValues,
    navController: NavHostController,
    servicioDevices: DeviceApiService
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
    ) {
        NavHost(
            navController = navController,
            startDestination = "inicio" // Ruta de inicio
        ) {
            composable("inicio") { ScreenInicio() }
            
            // RUTAS DE DEVICES API PUBLICA
            composable("devices") { ContenidoDevicesListado(navController, servicioDevices) }
            composable("deviceNuevo") {
                ContenidoDeviceEditar(navController, servicioDevices, "0")
            }
            composable("deviceVer/{id}", arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )) {
                ContenidoDeviceEditar(navController, servicioDevices, it.arguments!!.getString("id")!!)
            }
            composable("deviceDel/{id}", arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )) {
                ContenidoDeviceEliminar(navController, servicioDevices, it.arguments!!.getString("id")!!)
            }
        }
    }
}