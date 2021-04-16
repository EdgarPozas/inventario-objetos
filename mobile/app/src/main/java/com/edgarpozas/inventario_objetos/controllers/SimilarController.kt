package com.edgarpozas.inventario_objetos.controllers

import com.edgarpozas.inventario_objetos.models.*
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.views.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import org.json.JSONArray
import org.json.JSONObject


class SimilarController(val similar: Similar) {

    suspend fun searchSimilar(formData:List<PartData>) : ArrayList<Objects>? {
        val res= DataBase.getInstance().uploadFileHttp(
            similar.requireContext(),
            "/api/search",
            HttpMethod.Post,
            formData
        )

        val status:Int=res["status"].toString().toInt()
        if(status==200){
            val list =ArrayList<Objects>()
            val objects: JSONArray =res.getJSONArray("objects")
            for (i in 0 until objects.length()){
                val object_= Objects.createFromJSON(objects.getJSONObject(i))
                list.add(object_)
            }
            return list
        }
        return null
    }

    suspend fun downloadFile(path:String) :ByteArray{
        val res= DataBase.getInstance().downloadFileHttp(
            similar.requireContext(),
            path,
            HttpMethod.Get
        )

        return res.readBytes()
    }
}
