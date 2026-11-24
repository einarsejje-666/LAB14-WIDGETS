package com.example.widgets.cooruta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.updateAll
import com.example.widgets.BotonVolver
import com.example.widgets.Pantalla
import kotlinx.coroutines.launch

/** Lista de viajes de hoy. El widget de CooRuta abre esta vista con "Ver todos". */
class ViajesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val reservados by remember { ViajesRepositorio.reservadosFlow(context) }
                .collectAsState(ViajesRepositorio.reservados(context))
            Pantalla("Viajes de hoy - CooRuta") {
                ViajesRepositorio.viajes.forEach { viaje ->
                    TarjetaViaje(viaje, reservados, onClick = {
                        context.startActivity(
                            Intent(context, DetalleViajeActivity::class.java)
                                .putExtra(DetalleViajeActivity.EXTRA_ID, viaje.id)
                        )
                    })
                }
                BotonVolver()
            }
        }
    }
}

@Composable
fun TarjetaViaje(viaje: Viaje, reservados: Set<String>, onClick: () -> Unit) {
    val reservado = viaje.id in reservados
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = if (reservado) CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) else CardDefaults.cardColors()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(viaje.hora, style = MaterialTheme.typography.labelLarge)
                Text("${viaje.origen} → ${viaje.destino}", fontWeight = FontWeight.Bold)
                Text(
                    "${viaje.conductor} · ${ViajesRepositorio.textoAsientos(viaje, reservados)} · ${viaje.precio}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            BotonReserva(viaje.id, reservado)
        }
    }
}

/** Reserva o cancela y avisa al widget para que se actualice al instante. */
@Composable
fun BotonReserva(id: String, reservado: Boolean, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val accion = {
        ViajesRepositorio.alternarReserva(context, id)
        scope.launch { actualizarWidget(context) }
        Unit
    }
    if (reservado) OutlinedButton(onClick = accion, modifier = modifier) { Text("Cancelar") }
    else Button(onClick = accion, modifier = modifier) { Text("Reservar") }
}

suspend fun actualizarWidget(context: Context) = CooRutaWidget().updateAll(context)
