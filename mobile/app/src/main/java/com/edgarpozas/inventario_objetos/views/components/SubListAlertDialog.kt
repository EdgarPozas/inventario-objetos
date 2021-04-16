package com.edgarpozas.inventario_objetos.views.components

import android.app.AlertDialog
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.Room

class SubListAlertDialog(val fragment: Fragment) {

    fun createAlertDialog(title:String) : AlertDialog? {
        return fragment?.let {
            val builder = AlertDialog.Builder(it.requireContext())
            val inflater = fragment.layoutInflater;
            val view=inflater.inflate(R.layout.alert_sublist,null)
            builder.apply {
                setTitle(title)
                setView(view)
            }
            builder.create()
        }
    }
}