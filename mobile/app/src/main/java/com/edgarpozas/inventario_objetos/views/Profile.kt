package com.edgarpozas.inventario_objetos.views

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.ProfileController
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.views.components.GenericAlertDialog
import com.google.android.material.snackbar.Snackbar

class Profile : Fragment(), View.OnClickListener {

    val profileController:ProfileController= ProfileController(this)
    var userAux: User=User()
    var editFirstName:EditText?=null
    var editLastName:EditText?=null
    var editEmail:EditText?=null
    var editPassword:EditText?=null
    var editConfirmPassword:EditText?=null
    val profileAlertDialog:GenericAlertDialog= GenericAlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.profile, container, false)

        editFirstName=view.findViewById<EditText>(R.id.editFirstName)
        editLastName=view.findViewById<EditText>(R.id.editLastName)
        editEmail=view.findViewById<EditText>(R.id.editEmail)
        editPassword=view.findViewById<EditText>(R.id.editPassword)
        editConfirmPassword=view.findViewById<EditText>(R.id.editConfirmPassword)

        updateFields()

        view.findViewById<Button>(R.id.btnUpdate).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnDelete).setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        if (v?.id==R.id.btnUpdate)
            update(v)
        else if(v?.id==R.id.btnDelete)
            delete(v)
    }

    fun update(view:View){
        userAux=Storage.getInstance().user

        val password=editPassword?.text.toString()
        val confirmPassword= editConfirmPassword?.text.toString()

        if(password!=confirmPassword){
            Snackbar.make(view, R.string.passwords_not_math, Snackbar.LENGTH_SHORT).show()
            return
        }

        Storage.getInstance().user.firstName=editFirstName?.text.toString()
        Storage.getInstance().user.lastName=editLastName?.text.toString()
        Storage.getInstance().user.email=editEmail?.text.toString()
        Storage.getInstance().user.password=editPassword?.text.toString()

        if(Storage.getInstance().user.isAllEmpty()){
            Snackbar.make(view, R.string.fields_empty, Snackbar.LENGTH_SHORT).show()
            Storage.getInstance().user=userAux
            updateFields()
            return
        }
        if(!Storage.getInstance().user.isValidEmail()){
            Snackbar.make(view, R.string.email_format, Snackbar.LENGTH_SHORT).show()
            Storage.getInstance().user=userAux
            updateFields()
            return
        }
        val alertDialog: AlertDialog? = view.context.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.update_user_title)
                setMessage(R.string.update_user_content)
                setPositiveButton(R.string.button_yes, DialogInterface.OnClickListener { dialog, id ->
                    if(profileController.update(Storage.getInstance().user)){
                        Snackbar.make(view, R.string.user_updated, Snackbar.LENGTH_SHORT).show()
                    }else{
                        Snackbar.make(view, R.string.error_update_user, Snackbar.LENGTH_SHORT).show()
                    }
                })
                setNegativeButton(R.string.button_no, DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
            }
            builder.create()
        }
        alertDialog?.show()
    }

    fun delete(view:View){
        profileAlertDialog.createAlertDialog(
            view.context,
            getString(R.string.delete_user_title),
            getString(R.string.delete_user_content),
            { dialog, id ->
                if(profileController.update(Storage.getInstance().user)){
                    Snackbar.make(view, R.string.user_deleted, Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(view, R.string.error_delete_user, Snackbar.LENGTH_SHORT).show()
                }
            },
            { dialog, id ->
                dialog.dismiss()
            })?.show()
    }

    fun updateFields(){
        editFirstName?.setText(Storage.getInstance().user.firstName)
        editLastName?.setText(Storage.getInstance().user.lastName)
        editEmail?.setText(Storage.getInstance().user.email)
        editPassword?.setText(Storage.getInstance().user.password)
        editConfirmPassword?.setText(Storage.getInstance().user.password)
    }

}