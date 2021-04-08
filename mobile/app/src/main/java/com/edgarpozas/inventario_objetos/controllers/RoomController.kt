package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.*


class RoomController(val room: Room) {

    suspend fun create(room: com.edgarpozas.inventario_objetos.models.Room):Boolean{
        val created=room.create(this.room.requireContext())
        if(created) {
            Storage.getInstance().rooms.add(room)
        }
        return created
    }

    suspend fun update(room: com.edgarpozas.inventario_objetos.models.Room):Boolean{
        val updated=room.update(this.room.requireContext())
        if(updated){
            val index = Storage.getInstance().rooms.indexOf(room)
            Storage.getInstance().rooms[index]=room
        }
        return updated
    }

    suspend fun delete(room: com.edgarpozas.inventario_objetos.models.Room):Boolean{
        val deleted=room.delete(this.room.requireContext())
        if(deleted){
            Storage.getInstance().rooms.remove(room)
        }
        return deleted
    }

    fun goToIndividual(room: com.edgarpozas.inventario_objetos.models.Room){
        val destiny = Intent(
            this.room.context,
            RoomIndividual::class.java
        )
        this.room.startActivity(destiny)
    }
}