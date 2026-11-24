package com.example.widgets.cooruta

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/** Viaje compartido hacia (o desde) Tecsup, como en CooRuta. */
data class Viaje(
    val id: String,
    val hora: String,
    val origen: String,
    val destino: String,
    val conductor: String,
    val asientos: Int,
    val precio: String
)

/**
 * Fuente de datos de la app y del widget. Las reservas se guardan en
 * SharedPreferences para que la app y el widget vean lo mismo.
 */
object ViajesRepositorio {

    private const val ARCHIVO = "cooruta"
    private const val CLAVE_RESERVADOS = "reservados"

    val viajes = listOf(
        Viaje("v1", "7:10 a. m.", "Cayma", "Tecsup", "Luis M.", 3, "S/ 4.00"),
        Viaje("v2", "7:25 a. m.", "Yanahuara", "Tecsup", "Carla R.", 2, "S/ 3.50"),
        Viaje("v3", "7:40 a. m.", "Cerro Colorado", "Tecsup", "Diego P.", 1, "S/ 5.00"),
        Viaje("v4", "1:15 p. m.", "Tecsup", "Plaza de Armas", "Ana V.", 3, "S/ 4.00")
    )

    fun buscar(id: String?): Viaje = viajes.firstOrNull { it.id == id } ?: viajes.first()

    private fun prefs(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    fun reservados(context: Context): Set<String> =
        prefs(context).getStringSet(CLAVE_RESERVADOS, emptySet())!!.toSet()

    /** Emite las reservas cada vez que cambian (desde la app o desde el widget). */
    fun reservadosFlow(context: Context): Flow<Set<String>> = callbackFlow {
        val p = prefs(context)
        val escucha = SharedPreferences.OnSharedPreferenceChangeListener { _, clave ->
            if (clave == CLAVE_RESERVADOS) trySend(reservados(context))
        }
        p.registerOnSharedPreferenceChangeListener(escucha)
        trySend(reservados(context))
        awaitClose { p.unregisterOnSharedPreferenceChangeListener(escucha) }
    }

    /** Reserva el asiento si no estaba reservado; si ya lo estaba, lo cancela. */
    fun alternarReserva(context: Context, id: String) {
        val actual = reservados(context).toMutableSet()
        if (!actual.add(id)) actual.remove(id)
        prefs(context).edit(commit = true) { putStringSet(CLAVE_RESERVADOS, actual) }
    }

    fun asientosLibres(viaje: Viaje, reservados: Set<String>): Int =
        viaje.asientos - if (viaje.id in reservados) 1 else 0

    /** "1 asiento libre" / "3 asientos libres" */
    fun textoAsientos(viaje: Viaje, reservados: Set<String>, corto: Boolean = false): String {
        val n = asientosLibres(viaje, reservados)
        return when {
            corto -> if (n == 1) "1 libre" else "$n libres"
            n == 1 -> "1 asiento libre"
            else -> "$n asientos libres"
        }
    }
}
