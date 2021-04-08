package com.edgarpozas.inventario_objetos.views.components

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.RoomController
import com.edgarpozas.inventario_objetos.models.Room
import com.google.android.material.snackbar.Snackbar

class RoomListAdapter(
        var roomView:com.edgarpozas.inventario_objetos.views.Room,
        var rooms:List<Room>,
    ): BaseAdapter() {

    override fun getCount(): Int {
        return rooms.count()
    }

    override fun getItem(position: Int): Any {
        return rooms[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater:LayoutInflater= LayoutInflater.from(roomView.requireContext())
        val v=layoutInflater.inflate(R.layout.room_list_item,null)

        val room=rooms[position]

        v.findViewById<TextView>(R.id.name).text = room.name
        v.findViewById<TextView>(R.id.description).text = room.description
        val btnEdit: ImageButton?=v.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton?=v.findViewById(R.id.btnDelete)

        btnEdit?.setOnClickListener(View.OnClickListener {
            roomView.editRoom(roomView.requireView(),room)
        })
        btnDelete?.setOnClickListener(View.OnClickListener {
            roomView.deleteRoom(roomView.requireView(),room)
        })

        return v
    }
}