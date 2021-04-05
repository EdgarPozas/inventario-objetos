package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.Login
import com.edgarpozas.inventario_objetos.views.Principal
import com.edgarpozas.inventario_objetos.views.RecoveryPassword
import com.edgarpozas.inventario_objetos.views.Register


class RegisterController(val register: Register) {

    fun register(user: User):Int{
        return user.register()
    }

    fun goToLogin(){
        val destiny = Intent(
            register.baseContext,
            Login::class.java
        )
        register.startActivity(destiny)
    }
}