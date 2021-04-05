package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.RegisterController
import com.edgarpozas.inventario_objetos.models.User
import com.google.android.material.snackbar.Snackbar

class Register : AppCompatActivity() {
    var registerController: RegisterController?= RegisterController(this);
    var user: User=User()
    var confirmPassword=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        title= R.string.registerTitle.toString()
    }

    fun register(view: View){
        user.firstName= findViewById<EditText>(R.id.editFirstName)?.text.toString()
        user.lastName= findViewById<EditText>(R.id.editLastName)?.text.toString()
        user.email= findViewById<EditText>(R.id.editEmail)?.text.toString()
        user.password= findViewById<EditText>(R.id.editPassword)?.text.toString()
        confirmPassword= findViewById<EditText>(R.id.editConfirmPassword)?.text.toString()

        if(user.password!=confirmPassword){
            Snackbar.make(view, R.string.passwordsNotMath, Snackbar.LENGTH_SHORT).show()
            return
        }

        val registerCode=registerController?.register(user)
        if(registerCode==-1){
            Snackbar.make(view, R.string.fieldsEmpty, Snackbar.LENGTH_SHORT).show()
            return
        }
        if(registerCode==-2){
            Snackbar.make(view, R.string.emailFormat, Snackbar.LENGTH_SHORT).show()
            return
        }
        registerController?.goToLogin()
    }
}