package com.edgarpozas.inventario_objetos.models

import android.content.Context
import com.edgarpozas.inventario_objetos.utils.Utils
import io.ktor.http.*
import org.json.JSONArray
import org.json.JSONObject

data class Room(var id:String="",var name:String="", var description:String="", var createdBy:String=""){
    private val dataBase=DataBase.getInstance()

    companion object{
        suspend fun getById(context: Context,id: String): Room? {

            if(!Utils.isNetworkAvailable(context)){
                return null
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context,"/api/room/id/$id")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val room: JSONObject =res.getJSONObject("room")
                return createFromJSON(room)
            }
            return null
        }

        suspend fun getAll(context: Context): ArrayList<Room>? {

            if(!Utils.isNetworkAvailable(context)){
                return null
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context,"/api/room")
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
        fun createFromJSON(json:JSONObject):Room{
            val room=Room()
            room.id=json.getString("_id")
            room.name=json.getString("name")
            room.description=json.getString("description")
            room.createdBy=json.getString("createdBy")
            return room
        }
    }

    suspend fun create(context: Context): Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/room", HttpMethod.Post,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun update(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/room/$id", HttpMethod.Put,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun delete(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/room/$id", HttpMethod.Delete,toJSON())
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
    }

    fun toJSON(): JSONObject {
        var json= JSONObject()
        json.put("name",name)
        json.put("description",description)
        json.put("createdBy",createdBy)
        return json
    }

}
