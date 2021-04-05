package com.edgarpozas.inventario_objetos.models

class DataBase {



    private fun DataBase(){
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