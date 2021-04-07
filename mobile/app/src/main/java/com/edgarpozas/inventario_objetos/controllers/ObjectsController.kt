package com.edgarpozas.inventario_objetos.controllers

import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.views.*


class ObjectsController(val objects: Objects) {

    fun create(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        val created=objects.create()
        if(created) {
            Storage.getInstance().objects.add(objects)
        }
        return created
    }

    fun update(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        val updated=objects.update()
        if(updated){
            val index = Storage.getInstance().objects.indexOf(objects)
            Storage.getInstance().objects[index]=objects
        }
        return updated
    }

    fun delete(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        val deleted=objects.delete()
        if(deleted){
            Storage.getInstance().objects.remove(objects)
        }
        return deleted
    }
}