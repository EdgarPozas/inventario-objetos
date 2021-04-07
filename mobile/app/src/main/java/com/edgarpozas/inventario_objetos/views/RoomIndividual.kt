package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.edgarpozas.inventario_objetos.R

class RoomIndividual : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_individual)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}