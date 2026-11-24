package com.example.widgets

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.widgets.ui.theme.WidgetsLabTheme

/** Estructura común de las tres vistas de la app. */
@Composable
fun Pantalla(titulo: String, contenido: @Composable () -> Unit) {
    WidgetsLabTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))
                Text(titulo, style = MaterialTheme.typography.headlineMedium)
                contenido()
            }
        }
    }
}

@Composable
fun Tarjeta(titulo: String, detalle: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(detalle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun BotonIr(texto: String, destino: Class<out Activity>) {
    val context = LocalContext.current
    Button(onClick = { context.startActivity(Intent(context, destino)) }, modifier = Modifier.fillMaxWidth()) {
        Text(texto)
    }
}

@Composable
fun BotonVolver() {
    val activity = LocalContext.current as Activity
    OutlinedButton(onClick = { activity.finish() }, modifier = Modifier.fillMaxWidth()) {
        Text("Volver")
    }
}
