package com.example.widgets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pantalla("Página Principal") {
                Text("Laboratorio 14 - Widgets con Jetpack Glance")
                BotonIr("Mis notas", NotasActivity::class.java)
                BotonIr("Mi perfil", PerfilActivity::class.java)
            }
        }
    }
}
