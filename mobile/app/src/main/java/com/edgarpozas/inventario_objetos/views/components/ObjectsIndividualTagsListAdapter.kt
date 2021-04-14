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

class ObjectsIndividualTagsListAdapter(
        var objectsInvidual: ObjectsIndividual,
        var tags:ArrayList<String>,
    ): BaseAdapter() {

    override fun getCount(): Int {
        return tags.count()
    }

    override fun getItem(position: Int): Any {
        return tags[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater:LayoutInflater= LayoutInflater.from(objectsInvidual)
        val v=layoutInflater.inflate(R.layout.objects_individual_tags_list_item,null)

        val tag=tags[position]

        v.findViewById<TextView>(R.id.name).text = tag

        return v
    }
}