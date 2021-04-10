package com.edgarpozas.inventario_objetos.controllers

import android.content.Intent
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.*


class ProfileController(val profile: Profile) {

    suspend fun update(user: User):Boolean{
        return user.update(profile.requireContext())
    }

    suspend fun delete(user: User):Boolean{
        return user.delete(profile.requireContext())
    }

    fun goToLogin(){
        val destiny = Intent(
            profile.requireContext(),
            Principal::class.java
        )

        profile.startActivity(destiny)
        profile.activity?.finish()
    }
}