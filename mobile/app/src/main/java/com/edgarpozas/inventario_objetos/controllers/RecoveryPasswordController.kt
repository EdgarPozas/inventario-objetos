package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.Login
import com.edgarpozas.inventario_objetos.views.Principal
import com.edgarpozas.inventario_objetos.views.RecoveryPassword
import com.edgarpozas.inventario_objetos.views.Register


class RecoveryPasswordController(val recoveryPassword: RecoveryPassword) {

    fun recovery(user: User):Boolean{
        return user.recovery()
    }

    fun goToLogin(){
        val destiny = Intent(
            recoveryPassword.baseContext,
            Login::class.java
        )
        recoveryPassword.startActivity(destiny)
    }
}