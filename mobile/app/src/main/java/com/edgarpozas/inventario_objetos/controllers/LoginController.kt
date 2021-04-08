package com.edgarpozas.inventario_objetos.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.utils.ID
import com.edgarpozas.inventario_objetos.utils.USERID
import com.edgarpozas.inventario_objetos.utils.Utils
import com.edgarpozas.inventario_objetos.views.Login
import com.edgarpozas.inventario_objetos.views.Principal
import com.edgarpozas.inventario_objetos.views.RecoveryPassword
import com.edgarpozas.inventario_objetos.views.Register

class LoginController(val login: Login) {

    fun goToRegister(){
        val destiny = Intent(
            login.baseContext,
            Register::class.java
        )
        login.startActivity(destiny)
    }

    fun goToRecovery(){
        val destiny = Intent(
            login.baseContext,
            RecoveryPassword::class.java
        )
        login.startActivity(destiny)
    }

    fun goToPrincipal(user: User){

        val sharedPreferences: SharedPreferences =login.getSharedPreferences(
            ID,
            Context.MODE_PRIVATE
        )

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putString(USERID, user.id)
        editor.apply()

        Storage.getInstance().user=user

        val destiny = Intent(
            login.baseContext,
            Principal::class.java
        )

        login.startActivity(destiny)
        login.finish()
    }

    suspend fun login(user: User):Boolean{
        return user.login(login)
    }
}