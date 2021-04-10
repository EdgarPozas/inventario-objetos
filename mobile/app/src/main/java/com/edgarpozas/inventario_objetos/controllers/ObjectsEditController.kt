package com.edgarpozas.inventario_objetos.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.edgarpozas.inventario_objetos.models.Room
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.*
import com.google.android.gms.maps.SupportMapFragment


class ObjectsEditController(val objectsEdit: ObjectsEdit ) {

    suspend fun getAllUsers() {
        Storage.getInstance().users.clear()
        val users= User.getAll(objectsEdit)
        if(users!=null){
            for(user in users){
                Storage.getInstance().users.add(user)
            }
        }
    }

    suspend fun getAllRooms() {
        Storage.getInstance().rooms.clear()
        val rooms= Room.getAll(objectsEdit)
        if(rooms!=null){
            for(room in rooms){
                Storage.getInstance().rooms.add(room)
            }
        }
    }

    suspend fun create(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        return objects.create(objectsEdit)
    }

    suspend fun update(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        return objects.update(objectsEdit)
    }

    suspend fun delete(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        return objects.delete(objectsEdit)
    }

    fun goToPrincipal(){
        val destiny = Intent(
            objectsEdit,
            Principal::class.java
        )
        objectsEdit.startActivity(destiny)
    }
}