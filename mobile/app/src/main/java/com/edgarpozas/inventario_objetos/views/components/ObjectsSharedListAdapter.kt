package com.edgarpozas.inventario_objetos.views.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.ObjectsEdit
import com.edgarpozas.inventario_objetos.views.People

class ObjectsSharedListAdapter(
        var objectsView: ObjectsEdit,
        var shared:List<User>,
    ): BaseAdapter() {

    override fun getCount(): Int {
        return shared.count()
    }

    override fun getItem(position: Int): Any {
        return shared[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater:LayoutInflater= LayoutInflater.from(objectsView)
        val v=layoutInflater.inflate(R.layout.objects_shared_list_item,null)

        val share=shared[position]

        v.findViewById<TextView>(R.id.name).text = "${share.firstName} ${share.lastName}"

        val btnDelete: ImageButton?=v.findViewById(R.id.btnDelete)

        btnDelete?.setOnClickListener(View.OnClickListener {
            objectsView.deleteShared(it,position)
        })

        return v
    }
}