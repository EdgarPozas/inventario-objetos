package com.edgarpozas.inventario_objetos.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.utils.ID
import com.edgarpozas.inventario_objetos.utils.USERID
import com.edgarpozas.inventario_objetos.utils.Utils
import com.edgarpozas.inventario_objetos.views.*


class SplashController(val splash: Splash) {

    suspend fun goToNext(){

        val sharedPreferences: SharedPreferences =splash.getSharedPreferences(
            ID,
            Context.MODE_PRIVATE
        )

        var destiny = Intent(
            splash.baseContext,
            Login::class.java
        )

        if (sharedPreferences.contains(USERID)){
            val id=sharedPreferences.getString(USERID,"")!!
            if(id.isNotEmpty()){
                val user= User.getById(splash,id)
                if(user!=null){
                    destiny = Intent(
                        splash.baseContext,
                        Principal::class.java
                    )
                    Storage.getInstance().user=user
                }else{
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                }
            }
        }

        splash.startActivity(destiny)
    }

}
