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
import com.edgarpozas.inventario_objetos.views.SubList

class SubListListAdapter(
        var subList: SubList,
        var items:ArrayList<String>,
    ): BaseAdapter() {

    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater:LayoutInflater= LayoutInflater.from(subList.requireContext())
        val v=layoutInflater.inflate(R.layout.sublist_list_item,null)

        val item=items[position].split(";")

        v.findViewById<TextView>(R.id.name).text = item[0]
        v.findViewById<TextView>(R.id.items).text = item[1]

        return v
    }
}