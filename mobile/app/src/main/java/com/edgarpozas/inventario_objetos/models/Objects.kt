package com.edgarpozas.inventario_objetos.models

import android.content.Context
import com.edgarpozas.inventario_objetos.utils.Utils
import io.ktor.http.*
import org.json.JSONArray
import org.json.JSONObject

data class Objects(
    var id:String="",
    var name:String="",
    var description:String="",
    var functionality:String="",
    var tags:ArrayList<String>?=null,
    var urlImage:String="",
    var urlSound:String="",
    var price:Double= 0.0,
    var sharedBy:ArrayList<String>?=null,
    var positions:ArrayList<Position>?=null,
    var createdBy:String=""
    ){

    private val dataBase=DataBase.getInstance()

    companion object{
        suspend fun getById(context: Context,id: String): Objects? {
            if(!Utils.isNetworkAvailable(context)){
                return null
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context,"/api/object/id/$id")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val room: JSONObject =res.getJSONObject("object")
                return createFromJSON(room)
            }
            return null
        }

        suspend fun getAll(context: Context): ArrayList<Objects>? {
            if(!Utils.isNetworkAvailable(context)){
                return null
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context,"/api/object")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val list =ArrayList<Objects>()
                val objects: JSONArray =res.getJSONArray("objects")
                for (i in 0 until objects.length()){
                    val object_= createFromJSON(objects.getJSONObject(i))
                    list.add(object_)
                }
                return list
            }
            return null
        }

        fun createFromJSON(json:JSONObject):Objects{
            val objects=Objects()
            println(json)
            objects.id=json.getString("_id")
            objects.name=json.getString("name")
            objects.description=json.getString("description")
            objects.functionality=json.getString("functionality")
            val tags_=ArrayList<String>()
            val arrayTags=json.getJSONArray("tags")
            for (i in 0 until arrayTags.length()){
                tags_.add(arrayTags.getString(i))
            }
            objects.tags=tags_
            objects.urlImage=json.getString("urlImage")
            objects.urlSound=json.getString("urlSound")
            objects.price=json.getDouble("price")
            val sharedBy_=ArrayList<String>()
            val arraySharedBy=json.getJSONArray("sharedBy")
            for (i in 0 until arraySharedBy.length()){
                sharedBy_.add(arraySharedBy.getString(i))
            }
            objects.sharedBy=sharedBy_
            val positions_=ArrayList<Position>()
            val arrayPositions=json.getJSONArray("positions")
            for (i in 0 until arrayPositions.length()){
                val positionsAux=Position.createFromJSON(arrayPositions.getJSONObject(i))
                positions_.add(positionsAux)
            }
            objects.positions=positions_
            objects.createdBy=json.getString("createdBy")
            return objects
        }
    }


    suspend fun create(context: Context): Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/object", HttpMethod.Post,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun update(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/object/$id", HttpMethod.Put,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun delete(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/object/$id", HttpMethod.Delete,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    fun isAllEmpty():Boolean{
        return name.isEmpty() || description.isEmpty() || functionality.isEmpty()
    }

    fun reset(){
        id=""
        name=""
        description=""
        functionality=""
        tags=null
        urlImage=""
        urlSound=""
        price=0.0
        sharedBy=null
        positions=null
        createdBy=""
    }

    fun toJSON(): JSONObject {
        var json= JSONObject()
        json.put("name",name)
        json.put("description",description)
        json.put("functionality",functionality)
        var tagsArray=JSONArray()
        tags?.forEach { x-> tagsArray.put(x) }
        json.put("tags",tagsArray)
        json.put("urlImage",urlImage)
        json.put("urlSound",urlSound)
        json.put("price",price)
        val listSharedBy=JSONArray()
        sharedBy?.forEach { x-> listSharedBy.put(x) }
        json.put("sharedBy",listSharedBy)
        val listPositions=JSONArray()
        positions?.forEach { x-> listPositions.put(x.toJSON()) }
        json.put("positions",listPositions)
        json.put("createdBy",createdBy)
        return json
    }
}
