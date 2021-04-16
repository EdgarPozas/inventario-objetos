package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import com.edgarpozas.inventario_objetos.models.*
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.views.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.jvm.internal.Intrinsics


class SimilarController(val similar: Similar) {

    suspend fun searchSimilar(formData:List<PartData>) : ArrayList<Objects>? {
        val res= DataBase.getInstance().uploadFileHttp(
            similar.requireContext(),
            "/api/similar",
            HttpMethod.Post,
            formData
        )

        val status:Int=res["status"].toString().toInt()
        if(status==200){
            val list =ArrayList<Objects>()
            val percentages: JSONArray =res.getJSONArray("percentages")
            for (i in 0 until percentages.length()){
                val object_= Objects.createFromJSON(percentages.getJSONObject(i).getJSONObject("object"))
                val percentage=percentages.getJSONObject(i).getDouble("percentage")
                object_._percentage=percentage
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

    fun goToIndividual(objects: Objects) {
        val destiny = Intent(similar.requireContext(), ObjectsIndividual::class.java)
        destiny.putExtra("id", objects.id)
        similar.requireContext().startActivity(destiny)
    }

}
