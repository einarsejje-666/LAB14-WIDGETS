package com.example.widgets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class NotasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pantalla("Mis notas") {
                Tarjeta("Laboratorio 14", "Terminar el widget con Jetpack Glance")
                Tarjeta("Proyecto final", "Revisar el widget de CooRuta")
                Tarjeta("Informe", "Subir los commits y el video")
                BotonVolver()
            }
        }
    }
}
