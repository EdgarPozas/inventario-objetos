package com.edgarpozas.inventario_objetos.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.LoginController
import com.edgarpozas.inventario_objetos.controllers.RecoveryPasswordController
import com.edgarpozas.inventario_objetos.models.User
import com.google.android.material.snackbar.Snackbar

class RecoveryPassword : AppCompatActivity() {
    var recoveryPasswordController: RecoveryPasswordController?= RecoveryPasswordController(this);
    var user: User=User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recovery_password)
    }

    fun recovery(view: View) {
        user.email= findViewById<EditText>(R.id.editEmail)?.text.toString()
        user.password= findViewById<EditText>(R.id.editPassword)?.text.toString()
        val recoveryCode=recoveryPasswordController?.recovery(user)
        if(recoveryCode==-1){
            Snackbar.make(view, R.string.fieldsEmpty, Snackbar.LENGTH_SHORT).show()
            return
        }
        if(recoveryCode==-2){
            Snackbar.make(view, R.string.emailFormat, Snackbar.LENGTH_SHORT).show()
            return
        }
        recoveryPasswordController?.goToLogin()
    }
}