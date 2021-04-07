package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
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

}