package com.edgarpozas.inventario_objetos.models

import android.content.Context
import android.webkit.JsPromptResult
import com.edgarpozas.inventario_objetos.utils.Utils
import com.edgarpozas.inventario_objetos.views.Profile
import io.ktor.http.*
import org.json.JSONObject

data class User(var id:String="",var firstName:String="",var lastName:String="",var email:String="",var password:String=""){
    val dataBase=DataBase.getInstance()

    suspend fun getById(context: Context): Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.getQueryHttp(context,"/api/user/id/$id")
        val status:Int=res["status"].toString().toInt()
        if(status==200){
            val user: JSONObject =res.getJSONObject("user")
            createFromJSON(user)
        }
        return status==200
    }

    suspend fun getByEmail(context: Context): Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.getQueryHttp(context,"/api/user/email/$email")
        val status:Int=res["status"].toString().toInt()
        if(status==200){
            val user: JSONObject =res.getJSONObject("user")
            createFromJSON(user)
        }
        return status==200
    }

    suspend fun login(context: Context): Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/user/login", HttpMethod.Post,toJSON())
        val status:Int=res["status"].toString().toInt()
        if(status==200){
            val user: JSONObject =res.getJSONObject("user")
            createFromJSON(user)
        }
        return status==200
    }

    suspend fun register(context: Context): Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/user", HttpMethod.Post,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun recovery(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/email/recovery", HttpMethod.Post,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun update(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/user/$id", HttpMethod.Put,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun delete(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context,"/api/user/$id", HttpMethod.Delete,toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    fun isAllEmpty():Boolean{
        return firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()|| password.isEmpty()
    }

    fun isValidEmail():Boolean{
        return Utils.isEmailValid(email)
    }

    fun reset(){
        id=""
        firstName=""
        lastName=""
        email=""
        password=""
    }

    fun toJSON():JSONObject{
        var json=JSONObject()
        json.put("firstName",firstName)
        json.put("lastName",lastName)
        json.put("email",email)
        json.put("password",password)
        return json
    }
    fun createFromJSON(json:JSONObject){
        id=json["_id"].toString()
        firstName=json["firstName"].toString()
        lastName=json["lastName"].toString()
        email=json["email"].toString()
        password=json["password"].toString()
    }
}
