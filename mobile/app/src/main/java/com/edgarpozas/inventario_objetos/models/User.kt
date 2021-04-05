package com.edgarpozas.inventario_objetos.models

import com.edgarpozas.inventario_objetos.utils.Utils

data class User(var firstName:String="",var lastName:String="",var email:String="",var password:String=""){
    fun login(): Int{
        if (email.isEmpty()|| password.isEmpty())
            return -1
        if(!Utils.isEmailValid(email))
            return -2

        return 0
    }

    fun register(): Int{
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()|| password.isEmpty())
            return -1
        if(!Utils.isEmailValid(email))
            return -2

        return 0
    }

    fun recovery():Int{
        if( email.isEmpty())
            return -1
        if(!Utils.isEmailValid(email))
            return -2

        return 0
    }
}
