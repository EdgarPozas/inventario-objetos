package com.edgarpozas.inventario_objetos.views.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.People

class PeopleListAdapter(
        var peopleView: People,
        var users:List<User>,
    ): BaseAdapter() {

    override fun getCount(): Int {
        return users.count()
    }

    override fun getItem(position: Int): Any {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater:LayoutInflater= LayoutInflater.from(peopleView.requireContext())
        val v=layoutInflater.inflate(R.layout.people_list_item,null)

        val user=users[position]

        v.findViewById<TextView>(R.id.name).text = "${user.firstName} ${user.lastName}"
        v.findViewById<TextView>(R.id.description).text = user.email

        return v
    }
}