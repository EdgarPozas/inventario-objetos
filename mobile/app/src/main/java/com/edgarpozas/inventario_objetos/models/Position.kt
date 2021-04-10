package com.edgarpozas.inventario_objetos.models

import android.content.Context
import com.edgarpozas.inventario_objetos.utils.Utils
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

data class Position(
    var id:String="",
    var latitude: Double =0.0,
    var longitude:Double=0.0,
    var altitude:Double=0.0,
    var room:String="",
    var createdBy:String=""
    ){

    companion object{
        suspend fun getById(context: Context, id: String): Position? {
            if(!Utils.isNetworkAvailable(context)){
                return null
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context,"/api/position/id/$id")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val position: JSONObject =res.getJSONObject("position")
                return createFromJSON(position)
            }
            return null
        }

        suspend fun getAll(context: Context): ArrayList<Position>? {
            if(!Utils.isNetworkAvailable(context)){
                return null
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context,"/api/position")
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

        fun createFromJSON(json:JSONObject):Position {
            val position = Position()
            position.id=json.getString("_id")
            position.latitude=json.getDouble("latitude")
            position.longitude=json.getDouble("longitude")
            position.altitude=json.getDouble("altitude")
            position.room=json.getString("room")
            position.createdBy=json.getString("createdBy")
            return position
        }
    }

    fun toJSON(): JSONObject {
        var json= JSONObject()
        json.put("latitude",latitude)
        json.put("longitude",longitude)
        json.put("altitude",altitude)
        json.put("room",room)
        json.put("createdBy",createdBy)
        return json
    }
}
