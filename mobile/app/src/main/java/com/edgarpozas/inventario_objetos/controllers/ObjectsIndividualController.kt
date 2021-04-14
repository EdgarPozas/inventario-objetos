package com.edgarpozas.inventario_objetos.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.fragment.app.Fragment
import com.edgarpozas.inventario_objetos.models.*
import com.edgarpozas.inventario_objetos.models.Room
import com.edgarpozas.inventario_objetos.views.*
import com.google.android.gms.maps.SupportMapFragment
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import org.json.JSONObject


class ObjectsIndividualController(val objectsIndividual: ObjectsIndividual ) {

    suspend fun getAllUsers(db: SQLiteDatabase) {
        Storage.getInstance().users.clear()
        val users= User.getAll(objectsIndividual,db)
        if(users!=null){
            for(user in users){
                Storage.getInstance().users.add(user)
            }
        }
    }

    suspend fun getAllRooms(db: SQLiteDatabase) {
        Storage.getInstance().rooms.clear()
        val rooms= Room.getAll(objectsIndividual,db)
        if(rooms!=null){
            for(room in rooms){
                Storage.getInstance().rooms.add(room)
            }
        }
    }

    suspend fun getAllPositions(db: SQLiteDatabase) {
        Storage.getInstance().positions.clear()
        val positions= Position.getAll(objectsIndividual,db)
        if(positions!=null){
            for(position in positions){
                Storage.getInstance().positions.add(position)
            }
        }
    }

    suspend fun uploadFile(formData:List<PartData>) :JSONObject{
        val res= DataBase.getInstance().uploadFileHttp(
            objectsIndividual,
            "/api/file",
            HttpMethod.Post,
            formData
        )

        return res
    }

    suspend fun downloadFile(path:String) :ByteArray{
        val res= DataBase.getInstance().downloadFileHttp(
            objectsIndividual,
            path,
            HttpMethod.Get
        )

        return res.readBytes()
    }
}