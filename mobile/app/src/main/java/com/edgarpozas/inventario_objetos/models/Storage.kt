package com.edgarpozas.inventario_objetos.models

class Storage {

    var user:User=User()
    var rooms:MutableList<Room> = ArrayList()
    var objects:MutableList<Objects> = ArrayList()


    private fun Storage(){
    }

    companion object{
        var storage:Storage?=null

        fun getInstance() : Storage{
            if(storage==null){
                this.storage= Storage()
            }
            return this.storage!!
        }
    }
}