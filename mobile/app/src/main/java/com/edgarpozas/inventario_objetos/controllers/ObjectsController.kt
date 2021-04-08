package com.edgarpozas.inventario_objetos.controllers

import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.views.*


class ObjectsController(val objects: Objects) {

    suspend fun create(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        val created=objects.create(this.objects)
        if(created) {
            Storage.getInstance().objects.add(objects)
        }
        return created
    }

    suspend fun update(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        val updated=objects.update(this.objects)
        if(updated){
            val index = Storage.getInstance().objects.indexOf(objects)
            Storage.getInstance().objects[index]=objects
        }
        return updated
    }

    suspend fun delete(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        val deleted=objects.delete(this.objects)
        if(deleted){
            Storage.getInstance().objects.remove(objects)
        }
        return deleted
    }
}