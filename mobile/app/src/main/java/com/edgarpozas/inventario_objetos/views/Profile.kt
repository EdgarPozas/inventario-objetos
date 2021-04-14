package com.edgarpozas.inventario_objetos.views

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.ProfileController
import com.edgarpozas.inventario_objetos.models.DataBaseSQL
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.models.User
import com.edgarpozas.inventario_objetos.utils.Utils
import com.edgarpozas.inventario_objetos.views.components.GenericAlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

class Profile(val db:DataBaseSQL) : Fragment(), View.OnClickListener {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val profileController:ProfileController= ProfileController(this)
    private var userAux: User=User()
    private var editFirstName:EditText?=null
    private var editLastName:EditText?=null
    private var editEmail:EditText?=null
    private var editPassword:EditText?=null
    private var editConfirmPassword:EditText?=null
    private val profileAlertDialog:GenericAlertDialog= GenericAlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.profile, container, false)

        editFirstName=view.findViewById(R.id.editFirstName)
        editLastName=view.findViewById(R.id.editLastName)
        editEmail=view.findViewById(R.id.editEmail)
        editPassword=view.findViewById(R.id.editPassword)
        editConfirmPassword=view.findViewById(R.id.editConfirmPassword)

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

        if(!Utils.isNetworkAvailable(context)){
            Toast.makeText(requireContext(), R.string.error_no_internet, Toast.LENGTH_SHORT).show()
            return
        }

        if(password!=confirmPassword){
            Toast.makeText(requireContext(), R.string.passwords_no_match, Toast.LENGTH_SHORT).show()
            return
        }

        Storage.getInstance().user.firstName=editFirstName?.text.toString()
        Storage.getInstance().user.lastName=editLastName?.text.toString()
        Storage.getInstance().user.email=editEmail?.text.toString()
        Storage.getInstance().user.password=editPassword?.text.toString()

        if(Storage.getInstance().user.isAllEmpty()){
            Toast.makeText(requireContext(), R.string.fields_empty, Toast.LENGTH_SHORT).show()
            Storage.getInstance().user=userAux
            updateFields()
            return
        }
        if(!Storage.getInstance().user.isValidEmail()){
            Toast.makeText(requireContext(), R.string.email_format, Toast.LENGTH_SHORT).show()
            Storage.getInstance().user=userAux
            updateFields()
            return
        }
        val alertDialog: AlertDialog? = view.context.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.update_user_title)
                setMessage(R.string.update_user_content)
                setPositiveButton(R.string.button_yes) { dialog, id ->
                    scope.async {
                        if (profileController.update(Storage.getInstance().user)) {
                            Toast.makeText(requireContext(), R.string.user_updated, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), R.string.error_update_user, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                setNegativeButton(R.string.button_no) { dialog, id ->
                    dialog.dismiss()
                }
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
                scope.async {
                    if(profileController.delete(Storage.getInstance().user)){
                        Toast.makeText(requireContext(), R.string.user_deleted, Toast.LENGTH_SHORT).show()
                        profileController.goToLogin()
                    }else{
                        Toast.makeText(requireContext(), R.string.error_delete_user, Toast.LENGTH_SHORT).show()
                    }
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