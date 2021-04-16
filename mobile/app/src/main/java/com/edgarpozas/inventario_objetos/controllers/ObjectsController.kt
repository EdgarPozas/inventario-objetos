package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.fragment.app.Fragment
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.views.*
import kotlin.jvm.internal.Intrinsics


class ObjectsController(val fragment: Fragment) {

    suspend fun getAll(db: SQLiteDatabase) {
        Storage.getInstance().objects.clear()
        val objects= Objects.getAll(fragment.requireContext(), db)
        if(objects!=null){
            for(object_ in objects){
                Storage.getInstance().objects.add(object_)
            }
        }
    }

    suspend fun delete(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        return objects.delete(fragment.requireContext())
    }

    fun goToCreateObject(objects: Objects? = null){
        val destiny = Intent(
                fragment.requireContext(),
                ObjectsEdit::class.java
        )
        if(objects!=null){
            destiny.putExtra("id", objects.id)
        }
        fragment.requireContext().startActivity(destiny)
    }

    fun goToIndividual(objects: Objects) {
        val destiny = Intent(fragment.requireContext(), ObjectsIndividual::class.java)
        destiny.putExtra("id", objects.id)
        fragment.requireContext().startActivity(destiny)
    }

}