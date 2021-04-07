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
    var registerController: RegisterController= RegisterController(this);
    var user: User=User()
    var confirmPassword=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        title= R.string.register_title.toString()
    }

    fun register(view: View){
        user.firstName= findViewById<EditText>(R.id.editFirstName)?.text.toString()
        user.lastName= findViewById<EditText>(R.id.editLastName)?.text.toString()
        user.email= findViewById<EditText>(R.id.editEmail)?.text.toString()
        user.password= findViewById<EditText>(R.id.editPassword)?.text.toString()
        confirmPassword= findViewById<EditText>(R.id.editConfirmPassword)?.text.toString()

        if(user.password!=confirmPassword){
            Snackbar.make(view, R.string.passwords_not_math, Snackbar.LENGTH_SHORT).show()
            return
        }
        if(user.isAllEmpty()){
            Snackbar.make(view, R.string.fields_empty, Snackbar.LENGTH_SHORT).show()
            return
        }
        if(!user.isValidEmail()){
            Snackbar.make(view, R.string.email_format, Snackbar.LENGTH_SHORT).show()
            return
        }

        if(!registerController.register(user)){
            Snackbar.make(view, R.string.error_register, Snackbar.LENGTH_SHORT).show()
            return
        }

        registerController.goToLogin()
    }
}