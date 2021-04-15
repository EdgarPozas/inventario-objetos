package com.edgarpozas.inventario_objetos.models

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.edgarpozas.inventario_objetos.utils.Utils
import io.ktor.http.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.jvm.internal.Intrinsics




data class Room(
        var id: String = "",
        var name: String = "",
        var description: String = "",
        var createdBy: String = "",
        var active: Boolean = true
){
    private val dataBase=DataBase.getInstance()

    companion object{
        suspend fun getById(context: Context, id: String, db: SQLiteDatabase): Room? {

            if(!Utils.isNetworkAvailable(context)){
                val cursor=db.rawQuery("select * from rooms where _id='${id}'",null)
                return createFromCursor(cursor)
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context, "/api/room/id/$id")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val room: JSONObject =res.getJSONObject("room")
                return createFromJSON(room)
            }
            return null
        }

        suspend fun getAll(context: Context, db: SQLiteDatabase): ArrayList<Room>? {

            if(!Utils.isNetworkAvailable(context)){
                val cursor=db.rawQuery("select _id from rooms",null)
                val arr=ArrayList<Room>()
                if(!cursor.moveToFirst()){
                    return arr;
                }
                do{
                    val id=cursor.getString(0)
                    val cursorLocal=db.rawQuery("select * from rooms where _id='${id}'",null)
                    arr.add(createFromCursor(cursorLocal)!!)
                }while(cursor.moveToNext())
                return arr
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context, "/api/room")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val list =ArrayList<Room>()
                val rooms: JSONArray =res.getJSONArray("rooms")
                for (i in 0 until rooms.length()){
                    val room= createFromJSON(rooms.getJSONObject(i))
                    list.add(room)
                }
                return list
            }
            return null
        }
        fun createFromJSON(json: JSONObject):Room{
            val room=Room()
            room.id=json.getString("_id")
            room.name=json.getString("name")
            room.description=json.getString("description")
            room.createdBy=json.getString("createdBy")
            room.active=json.getBoolean("active")
            return room
        }

        fun resetSQL(db: SQLiteDatabase): Boolean {
            db.execSQL("delete from rooms")
            return true
        }

        fun createFromCursor(cursor: Cursor): Room? {
            if (!cursor.moveToFirst()) {
                return null
            }
            val room = Room()
            do {
                room.id=cursor.getString(0)
                room.name=cursor.getString(1)
                room.description=cursor.getString(2)
                room.active=cursor.getInt(3)==1
                room.createdBy=cursor.getString(4)
            } while (cursor.moveToNext())
            return room
        }
    }

    fun createSQL(db: SQLiteDatabase): Boolean {
        db.execSQL("insert into rooms(_id,name,description,createdBy,active)" +
                " values('${id}','${name}','${description}','${createdBy}',${(if (active) 1 else 0)})")
        return true
    }


    suspend fun create(context: Context): Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context, "/api/room", HttpMethod.Post, toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun update(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context, "/api/room/$id", HttpMethod.Put, toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun delete(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context, "/api/room/$id", HttpMethod.Delete, toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    fun isAllEmpty():Boolean{
        return name.isEmpty() || description.isEmpty()
    }

    fun reset(){
        id=""
        name=""
        description=""
        createdBy=""
        active=true
    }

    fun toJSON(): JSONObject {
        var json= JSONObject()
        json.put("name", name)
        json.put("description", description)
        json.put("createdBy", createdBy)
        return json
    }

}
