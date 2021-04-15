package com.edgarpozas.inventario_objetos.models

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.edgarpozas.inventario_objetos.utils.Utils
import io.ktor.http.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.jvm.internal.Intrinsics


data class Objects(
        var id: String = "",
        var name: String = "",
        var description: String = "",
        var functionality: String = "",
        var tags: ArrayList<String>? = null,
        var urlImage: String = "",
        var urlSound: String = "",
        var price: Double = 0.0,
        var sharedBy: ArrayList<String>? = null,
        var positions: ArrayList<String>? = null,
        var position: Position? = null,
        var createdBy: String = "",
        var active: Boolean = true
){

    private val dataBase=DataBase.getInstance()

    companion object{
        suspend fun getById(context: Context, id: String, db: SQLiteDatabase): Objects? {
            if(!Utils.isNetworkAvailable(context)){
                val cursorMain=db.rawQuery("select * from objects where _id='${id}'",null)
                val cursorTags=db.rawQuery("select * from objects_tags where _id='${id}'",null)
                val cursorShared=db.rawQuery("select * from objects_shared where _id='${id}'",null)
                val cursorPosition=db.rawQuery("select * from objects_position where _id='${id}'",null)
                return createFromCursors(cursorMain,cursorTags,cursorShared,cursorPosition)
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context, "/api/object/id/$id")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val room: JSONObject =res.getJSONObject("object")
                return createFromJSON(room)
            }
            return null
        }

        suspend fun getAll(context: Context, db: SQLiteDatabase): ArrayList<Objects>? {
            if(!Utils.isNetworkAvailable(context)){
                val cursor=db.rawQuery("select _id from objects",null)
                val arr= ArrayList<Objects>()
                if(!cursor.moveToFirst()){
                    return arr;
                }
                do{
                    val id=cursor.getString(0)
                    val cursorMain=db.rawQuery("select * from objects where _id='${id}'",null)
                    val cursorTags=db.rawQuery("select * from objects_tags where _id='${id}'",null)
                    val cursorShared=db.rawQuery("select * from objects_shared where _id='${id}'",null)
                    val cursorPosition=db.rawQuery("select * from objects_positions where _id='${id}'",null)
                    arr.add(createFromCursors(cursorMain,cursorTags,cursorShared,cursorPosition)!!)
                }while(cursor.moveToNext())
                return arr
            }
            val dataBase=DataBase.getInstance()
            val res=dataBase.getQueryHttp(context, "/api/object")
            val status:Int=res["status"].toString().toInt()
            if(status==200){
                val list =ArrayList<Objects>()
                val objects: JSONArray =res.getJSONArray("objects")
                for (i in 0 until objects.length()){
                    val object_= createFromJSON(objects.getJSONObject(i))
                    list.add(object_)
                }
                return list
            }
            return null
        }

        fun createFromJSON(json: JSONObject):Objects{
            val objects=Objects()
            objects.id=json.getString("_id")
            objects.name=json.getString("name")
            objects.description=json.getString("description")
            objects.functionality=json.getString("functionality")
            val tags_=ArrayList<String>()
            val arrayTags=json.getJSONArray("tags")
            for (i in 0 until arrayTags.length()){
                tags_.add(arrayTags.getString(i))
            }
            objects.tags=tags_
            objects.urlImage=json.getString("urlImage")
            objects.urlSound=json.getString("urlSound")
            objects.price=json.getDouble("price")
            val sharedBy_=ArrayList<String>()
            val arraySharedBy=json.getJSONArray("sharedBy")
            for (i in 0 until arraySharedBy.length()){
                sharedBy_.add(arraySharedBy.getString(i))
            }
            objects.sharedBy=sharedBy_
            val positions_=ArrayList<String>()
            val arrayPositions=json.getJSONArray("positions")
            for (i in 0 until arrayPositions.length()){
                positions_.add(arrayPositions.getString(i))
            }
            objects.positions=positions_
            objects.createdBy=json.getString("createdBy")
            objects.active=json.getBoolean("active")
            return objects
        }

        fun resetSQL(db: SQLiteDatabase): Boolean {
            db.execSQL("delete from objects")
            db.execSQL("delete from objects_tags")
            db.execSQL("delete from objects_shared")
            db.execSQL("delete from objects_positions")
            return true
        }

        fun createFromCursors(cursorMain: Cursor, cursorTags: Cursor, cursorShared: Cursor, cursorPosition: Cursor): Objects? {
            if (!cursorMain.moveToFirst()) {
                return null
            }
            val objects_ = Objects()
            do {
                objects_.id=cursorMain.getString(0)
                objects_.name=cursorMain.getString(1)
                objects_.description=cursorMain.getString(2)
                objects_.functionality=cursorMain.getString(3)
                objects_.urlImage=cursorMain.getString(4)
                objects_.urlSound=cursorMain.getString(5)
                objects_.price=cursorMain.getInt(6).toDouble()
                objects_.active=cursorMain.getInt(7)==1
                objects_.createdBy=cursorMain.getString(8)
            } while (cursorMain.moveToNext())
            if (cursorTags.moveToFirst()) {
                objects_.tags=ArrayList()
                do {
                    objects_.tags!!.add(cursorTags.getString(1))
                } while (cursorTags.moveToNext())
            }
            if (cursorShared.moveToFirst()) {
                objects_.sharedBy=ArrayList()
                do {
                    objects_.sharedBy!!.add(cursorShared.getString(1))
                } while (cursorShared.moveToNext())
            }
            if (cursorPosition.moveToFirst()) {
                objects_.positions=ArrayList()
                do {
                    objects_.positions!!.add(cursorPosition.getString(1))
                } while (cursorPosition.moveToNext())
            }
            return objects_
        }
    }

    fun createSQL(db: SQLiteDatabase): Boolean {
        db.execSQL("insert into objects(_id,name,description,functionality,urlImage,urlSound,price,createdBy,active)" +
                " values('${this.id}','${this.name}','${this.description}','${this.functionality}','${this.urlImage}','${this.urlSound}',${this.price},'${this.createdBy}',${(if (this.active) 1 else 0)})")
        if(tags!=null){
            for (tag in tags!!){
                db.execSQL("insert into objects_tags(_id,tag)" + " values('${this.id}','${tag}')")
            }
        }
        if(sharedBy!=null) {
            for (shared in sharedBy!!){
                db.execSQL("insert into objects_shared(_id,_id_user)" + " values('${this.id}','${shared}')")
            }
        }
        if(positions!=null) {
            for (position in positions!!){
                db.execSQL("insert into objects_positions(_id,_id_position)" + " values('${this.id}','${position}')")
            }
        }
        return true
    }

    suspend fun create(context: Context): Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context, "/api/object", HttpMethod.Post, toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun update(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context, "/api/object/$id", HttpMethod.Put, toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    suspend fun delete(context: Context):Boolean{
        if(!Utils.isNetworkAvailable(context)){
            return false
        }
        val res=dataBase.sendQueryHttp(context, "/api/object/$id", HttpMethod.Delete, toJSON())
        val status:Int=res["status"].toString().toInt()
        return status==200
    }

    fun isAllEmpty():Boolean{
        return name.isEmpty() || description.isEmpty() || functionality.isEmpty()
    }

    fun reset(){
        id=""
        name=""
        description=""
        functionality=""
        tags=null
        urlImage=""
        urlSound=""
        price=0.0
        sharedBy=null
        positions=null
        createdBy=""
    }

    fun toJSON(): JSONObject {
        var json= JSONObject()
        json.put("name", name)
        json.put("description", description)
        json.put("functionality", functionality)
        var tagsArray=JSONArray()
        tags?.forEach { x-> tagsArray.put(x) }
        json.put("tags", tagsArray)
        json.put("urlImage", urlImage)
        json.put("urlSound", urlSound)
        json.put("price", price)
        val listSharedBy=JSONArray()
        sharedBy?.forEach { x-> listSharedBy.put(x) }
        json.put("sharedBy", listSharedBy)
        json.put("position", position?.toJSON())
        json.put("createdBy", createdBy)
        return json
    }
}
