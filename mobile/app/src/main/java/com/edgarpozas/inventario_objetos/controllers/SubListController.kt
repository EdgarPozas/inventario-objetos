package com.edgarpozas.inventario_objetos.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.models.Position
import com.edgarpozas.inventario_objetos.models.Room
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.utils.ID
import com.edgarpozas.inventario_objetos.utils.USERID
import com.edgarpozas.inventario_objetos.views.*


class SubListController(val subList: SubList) {

    private var end=HashMap<Int,ArrayList<ArrayList<Objects>>>()

    fun listSub(originalTarget: Int,target:Int,dataSet:ArrayList<Objects>,path:ArrayList<Objects>){
        if(dataSet.size==0){
            val prices=path.map {x->x.price.toInt()}
            val k=Math.abs(originalTarget-prices.sum())
            if(!end.keys.contains(k)){
                end.put(k,ArrayList())
            }
            end[k]?.add(path)
            return
        }
        for (i in 0..dataSet.size){
            val value=dataSet[i]
            var arr=dataSet.subList(0,i)
            val arr2=dataSet.subList(i+1,dataSet.size)
            arr.addAll(arr2)
            val t= target-value.price.toInt()
            val arrF=arr.filter { x->x.price<=t }
            path.add(value)
            listSub(originalTarget,t, arrF as ArrayList<Objects>,path)
            path.removeAt(path.size-1)
        }
    }


    suspend fun runSearch(price:Int,objects:MutableList<Objects>): ArrayList<ArrayList<Objects>>? {

        var price=10
        var objects=ArrayList<Objects>()

        var values= arrayOf(70.0,100.0,20.0,50.0,120.0,80.0)

        for (it in values)
            objects.add(Objects(price = it))

        listSub(price,price,objects,ArrayList())

        var vals=ArrayList<ArrayList<Objects>>()

        return vals
    }
}
