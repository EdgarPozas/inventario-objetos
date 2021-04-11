package com.edgarpozas.inventario_objetos.models

import android.content.Context
import com.edgarpozas.inventario_objetos.utils.SERVER
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.content.TextContent
import io.ktor.http.*
import io.ktor.http.content.*
import org.json.JSONObject

class DataBase {

    private fun DataBase(){
    }

    suspend fun sendQueryHttp(context: Context,path:String,httpMethod:HttpMethod,json:JSONObject): JSONObject{
        val client = HttpClient(CIO) {
            expectSuccess = false
        }
        val response: HttpResponse = client.request{
            url(SERVER+path)
            method = httpMethod
            body=TextContent(json.toString(), contentType = ContentType.Application.Json)
        }
        client.close()
        return JSONObject(response.readText())
    }

    suspend fun uploadFileHttp(context: Context,path:String,httpMethod:HttpMethod,data:List<PartData>): JSONObject{
        val client = HttpClient(CIO) {
            expectSuccess = false
        }
        val response: HttpResponse = client.request{
            url(SERVER+path)
            method = httpMethod
            body= MultiPartFormDataContent(data)
        }
        client.close()
        return JSONObject(response.readText())
    }

    suspend fun downloadFileHttp(context: Context,path:String,httpMethod:HttpMethod): HttpResponse{
        val client = HttpClient(CIO) {
            expectSuccess = false
        }
        val response: HttpResponse = client.request{
            url(path)
            method = httpMethod
        }
        client.close()
        return response
    }

    fun getQueryLocal(){

    }

    suspend fun getQueryHttp(context: Context,path:String) : JSONObject{
        val client = HttpClient(CIO) {
            expectSuccess = false
        }
        val response: HttpResponse = client.get(SERVER+path)
        client.close()
        return JSONObject(response.readText())
    }

    companion object{
        var dataBase:DataBase?=null

        fun getInstance() : DataBase{
            if(dataBase==null){
                this.dataBase= DataBase()
            }
            return this.dataBase!!
        }
    }
}