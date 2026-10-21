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
import com.example.lab10.data.PostApiService
import com.example.lab10.data.PostModel
import kotlinx.coroutines.delay

@Composable
fun ContenidoPostsListado(navController: NavHostController, servicio: PostApiService) {
    var listaPosts: SnapshotStateList<PostModel> = remember { mutableStateListOf() }
    LaunchedEffect(Unit) {
        val listado = servicio.selectPosts()
        listaPosts.clear()
        // limitamos a 20 para no saturar la pantalla con 100 resultados de la API
        listado.take(20).forEach { listaPosts.add(it) }
    }

    LazyColumn(
    ) {
        item {
            Row(
                modifier = Modifier.fillParentMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.2f)
                )
                Text(
                    text = "TITULO",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.6f)
                )
                Text(
                    text = "Acción",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.2f)
                )
            }
        }
        items(listaPosts) { item ->
            Row(
                modifier = Modifier.padding(start = 8.dp, bottom = 12.dp).fillParentMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${item.id}", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.2f))
                Text(text = item.title, fontSize = 16.sp, modifier = Modifier.weight(0.6f))
                Column(Modifier.weight(0.2f)) {
                    IconButton(
                        onClick = {
                            navController.navigate("postVer/${item.id}")
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Ver")
                    }
                    IconButton(
                        onClick = {
                            navController.navigate("postDel/${item.id}")
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Borrar")
                    }
                }
            }
        }
    }
}

@Composable
fun ContenidoPostEditar(navController: NavHostController, servicio: PostApiService, pid: Int = 0) {
    var id by remember { mutableStateOf<Int>(pid) }
    var title by remember { mutableStateOf<String>("") }
    var body by remember { mutableStateOf<String>("") }
    var grabar by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        if (id != 0) {
            val objPost = servicio.selectPost(id.toString())
            if (objPost.isSuccessful) {
                title = objPost.body()?.title ?: ""
                body = objPost.body()?.body ?: ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = id.toString(),
            onValueChange = { },
            label = { Text("ID (solo lectura)") },
            readOnly = true,
            singleLine = true
        )
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título: ") },
            singleLine = true
        )
        TextField(
            value = body,
            onValueChange = { body = it },
            label = { Text("Cuerpo/Contenido:") }
        )
        Button(
            onClick = {
                if (title.isNotBlank() && body.isNotBlank()) {
                    grabar = true
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Grabar Post", fontSize = 16.sp)
        }
    }

    if (grabar) {
        LaunchedEffect(Unit) {
            val objPost = PostModel(id = if(id == 0) 101 else id, title = title, body = body, userId = 1)
            if (id == 0) {
                servicio.insertPost(objPost)
            } else {
                servicio.updatePost(id.toString(), objPost)
            }
            grabar = false
            navController.navigate("posts")
        }
    }
}

@Composable
fun ContenidoPostEliminar(navController: NavHostController, servicio: PostApiService, id: Int) {
    var showDialog by remember { mutableStateOf(true) }
    var borrar by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmar Eliminación") },
            text = { Text("¿Está seguro de eliminar el Post $id?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        borrar = true
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { 
                    showDialog = false
                    navController.navigate("posts") 
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
    if (borrar) {
        LaunchedEffect(Unit) {
            servicio.deletePost(id.toString())
            borrar = false
            navController.navigate("posts")
        }
    }
}