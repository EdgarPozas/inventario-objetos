package com.edgarpozas.inventario_objetos.views

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.SubListController
import com.edgarpozas.inventario_objetos.models.DataBaseSQL
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.views.components.RoomListAdapter
import com.edgarpozas.inventario_objetos.views.components.SubListAlertDialog
import com.edgarpozas.inventario_objetos.views.components.SubListListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

class SubList(val db: DataBaseSQL) : Fragment(), View.OnClickListener {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val subListController=SubListController(this)
    private val alertSubList=SubListAlertDialog(this)
    private var listView: ListView?=null
    private var adapter: SubListListAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.sub_list, container, false)

        val btnSearch:FloatingActionButton=view.findViewById(R.id.btnSearch)
        btnSearch.setOnClickListener(this)

        listView=view.findViewById(R.id.listView)

        return view
    }

    override fun onClick(v: View?) {
        val alertDialog=alertSubList.createAlertDialog(
            getString(R.string.sublist_title)
        )

        val subList=this
        alertDialog?.setButton(
                AlertDialog.BUTTON_POSITIVE, getString(R.string.sublist_search),
                DialogInterface.OnClickListener { item, it ->
                    Toast.makeText(requireContext(),R.string.sublist_search_started,Toast.LENGTH_SHORT).show()
                    val txtPrice=alertDialog.findViewById<EditText>(R.id.editPrice).text.toString()
                    var price=0
                    if(txtPrice.isNotEmpty())
                        price=txtPrice.toInt()
                    val items=subListController.runSearch(price,Storage.getInstance().objects)
                    if (items != null) {
                        createItems(items)
                        Toast.makeText(requireContext(),R.string.sublist_search_completed,Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),R.string.sublist_search_completed_not_found,Toast.LENGTH_SHORT).show()
                    }
                })
        alertDialog?.setButton(
                AlertDialog.BUTTON_NEGATIVE, getString(R.string.button_cancel),
                DialogInterface.OnClickListener { item, it ->
                    item.dismiss()
                })

        alertDialog?.show()
    }

    fun createItems(items:ArrayList<ArrayList<Objects>>){
        val arr=ArrayList<String>()
        var i=1
        for (it in items){
            var cad="$i;"
            for (it2 in it){
                cad+="${it2.name} -> ${it2.price}\n"
            }
            arr.add(cad)
        }

        adapter= SubListListAdapter(this, arr)
        listView?.adapter=adapter
    }
}