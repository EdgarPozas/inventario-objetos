package com.edgarpozas.inventario_objetos.views.components

import android.app.AlertDialog
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.models.Room

class ObjectsAlertDialog(val fragment: Fragment) {

    var objects: Objects?=null

    private fun setValues(v: View){
        v.findViewById<EditText>(R.id.editRoomName).setText(objects?.name)
        v.findViewById<EditText>(R.id.editDescriptionRoom).setText(objects?.description)
    }

    fun createAlertDialog(title:String) : AlertDialog? {
        return fragment?.let {
            val builder = AlertDialog.Builder(it.requireContext())
            val inflater = fragment.layoutInflater;
            val view=inflater.inflate(R.layout.alert_objects,null)
            setValues(view)
            builder.apply {
                setTitle(title)
                setView(view)
            }
            builder.create()
        }
    }
}