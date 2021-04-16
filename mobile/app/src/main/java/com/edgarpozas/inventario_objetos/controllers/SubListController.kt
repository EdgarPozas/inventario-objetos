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
import kotlin.math.abs


class SubListController(val subList: SubList) {

    private var end=HashMap<Int,ArrayList<ArrayList<Objects>>>()

    fun listSub(originalTarget: Int,target:Int,dataSet:ArrayList<Objects>,path:ArrayList<Objects>){
        if(dataSet.size==0){
            val prices=path.map {x->x.price.toInt()}
            val k= abs(originalTarget-prices.sum())

            if(!end.keys.contains(k)){
                end[k] = ArrayList()
            }

            val aux=ArrayList(path)
            aux.sortBy { x->x.price }

            if(!end[k]!!.contains(aux))
                end[k]?.add(aux)
            return
        }
        for (i in 0 until dataSet.size){
            val value=dataSet[i]
            val t= target-value.price.toInt()
            if(t<0)
                continue
            var arr=dataSet.subList(i+1,dataSet.size)
            val arrF=arr.filter { x->x.price<=t }
            path.add(value)
            listSub(originalTarget,t, arrF as ArrayList<Objects>,path)
            path.removeAt(path.size-1)
        }
    }


    fun runSearch(price:Int,objects:MutableList<Objects>): ArrayList<ArrayList<Objects>>? {

        /*var objects=ArrayList<Objects>()

        var values= arrayOf(70,100,20,70,50,120,80,160)
        values.sortDescending()

        for (it in values)
            objects.add(Objects(price = it.toDouble()))*/

        end=HashMap()

        listSub(price,price, objects as ArrayList<Objects>,ArrayList())
        val minK=end.keys.minByOrNull { x->x }

        var vals=ArrayList<ArrayList<Objects>>()

        for(it in end[minK]!!){
            vals.add(it)
        }

        return vals
    }
}
