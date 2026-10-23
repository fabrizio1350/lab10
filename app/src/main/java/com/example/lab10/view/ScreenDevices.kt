package com.example.lab10.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lab10.data.DeviceApiService
import com.example.lab10.data.DeviceModel
import com.example.lab10.data.DeviceData

@Composable
fun ContenidoDevicesListado(navController: NavHostController, servicio: DeviceApiService) {
    val listaDevices: SnapshotStateList<DeviceModel> = remember { mutableStateListOf() }
    LaunchedEffect(Unit) {
        val listado = servicio.selectDevices()
        listaDevices.clear()
        listado.forEach { listaDevices.add(it) }
    }

    LazyColumn {
        item {
            Row(
                modifier = Modifier.fillParentMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("NOMBRE", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
                Text("ACCIÓN", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.3f))
            }
        }
        items(listaDevices) { item ->
            Row(
                modifier = Modifier.padding(8.dp).fillParentMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.name, fontSize = 16.sp, modifier = Modifier.weight(0.7f))
                Row(Modifier.weight(0.3f)) {
                    IconButton(onClick = { navController.navigate("deviceVer/${item.id}") }) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { navController.navigate("deviceDel/${item.id}") }) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Borrar")
                    }
                }
            }
        }
    }
}

@Composable
fun ContenidoDeviceEditar(navController: NavHostController, servicio: DeviceApiService, pid: String = "0") {
    var id by remember { mutableStateOf(pid) }
    var name by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var grabar by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        if (id != "0") {
            val response = servicio.selectDevice(id)
            if (response.isSuccessful) {
                name = response.body()?.name ?: ""
                color = response.body()?.data?.color ?: ""
                price = response.body()?.data?.price?.toString() ?: ""
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (id != "0") {
            Text(text = "ID: $id", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        }
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del Equipo:") },
            singleLine = true,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = color,
            onValueChange = { color = it },
            label = { Text("Color:") },
            singleLine = true,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio:") },
            singleLine = true,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = {
                if (name.isNotBlank()) grabar = true
            }
        ) {
            Text("Grabar Dispositivo", fontSize = 16.sp)
        }
    }

    if (grabar) {
        LaunchedEffect(Unit) {
            val deviceData = DeviceData(color = color, price = price.toDoubleOrNull())
            val objDevice = DeviceModel(name = name, data = deviceData)

            if (id == "0") {
                servicio.insertDevice(objDevice)
            } else {
                servicio.updateDevice(id, objDevice)
            }
            grabar = false
            navController.navigate("devices")
        }
    }
}

@Composable
fun ContenidoDeviceEliminar(navController: NavHostController, servicio: DeviceApiService, id: String) {
    var showDialog by remember { mutableStateOf(true) }
    var borrar by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmar Eliminación") },
            text = { Text("¿Está seguro de eliminar el dispositivo?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    borrar = true
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { 
                    showDialog = false
                    navController.navigate("devices") 
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
    if (borrar) {
        LaunchedEffect(Unit) {
            servicio.deleteDevice(id)
            borrar = false
            navController.navigate("devices")
        }
    }
}