package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.PrincipalController
import com.edgarpozas.inventario_objetos.views.components.GenericAlertDialog
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class Principal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var toolbar:Toolbar?=null
    private var drawer:DrawerLayout?=null
    private var toggle:ActionBarDrawerToggle?=null
    private var navigationView:NavigationView?=null
    private val principalController:PrincipalController= PrincipalController(this)
    private val closeSessionAlertDialog:GenericAlertDialog= GenericAlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.principal)

        toolbar = findViewById(R.id.toolbar)
        drawer=findViewById(R.id.drawer)
        navigationView=findViewById(R.id.navigation)

        supportFragmentManager.beginTransaction().add(R.id.content, Objects()).commit()
        toolbar?.title=getString(R.string.menu_objects)

        toggle=ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        drawer?.addDrawerListener(toggle!!)
        toggle?.syncState()


        navigationView?.setNavigationItemSelectedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val ft=supportFragmentManager.beginTransaction()
        when (item.itemId) {
            R.id.navigation_profile -> {
                ft.replace(R.id.content, Profile()).commit()
            }
            R.id.navigation_people -> {
                ft.replace(R.id.content, People()).commit()
            }
            R.id.navigation_objects -> {
                ft.replace(R.id.content, Objects()).commit()
            }
            R.id.navigation_room -> {
                ft.replace(R.id.content, Room()).commit()
            }
            R.id.navigation_list -> {
                ft.replace(R.id.content, SubList()).commit()
            }
            R.id.navigation_similar -> {
                ft.replace(R.id.content, Similar()).commit()
            }
            R.id.navigation_close_session -> {
                closeSessionAlertDialog.createAlertDialog(
                    this,
                    getString(R.string.close_session_title),
                    getString(R.string.close_session_message),
                    { dialog,it->principalController.closeSession()},
                    { dialog,it-> dialog.dismiss()}
                ).show()
                return true
            }
        }

        toolbar?.title=item.title
        drawer?.closeDrawers()

        return true
    }

}