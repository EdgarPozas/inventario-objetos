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
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.models.Room
import com.google.android.material.snackbar.Snackbar

class ObjectsListAdapter(
        var objectsView:com.edgarpozas.inventario_objetos.views.Objects,
        var objects:List<Objects>,
    ): BaseAdapter() {

    override fun getCount(): Int {
        return objects.count()
    }

    override fun getItem(position: Int): Any {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater:LayoutInflater= LayoutInflater.from(objectsView.requireContext())
        val v=layoutInflater.inflate(R.layout.objects_list_item,null)

        val room=objects[position]

        v.findViewById<TextView>(R.id.name).text = room.name
        v.findViewById<TextView>(R.id.description).text = room.description
        val btnEdit: ImageButton?=v.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton?=v.findViewById(R.id.btnDelete)

        btnEdit?.setOnClickListener(View.OnClickListener {
            objectsView.editObjects(objectsView.requireView(),room)
        })
        btnDelete?.setOnClickListener(View.OnClickListener {
            objectsView.deleteObjects(objectsView.requireView(),room)
        })

        return v
    }
}