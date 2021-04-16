package com.edgarpozas.inventario_objetos.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.models.Position
import com.edgarpozas.inventario_objetos.models.Room
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.utils.ID
import com.edgarpozas.inventario_objetos.utils.USERID
import com.edgarpozas.inventario_objetos.views.*


class SplashController(val splash: Splash) {

    suspend fun goToNext(db:SQLiteDatabase){

        val sharedPreferences: SharedPreferences =splash.getSharedPreferences(
            ID,
            Context.MODE_PRIVATE
        )

        var destiny = Intent(
            splash.baseContext,
            Login::class.java
        )

        if (sharedPreferences.contains(USERID)){
            val id=sharedPreferences.getString(USERID, "")!!
            if(id.isNotEmpty()){
                val user= User.getById(splash, id,db)
                if(user!=null){
                    destiny = Intent(
                        splash.baseContext,
                        Principal::class.java
                    )
                    Storage.getInstance().user=user
                }else{
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                }
            }
        }

        splash.startActivity(destiny)
    }

    suspend fun getAll(db: SQLiteDatabase){
        Storage.getInstance().users.clear()
        val users= User.getAll(splash,db)
        if(users!=null){
            for(user in users){
                Storage.getInstance().users.add(user)
            }
        }
        Storage.getInstance().rooms.clear()
        val rooms= Room.getAll(splash,db)
        if(rooms!=null){
            for(room in rooms){
                Storage.getInstance().rooms.add(room)
            }
        }
        Storage.getInstance().positions.clear()
        val positions= Position.getAll(splash,db)
        if(positions!=null){
            for(position in positions){
                Storage.getInstance().positions.add(position)
            }
        }
        Storage.getInstance().objects.clear()
        val objects= Objects.getAll(splash,db)
        if(objects!=null){
            for(object_ in objects){
                Storage.getInstance().objects.add(object_)
            }
        }
    }

    fun sync(db: SQLiteDatabase) {
        User.resetSQL(db)
        for (v in Storage.getInstance().users) {
            v.createSQL(db)
        }
        Room.resetSQL(db)
        for (v2 in Storage.getInstance().rooms) {
            v2.createSQL(db)
        }
        Position.resetSQL(db)
        for (v3 in Storage.getInstance().positions) {
            v3.createSQL(db)
        }
        Objects.resetSQL(db)
        for (v4 in Storage.getInstance().objects) {
            v4.createSQL(db)
        }
    }
}
