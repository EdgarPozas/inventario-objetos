package com.edgarpozas.inventario_objetos.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.LoginController
import com.edgarpozas.inventario_objetos.models.User
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {
    var loginController:LoginController=LoginController(this)
    var user: User=User()

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

        if(!loginController.login(user)){
            Snackbar.make(view, R.string.error_login, Snackbar.LENGTH_SHORT).show()
            return
        }

        loginController.goToPrincipal(user)
    }
}