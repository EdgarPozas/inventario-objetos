package com.edgarpozas.inventario_objetos.models

import android.content.Context
import com.edgarpozas.inventario_objetos.utils.Utils
import com.edgarpozas.inventario_objetos.views.Objects
import io.ktor.http.*
import org.json.JSONObject

data class Objects(var id:String="", var name:String="", var description:String="", var createdBy:String=""){
    val dataBase=DataBase.getInstance()

    suspend fun create(context: Context): Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/user", HttpMethod.Post,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
        return true
    }

    suspend fun update(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/user", HttpMethod.Post,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun delete(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/user", HttpMethod.Post,toJSON())
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
    }

    fun toJSON(): JSONObject {
        var json= JSONObject()
        json.put("name",name)
        json.put("description",description)
        return json
    }

}
