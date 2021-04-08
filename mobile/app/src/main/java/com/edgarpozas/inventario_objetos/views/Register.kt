package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.RegisterController
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.utils.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

class Register : AppCompatActivity() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var registerController: RegisterController= RegisterController(this);
    private var user: User=User()
    private var confirmPassword=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        title= getString(R.string.register_title)

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

    fun register(view: View){
        user.firstName= findViewById<EditText>(R.id.editFirstName)?.text.toString()
        user.lastName= findViewById<EditText>(R.id.editLastName)?.text.toString()
        user.email= findViewById<EditText>(R.id.editEmail)?.text.toString()
        user.password= findViewById<EditText>(R.id.editPassword)?.text.toString()
        confirmPassword= findViewById<EditText>(R.id.editConfirmPassword)?.text.toString()

        if(!Utils.isNetworkAvailable(this)){
            Snackbar.make(view, R.string.error_no_internet, Snackbar.LENGTH_SHORT).show()
            return
        }
        if(user.password!=confirmPassword){
            Snackbar.make(view, R.string.passwords_no_match, Snackbar.LENGTH_SHORT).show()
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

        scope.async {
            if(!registerController.register(user)){
                Snackbar.make(view, R.string.error_register, Snackbar.LENGTH_SHORT).show()
                return@async
            }
            registerController.goToLogin()
        }
    }
}