package com.edgarpozas.inventario_objetos.models

import org.json.JSONObject

data class Position(
    var latitude: Double =0.0,
    var longitude:Double=0.0,
    var altitude:Double=0.0,
    var room:String="",
    var updatedBy:String="",
    var date:Long=0
    ){

    companion object{
        fun createFromJSON(json:JSONObject):Position {
            val position = Position()
            position.latitude=json.getDouble("latitude")
            position.longitude=json.getDouble("longitude")
            position.altitude=json.getDouble("altitude")
            position.room=json.getString("room")
            position.updatedBy=json.getString("updatedBy")
            position.date=json.getLong("date")
            return position
        }
    }

    fun toJSON(): JSONObject {
        var json= JSONObject()
        json.put("latitude",latitude)
        json.put("longitude",longitude)
        json.put("altitude",altitude)
        json.put("room",room)
        json.put("updatedBy",updatedBy)
        json.put("date",date)
        return json
    }
}
