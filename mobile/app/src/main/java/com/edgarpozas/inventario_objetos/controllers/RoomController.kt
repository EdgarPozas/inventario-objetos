package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.*


class RoomController(val room: Room) {

    fun create(room: com.edgarpozas.inventario_objetos.models.Room):Boolean{
        val created=room.create()
        if(created) {
            Storage.getInstance().rooms.add(room)
        }
        return created
    }

    fun update(room: com.edgarpozas.inventario_objetos.models.Room):Boolean{
        val updated=room.update()
        if(updated){
            val index = Storage.getInstance().rooms.indexOf(room)
            Storage.getInstance().rooms[index]=room
        }
        return updated
    }

    fun delete(room: com.edgarpozas.inventario_objetos.models.Room):Boolean{
        val deleted=room.delete()
        if(deleted){
            Storage.getInstance().rooms.remove(room)
        }
        return deleted
    }
}