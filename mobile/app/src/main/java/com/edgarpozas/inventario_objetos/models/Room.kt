package com.edgarpozas.inventario_objetos.models

import com.edgarpozas.inventario_objetos.utils.Utils

data class Room(var id:String="",var name:String="", var description:String="", var createdBy:String=""){


    fun create(): Boolean{
        return true
    }

    fun update():Boolean{
        return true
    }

    fun delete():Boolean{
        return true
    }

    fun isAllEmpty():Boolean{
        return name.isEmpty() || description.isEmpty()
    }

}
