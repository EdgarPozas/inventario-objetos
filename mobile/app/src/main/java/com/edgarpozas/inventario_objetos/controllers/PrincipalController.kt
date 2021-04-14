package com.edgarpozas.inventario_objetos.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.utils.ID
import com.edgarpozas.inventario_objetos.utils.USERID
import com.edgarpozas.inventario_objetos.views.Login
import com.edgarpozas.inventario_objetos.views.Principal
import com.edgarpozas.inventario_objetos.views.RecoveryPassword
import com.edgarpozas.inventario_objetos.views.Register


class PrincipalController(val principal: Principal) {

    fun goToLogin(){
        val destiny = Intent(
            principal.baseContext,
            Login::class.java
        )
        principal.startActivity(destiny)
    }

    fun closeSession(){
        val sharedPreferences: SharedPreferences =principal.getSharedPreferences(
            ID,
            Context.MODE_PRIVATE
        )

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        Storage.getInstance().user.reset()
        Storage.getInstance().rooms.clear()
        Storage.getInstance().objects.clear()
        Storage.getInstance().positions.clear()
        Storage.getInstance().users.clear()
        
        val destiny = Intent(
            principal.baseContext,
            Login::class.java
        )

        principal.startActivity(destiny)
        principal.finish()
    }

}