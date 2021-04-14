package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.SplashController
import com.edgarpozas.inventario_objetos.models.DataBaseSQL
import com.edgarpozas.inventario_objetos.utils.Utils
import kotlinx.coroutines.*


class Splash : AppCompatActivity() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val splashController:SplashController= SplashController(this)
    private val db = DataBaseSQL(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splash=this
        scope.async{

            if(Utils.isNetworkAvailable(splash)){

                Toast.makeText(splash, R.string.sincronizing,Toast.LENGTH_SHORT).show()

                splashController.getAll(db.readableDatabase)
                splashController.sync(db.writableDatabase)

                Toast.makeText(splash, R.string.sincronizing_completed,Toast.LENGTH_SHORT).show()
                delay(500)
            }

            splashController.goToNext(db.readableDatabase)
            finish()
        }
    }
}