package com.edgarpozas.inventario_objetos.models

import com.edgarpozas.inventario_objetos.utils.Utils

data class User(var id:String="",var firstName:String="",var lastName:String="",var email:String="",var password:String=""){
    fun login(): Boolean{
        return true
    }

    fun register(): Boolean{
        return true
    }

    fun recovery():Boolean{
        return true
    }

    fun update():Boolean{
        return true
    }

    fun delete():Boolean{
        return true
    }

    fun isAllEmpty():Boolean{
        return firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()|| password.isEmpty()
    }

    fun isValidEmail():Boolean{
        return Utils.isEmailValid(email)
    }
}
