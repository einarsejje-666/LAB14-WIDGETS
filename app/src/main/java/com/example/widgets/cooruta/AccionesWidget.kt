package com.example.widgets.cooruta

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll

/** Pasa al siguiente viaje sin abrir la app (estado propio de cada widget). */
class SiguienteViajeAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val actual = prefs[CooRutaWidget.KEY_INDICE] ?: 0
            prefs[CooRutaWidget.KEY_INDICE] = (actual + 1) % ViajesRepositorio.viajes.size
        }
        CooRutaWidget().update(context, glanceId)
    }
}

/** Reserva o cancela el asiento desde el widget; la app ve el mismo cambio. */
class ReservarViajeAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val id = parameters[CooRutaWidget.KEY_VIAJE] ?: return
        ViajesRepositorio.alternarReserva(context, id)
        CooRutaWidget().updateAll(context)
    }
}
