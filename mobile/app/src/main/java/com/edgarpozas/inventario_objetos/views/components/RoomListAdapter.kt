package com.edgarpozas.inventario_objetos.views.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.Room

class RoomListAdapter(var context: Context,var layout:Int,var rooms:List<Room>): BaseAdapter() {
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
        var v=convertView
        val layoutInflater:LayoutInflater= LayoutInflater.from(this.context)
        v=layoutInflater.inflate(R.layout.room_list_item,null)

        val room=rooms[position]

        v.findViewById<TextView>(R.id.name).text = room.name
        v.findViewById<TextView>(R.id.description).text = room.description

        return v
    }
}