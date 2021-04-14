package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.views.*


class RoomController(val room: Room) {

    suspend fun getAll(db:SQLiteDatabase) {
        Storage.getInstance().rooms.clear()
        val rooms= com.edgarpozas.inventario_objetos.models.Room.getAll(room.requireContext(),db)
        if(rooms!=null){
            for(room_ in rooms){
                Storage.getInstance().rooms.add(room_)
            }
        }
    }

    suspend fun create(room: com.edgarpozas.inventario_objetos.models.Room):Boolean{
        return room.create(this.room.requireContext())
    }

    suspend fun update(room: com.edgarpozas.inventario_objetos.models.Room):Boolean{
        return room.update(this.room.requireContext())
    }

    suspend fun delete(room: com.edgarpozas.inventario_objetos.models.Room):Boolean{
        return room.delete(this.room.requireContext())
    }

    fun goToIndividual(room: com.edgarpozas.inventario_objetos.models.Room){
        val destiny = Intent(
            this.room.context,
            RoomIndividual::class.java
        )
        destiny.putExtra("id",room.id)
        this.room.startActivity(destiny)
    }
}