package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.models.Storage


class Principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal)
        title = "Hola ${Storage.getInstance().user.firstName}"
    }
}