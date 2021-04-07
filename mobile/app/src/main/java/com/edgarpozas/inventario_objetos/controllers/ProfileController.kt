package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.*


class ProfileController(val profile: Profile) {

    fun update(user: User):Boolean{
        return user.update()
    }

    fun delete(user: User):Boolean{
        return user.delete()
    }
}