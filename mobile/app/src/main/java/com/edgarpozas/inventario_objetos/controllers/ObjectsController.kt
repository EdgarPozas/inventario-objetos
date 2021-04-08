package com.edgarpozas.inventario_objetos.controllers

import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.views.*


class ObjectsController(val objects: Objects) {

    suspend fun getAll() {
        Storage.getInstance().objects.clear()
        val objects= com.edgarpozas.inventario_objetos.models.Objects.getAll(objects.requireContext())
        if(objects!=null){
            for(object_ in objects){
                Storage.getInstance().objects.add(object_)
            }
        }
    }

    suspend fun create(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        return objects.create(this.objects.requireContext())
    }

    suspend fun update(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        return objects.update(this.objects.requireContext())
    }

    suspend fun delete(objects: com.edgarpozas.inventario_objetos.models.Objects):Boolean{
        return objects.delete(this.objects.requireContext())
    }
}