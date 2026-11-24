package com.example.widgets.cooruta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.widgets.BotonVolver
import com.example.widgets.Pantalla
import com.example.widgets.Tarjeta

/** Detalle de un viaje. El widget la abre al tocar la tarjeta del próximo viaje. */
class DetalleViajeActivity : ComponentActivity() {

    companion object {
        const val EXTRA_ID = "viajeId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viaje = ViajesRepositorio.buscar(intent.getStringExtra(EXTRA_ID))
        setContent {
            val context = LocalContext.current
            val reservados by remember { ViajesRepositorio.reservadosFlow(context) }
                .collectAsState(ViajesRepositorio.reservados(context))
            val reservado = viaje.id in reservados
            Pantalla("Detalle del viaje") {
                Tarjeta("${viaje.origen} → ${viaje.destino}", "Sale hoy a las ${viaje.hora}")
                Tarjeta("Conductor", viaje.conductor)
                Tarjeta(
                    "Asientos libres",
                    "${ViajesRepositorio.asientosLibres(viaje, reservados)} de ${viaje.asientos} · ${viaje.precio} por persona"
                )
                Tarjeta("Estado", if (reservado) "Tienes un asiento reservado" else "Aún no reservas")
                BotonReserva(viaje.id, reservado, Modifier.fillMaxWidth())
                BotonVolver()
            }
        }
    }
}
