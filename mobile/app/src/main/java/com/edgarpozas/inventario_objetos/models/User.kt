package com.edgarpozas.inventario_objetos.models

import android.content.Context
import com.edgarpozas.inventario_objetos.utils.Utils
import io.ktor.http.*
import org.json.JSONArray
import org.json.JSONObject

data class User(var id:String="",var firstName:String="",var lastName:String="",var email:String="",var password:String="",var active:Boolean=false,var verified:Boolean=false){
    private val dataBase=DataBase.getInstance()

    companion object{

        suspend fun getById(context: Context,id: String): User? {
            if(!Utils.isNetworkAvailable(context)){
                return null
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context,"/api/user/id/$id")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val json: JSONObject =res.getJSONObject("user")
                return createFromJSON(json)
            }
            return null
        }

        suspend fun getByEmail(context: Context,email: String): Boolean{
            if(!Utils.isNetworkAvailable(context)){
                return false
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context,"/api/user/email/$email")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val user: JSONObject =res.getJSONObject("user")
                createFromJSON(user)
            }
            return status==200
        }

        suspend fun getAll(context: Context): ArrayList<User>? {

            if(!Utils.isNetworkAvailable(context)){
                return null
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context,"/api/user")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val list =ArrayList<User>()
                val users: JSONArray =res.getJSONArray("users")
                for (i in 0 until users.length()){
                    val user= createFromJSON(users.getJSONObject(i))
                    list.add(user)
                }
                return list
            }
            return null
        }

        suspend fun login(context: Context,user:User): User?{
            if(!Utils.isNetworkAvailable(context)){
                return null
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.sendQueryHttp(context,"/api/user/login", HttpMethod.Post,user.toJSON())
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val user: JSONObject =res.getJSONObject("user")
                return createFromJSON(user)
            }
            return null
        }

        suspend fun register(context: Context,user:User): Boolean{
            if(!Utils.isNetworkAvailable(context)){
                return false
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.sendQueryHttp(context,"/api/user", HttpMethod.Post,user.toJSON())
            val status:Int=res["status"].toString().toInt()
            return status==200
        }

        suspend fun recovery(context: Context,user:User):Boolean{
            if(!Utils.isNetworkAvailable(context)){
                return false
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.sendQueryHttp(context,"/api/email/recovery", HttpMethod.Post,user.toJSON())
            val status:Int=res["status"].toString().toInt()
            println(status)
            return status==200
        }

        fun createFromJSON(json:JSONObject):User{
            val user=User()
            user.id=json.getString("_id")
            user.firstName=json.getString("firstName")
            user.lastName=json.getString("lastName")
            user.email=json.getString("email")
            user.password=json.getString("password")
            user.verified=json.getBoolean("verified")
            user.active=json.getBoolean("active")
            return user
        }
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
        verified=false
        active=false
    }

    fun toJSON():JSONObject{
        var json=JSONObject()
        json.put("firstName",firstName)
        json.put("lastName",lastName)
        json.put("email",email)
        json.put("password",password)
        return json
    }
}
