package com.edgarpozas.inventario_objetos.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.utils.ID
import com.edgarpozas.inventario_objetos.utils.USERID
import com.edgarpozas.inventario_objetos.views.*


class SplashController(val splash: Splash) {

    fun goToNext(){

        val sharedPreferences: SharedPreferences =splash.getSharedPreferences(
            ID,
            Context.MODE_PRIVATE
        )

        var destiny = Intent(
            splash.baseContext,
            Login::class.java
        )

        if (sharedPreferences.contains(USERID)){
            destiny = Intent(
                splash.baseContext,
                Principal::class.java
            )
            Storage.getInstance().user.getById()
        }

        splash.startActivity(destiny)
    }

}