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
import com.edgarpozas.inventario_objetos.views.ObjectsIndividual
import com.edgarpozas.inventario_objetos.views.People

class ObjectsIndividualSharedListAdapter(
        var objectsInvidual: ObjectsIndividual,
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
        val layoutInflater:LayoutInflater= LayoutInflater.from(objectsInvidual)
        val v=layoutInflater.inflate(R.layout.objects_individual_shared_list_item,null)

        val share=shared[position]

        v.findViewById<TextView>(R.id.name).text = "${share.firstName} ${share.lastName}"

        return v
    }
}