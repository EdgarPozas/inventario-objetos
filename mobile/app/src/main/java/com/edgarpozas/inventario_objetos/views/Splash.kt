package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.SplashController
import kotlinx.coroutines.*


class Splash : AppCompatActivity() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val splashController:SplashController= SplashController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.async{
            delay(1000)
            splashController.goToNext()
            finish()
        }
    }
}