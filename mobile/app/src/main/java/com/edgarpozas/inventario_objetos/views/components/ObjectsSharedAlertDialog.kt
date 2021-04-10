package com.edgarpozas.inventario_objetos.views.components

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.Room
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.views.ObjectsEdit

class ObjectsSharedAlertDialog(val objectsEdit: ObjectsEdit) {

    fun createAlertDialog(title:String, listener:DialogInterface.OnClickListener ) : AlertDialog? {
        return objectsEdit?.let {
            val builder = AlertDialog.Builder(it)
            val usersToShow=Storage.getInstance().users

            usersToShow.removeAll(objectsEdit.users)

            val size=usersToShow.size
            val users= Array<String>(size) { i ->
                val user=usersToShow[i]
                return@Array "${user.firstName} ${user.lastName}"
            }

            builder.apply {
                setTitle(title)
                setItems(users,listener)
            }
            builder.create()
        }
    }
}