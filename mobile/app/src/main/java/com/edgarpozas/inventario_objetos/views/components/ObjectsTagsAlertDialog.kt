package com.edgarpozas.inventario_objetos.views.components

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.Room

class ObjectsTagsAlertDialog(val activity: Activity) {

    var tag:String?=null

    private fun setValues(v: View){
        v.findViewById<EditText>(R.id.editTagName).setText(tag)
    }

    fun createAlertDialog(title:String) : AlertDialog? {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = activity.layoutInflater;
            val view=inflater.inflate(R.layout.alert_objects_tags,null)
            setValues(view)
            builder.apply {
                setTitle(title)
                setView(view)
            }
            builder.create()
        }
    }
}