package com.edgarpozas.inventario_objetos.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.utils.ID
import com.edgarpozas.inventario_objetos.utils.USERID
import com.edgarpozas.inventario_objetos.utils.Utils
import com.edgarpozas.inventario_objetos.views.*


class PeopleController(val people: People) {

    suspend fun getAll(db: SQLiteDatabase) {
        Storage.getInstance().users.clear()
        val users=User.getAll(people.requireContext(),db)
        if(users!=null){
            for(user in users){
                Storage.getInstance().users.add(user)
            }
        }
    }
}
