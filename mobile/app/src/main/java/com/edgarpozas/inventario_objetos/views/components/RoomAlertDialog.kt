package com.edgarpozas.inventario_objetos.views.components

import android.app.AlertDialog
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.Room

class RoomAlertDialog(val fragment: Fragment) {

    var room:Room?=null

    private fun setValues(v: View){
        v.findViewById<EditText>(R.id.editRoomName).setText(room?.name)
        v.findViewById<EditText>(R.id.editDescriptionRoom).setText(room?.description)
    }

    fun createAlertDialog(title:String) : AlertDialog? {
        return fragment?.let {
            val builder = AlertDialog.Builder(it.requireContext())
            val inflater = fragment.layoutInflater;
            val view=inflater.inflate(R.layout.alert_room,null)
            setValues(view)
            builder.apply {
                setTitle(title)
                setView(view)
            }
            builder.create()
        }
    }
}