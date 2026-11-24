package com.example.widgets.cooruta

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.FilledButton
import androidx.glance.appwidget.components.OutlineButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

/** Widget del proyecto final: muestra el próximo viaje de CooRuta. */
class CooRutaWidget : GlanceAppWidget() {

    companion object {
        val KEY_INDICE = intPreferencesKey("indice_viaje")
        val KEY_VIAJE = ActionParameters.Key<String>(DetalleViajeActivity.EXTRA_ID)

        // Tamaños que diseñamos (Responsive): 2x1, 4x2 y 4x3 aprox.
        private val PEQUENO = DpSize(110.dp, 50.dp)
        private val MEDIANO = DpSize(250.dp, 110.dp)
        private val GRANDE = DpSize(250.dp, 220.dp)
    }

    override val sizeMode = SizeMode.Responsive(setOf(PEQUENO, MEDIANO, GRANDE))

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // Se recompone solo cuando cambian las reservas (desde la app o el widget)
            val reservados by ViajesRepositorio.reservadosFlow(context)
                .collectAsState(ViajesRepositorio.reservados(context))
            val indice = currentState(KEY_INDICE) ?: 0
            val viajes = ViajesRepositorio.viajes
            GlanceTheme(colors = CooRutaColores) {
                Contenido(viajes[indice % viajes.size], indice, reservados)
            }
        }
    }

    @Composable
    private fun Contenido(viaje: Viaje, indice: Int, reservados: Set<String>) {
        val size = LocalSize.current
        if (size.width < MEDIANO.width || size.height < MEDIANO.height) {
            VistaCompacta(viaje)
            return
        }
        Scaffold(
            titleBar = {
                TitleBar(
                    startIcon = ImageProvider(com.example.widgets.R.drawable.ic_cooruta),
                    iconColor = GlanceTheme.colors.primary,
                    title = "CooRuta",
                    actions = {
                        CircleIconButton(
                            imageProvider = ImageProvider(com.example.widgets.R.drawable.ic_siguiente),
                            contentDescription = "Siguiente viaje",
                            backgroundColor = null,
                            contentColor = GlanceTheme.colors.primary,
                            onClick = actionRunCallback<SiguienteViajeAction>()
                        )
                    }
                )
            },
            horizontalPadding = 12.dp
        ) {
            Column(GlanceModifier.fillMaxSize()) {
                TarjetaProximo(viaje, reservados)
                if (size.height >= GRANDE.height) {
                    Spacer(GlanceModifier.height(8.dp))
                    ListaDespues(indice, reservados)
                }
                Spacer(GlanceModifier.defaultWeight())
                Acciones(viaje, viaje.id in reservados)
                Spacer(GlanceModifier.height(12.dp))
            }
        }
    }

    /** 2x1: solo hora y ruta; al tocar abre el detalle. */
    @Composable
    private fun VistaCompacta(viaje: Viaje) {
        Column(
            modifier = GlanceModifier.fillMaxSize()
                .background(GlanceTheme.colors.primaryContainer)
                .cornerRadius(android.R.dimen.system_app_widget_background_radius)
                .padding(horizontal = 12.dp)
                .clickable(abrirDetalle(viaje)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(viaje.hora, style = estilo(12, GlanceTheme.colors.onPrimaryContainer))
            Text(
                "${viaje.origen} → ${viaje.destino}", maxLines = 1,
                style = estilo(14, GlanceTheme.colors.onPrimaryContainer, negrita = true)
            )
        }
    }

    @Composable
    private fun TarjetaProximo(viaje: Viaje, reservados: Set<String>) {
        val reservado = viaje.id in reservados
        val fondo = if (reservado) GlanceTheme.colors.primaryContainer else GlanceTheme.colors.secondaryContainer
        val texto = if (reservado) GlanceTheme.colors.onPrimaryContainer else GlanceTheme.colors.onSecondaryContainer
        Column(
            modifier = GlanceModifier.fillMaxWidth()
                .background(fondo)
                .cornerRadius(16.dp)
                .padding(12.dp)
                .clickable(abrirDetalle(viaje))
        ) {
            Text(
                if (reservado) "Hoy · ${viaje.hora} · Reservado ✓" else "Próximo viaje · ${viaje.hora}",
                style = estilo(12, texto)
            )
            Text("${viaje.origen} → ${viaje.destino}", maxLines = 1, style = estilo(16, texto, negrita = true))
            Text(
                "${viaje.conductor} · ${ViajesRepositorio.asientosLibres(viaje, reservados)} asientos libres · ${viaje.precio}",
                maxLines = 1, style = estilo(12, texto)
            )
        }
    }

    /** 4x3 o más: los dos viajes que siguen. */
    @Composable
    private fun ListaDespues(indice: Int, reservados: Set<String>) {
        val viajes = ViajesRepositorio.viajes
        Text("Después", style = estilo(12, GlanceTheme.colors.onSurfaceVariant, negrita = true))
        for (i in 1..2) {
            val v = viajes[(indice + i) % viajes.size]
            Row(
                modifier = GlanceModifier.fillMaxWidth().padding(vertical = 4.dp).clickable(abrirDetalle(v)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(v.hora, style = estilo(12, GlanceTheme.colors.primary, negrita = true))
                Spacer(GlanceModifier.width(8.dp))
                Text(
                    "${v.origen} → ${v.destino} · ${ViajesRepositorio.asientosLibres(v, reservados)} libres",
                    maxLines = 1, style = estilo(12, GlanceTheme.colors.onSurface)
                )
            }
        }
    }

    @Composable
    private fun Acciones(viaje: Viaje, reservado: Boolean) {
        Row(GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            FilledButton(
                text = if (reservado) "Cancelar" else "Reservar",
                icon = ImageProvider(com.example.widgets.R.drawable.ic_asiento),
                onClick = actionRunCallback<ReservarViajeAction>(actionParametersOf(KEY_VIAJE to viaje.id)),
                modifier = GlanceModifier.defaultWeight()
            )
            Spacer(GlanceModifier.width(8.dp))
            OutlineButton(
                text = "Ver todos",
                contentColor = GlanceTheme.colors.primary,
                onClick = actionStartActivity<ViajesActivity>(),
                modifier = GlanceModifier.defaultWeight()
            )
        }
    }

    private fun abrirDetalle(viaje: Viaje) =
        actionStartActivity<DetalleViajeActivity>(actionParametersOf(KEY_VIAJE to viaje.id))

    private fun estilo(
        tam: Int,
        color: androidx.glance.unit.ColorProvider,
        negrita: Boolean = false
    ) = TextStyle(
        fontSize = tam.sp,
        color = color,
        fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal
    )
}
