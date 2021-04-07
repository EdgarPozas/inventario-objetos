package com.edgarpozas.inventario_objetos.views.components

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.edgarpozas.inventario_objetos.R

class GenericAlertDialog() {

    fun createAlertDialog(
        context: Context,
        title:String,
        message:String,
        btnYes:DialogInterface.OnClickListener,
        btnNo:DialogInterface.OnClickListener
    ) : AlertDialog{
        return context.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton(R.string.button_yes, btnYes)
                setNegativeButton(R.string.button_no, btnNo)
            }
            builder.create()
        }
    }
}