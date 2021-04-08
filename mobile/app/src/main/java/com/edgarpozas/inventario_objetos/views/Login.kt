package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.LoginController
import com.edgarpozas.inventario_objetos.models.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

class Login : AppCompatActivity() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var loginController:LoginController=LoginController(this)
    private var user: User=User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
    }

    fun goToRegister(view: View) {
        loginController.goToRegister()
    }

    fun goToRecovery(view: View) {
        loginController.goToRecovery()
    }

    fun login(view: View) {
        user.email= findViewById<EditText>(R.id.editEmail)?.text.toString()
        user.password= findViewById<EditText>(R.id.editPassword)?.text.toString()

        if(user.email.isEmpty() || user.password.isEmpty()){
            Snackbar.make(view, R.string.fields_empty, Snackbar.LENGTH_SHORT).show()
            return
        }
        if(!user.isValidEmail()){
            Snackbar.make(view, R.string.email_format, Snackbar.LENGTH_SHORT).show()
            return
        }

        scope.async {
            if(!loginController.login(user)){
                Snackbar.make(view, R.string.error_login, Snackbar.LENGTH_SHORT).show()
                return@async
            }

            loginController.goToPrincipal()
        }
    }
}