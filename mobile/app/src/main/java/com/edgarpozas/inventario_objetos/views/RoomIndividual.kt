package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.Storage

class RoomIndividual : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_individual)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title="Espacio individual"

        val id=intent?.getStringExtra("id")
        val room=Storage.getInstance().rooms.toList().find { r-> r.id==id }
        findViewById<TextView>(R.id.editRoomName).text = room?.name
        findViewById<TextView>(R.id.editDescriptionRoom).text = room?.description
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}