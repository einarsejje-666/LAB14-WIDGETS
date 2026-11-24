package com.example.widgets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class PerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pantalla("Mi perfil") {
                Tarjeta("Einar Sejje", "Diseño y Desarrollo de Software - TECSUP")
                Tarjeta("Curso", "Aplicaciones Móviles")
                BotonVolver()
            }
        }
    }
}
