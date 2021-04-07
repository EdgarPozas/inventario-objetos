package com.edgarpozas.inventario_objetos.models

class DataBase {

    private fun DataBase(){
    }

    fun QuerySQL(){

    }

    fun QueryNoSQL(){

    }

    fun getQuerySQL(){

    }

    fun getQueryNoSQL(){

    }

    companion object{
        var dataBase:DataBase?=null

        fun getInstance() : DataBase{
            if(dataBase==null){
                this.dataBase= DataBase()
            }
            return this.dataBase!!
        }
    }
}