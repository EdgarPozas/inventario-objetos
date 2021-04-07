package com.edgarpozas.inventario_objetos.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.LoginController
import com.edgarpozas.inventario_objetos.controllers.RecoveryPasswordController
import com.edgarpozas.inventario_objetos.models.User
import com.google.android.material.snackbar.Snackbar

class RecoveryPassword : AppCompatActivity() {
    private var recoveryPasswordController: RecoveryPasswordController= RecoveryPasswordController(this);
    private var user: User=User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recovery_password)
        title=getString(R.string.recovery_title)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    fun recovery(view: View) {
        user.email= findViewById<EditText>(R.id.editEmail)?.text.toString()
        user.password= findViewById<EditText>(R.id.editPassword)?.text.toString()

        if(user.email.isEmpty() || user.password.isEmpty()){
            Snackbar.make(view, R.string.fields_empty, Snackbar.LENGTH_SHORT).show()
            return
        }
        if(user.isValidEmail()){
            Snackbar.make(view, R.string.email_format, Snackbar.LENGTH_SHORT).show()
            return
        }

        if(!recoveryPasswordController.recovery(user)){
            Snackbar.make(view, R.string.error_recovery, Snackbar.LENGTH_SHORT).show()
            return
        }

        recoveryPasswordController.goToLogin()
    }
}