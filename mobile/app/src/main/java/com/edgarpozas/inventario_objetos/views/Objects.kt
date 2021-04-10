package com.edgarpozas.inventario_objetos.views

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.ObjectsController
import com.edgarpozas.inventario_objetos.controllers.RoomController
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.models.Room
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.utils.Utils
import com.edgarpozas.inventario_objetos.views.components.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class Objects : Fragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var swipeRefresh:SwipeRefreshLayout?=null
    private var listView:ListView?=null
    private val objectsController: ObjectsController = ObjectsController(this)
    private val deleteAlertDialog: GenericAlertDialog = GenericAlertDialog()
    private var adapter:ObjectsListAdapter? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.objects, container, false)

        listView=view.findViewById(R.id.listView)
        swipeRefresh=view.findViewById(R.id.swipeRefresh)

        view.findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener(this)
        swipeRefresh?.setOnRefreshListener(this)
        listView?.onItemClickListener = this

        adapter= ObjectsListAdapter(this,Storage.getInstance().objects);

        refresh()

        return view
    }

    override fun onClick(v: View?) {
        if(v?.id==R.id.btnAdd)
            createObjects(v)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //selectRoom(requireView(),Storage.getInstance().rooms[position])
    }

    override fun onRefresh() {
        refresh()
    }

    fun refresh(){
        swipeRefresh?.isRefreshing=true
        val objects=this
        scope.async {

            objectsController.getAll()

            adapter= ObjectsListAdapter(objects,Storage.getInstance().objects);
            listView?.adapter=adapter;
            swipeRefresh?.isRefreshing=false
        }
    }

    fun createObjects(view:View){

        if(!Utils.isNetworkAvailable(view.context)){
            Snackbar.make(view,R.string.error_no_internet,Snackbar.LENGTH_SHORT).show()
            return
        }
        objectsController.goToCreateObject()
    }

    fun editObjects(view:View,objects:Objects){
        if(!Utils.isNetworkAvailable(view.context)){
            Snackbar.make(view,R.string.error_no_internet,Snackbar.LENGTH_SHORT).show()
            return
        }
        objectsController.goToCreateObject(objects)
    }

    fun deleteObjects(view:View,objects:Objects){
        if(!Utils.isNetworkAvailable(view.context)){
            Snackbar.make(view,R.string.error_no_internet,Snackbar.LENGTH_SHORT).show()
            return
        }
        deleteAlertDialog.createAlertDialog(
            view.context,
            getString(R.string.delete_object_title),
            getString(R.string.delete_object_content),
            DialogInterface.OnClickListener{ dialog,it->
                if(!Utils.isNetworkAvailable(view.context)){
                    Snackbar.make(view,R.string.error_no_internet,Snackbar.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                scope.async {
                    if(objectsController.delete(objects)){
                        Snackbar.make(view,R.string.object_deleted, Snackbar.LENGTH_SHORT).show()
                        refresh()
                    }else{
                        Snackbar.make(view,R.string.error_delete_object, Snackbar.LENGTH_SHORT).show()
                    }
                }
            },
            DialogInterface.OnClickListener{ dialog,it->
                dialog.dismiss()
            }
        ).show()
    }
}