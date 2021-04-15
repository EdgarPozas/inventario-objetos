package com.edgarpozas.inventario_objetos.models

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.edgarpozas.inventario_objetos.utils.Utils
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.jvm.internal.Intrinsics




data class Position(
        var id: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var altitude: Double = 0.0,
        var room: String = "",
        var createdBy: String = ""
){

    companion object{
        suspend fun getById(context: Context, id: String, db: SQLiteDatabase): Position? {
            if(!Utils.isNetworkAvailable(context)){
                val cursor=db.rawQuery("select * from positions where _id='${id}'",null)
                return createFromCursor(cursor)
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context, "/api/position/id/$id")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val position: JSONObject =res.getJSONObject("position")
                return createFromJSON(position)
            }
            return null
        }

        suspend fun getAll(context: Context, db: SQLiteDatabase): ArrayList<Position>? {
            if(!Utils.isNetworkAvailable(context)){
                val cursor=db.rawQuery("select _id from positions",null)
                val arr=ArrayList<Position>()
                if(!cursor.moveToFirst()){
                    return arr;
                }
                do{
                    val id=cursor.getString(0)
                    val cursorLocal=db.rawQuery("select * from positions where _id='${id}'",null)
                    arr.add(createFromCursor(cursorLocal)!!)
                }while(cursor.moveToNext())
                return arr
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context, "/api/position")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val list =ArrayList<Position>()
                val objects: JSONArray =res.getJSONArray("positions")
                for (i in 0 until objects.length()){
                    val object_= createFromJSON(objects.getJSONObject(i))
                    list.add(object_)
                }
                return list
            }
            return null
        }

        fun createFromJSON(json: JSONObject):Position {
            val position = Position()
            position.id=json.getString("_id")
            position.latitude=json.getDouble("latitude")
            position.longitude=json.getDouble("longitude")
            position.altitude=json.getDouble("altitude")
            position.room=json.getString("room")
            position.createdBy=json.getString("createdBy")
            return position
        }

        fun resetSQL(db: SQLiteDatabase): Boolean {
            db.execSQL("delete from positions")
            return true
        }

        fun createFromCursor(cursor: Cursor): Position? {
            if (!cursor.moveToFirst()) {
                return null
            }
            val position = Position()
            do {
                position.id= cursor.getString(0)
                position.latitude=cursor.getDouble(1)
                position.longitude=cursor.getDouble(2)
                position.altitude=cursor.getDouble(3)
                position.room=cursor.getString(4)
                position.createdBy=cursor.getString(5)
            } while (cursor.moveToNext())
            return position
        }
    }

    fun createSQL(db: SQLiteDatabase): Boolean {
        db.execSQL("insert into positions(_id,latitude,longitude,altitude,room,createdBy)" +
                " values('${id}',${latitude},${ longitude},${ altitude},'${ room }','${ createdBy }')")
        return true
    }

    fun toJSON(): JSONObject {
        var json= JSONObject()
        json.put("latitude", latitude)
        json.put("longitude", longitude)
        json.put("altitude", altitude)
        json.put("room", room)
        json.put("createdBy", createdBy)
        return json
    }
}
