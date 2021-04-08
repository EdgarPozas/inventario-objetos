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
import com.edgarpozas.inventario_objetos.views.components.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class Objects : Fragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var swipeRefresh:SwipeRefreshLayout?=null
    private var listView:ListView?=null
    private val objectsAlertDialog: ObjectsAlertDialog = ObjectsAlertDialog(this)
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
        scope.async {
            listView?.adapter=adapter;
            swipeRefresh?.isRefreshing=false
        }
    }

    fun createObjects(view:View){
        objectsAlertDialog.objects= Objects()
        val alertDialog=objectsAlertDialog.createAlertDialog(
            getString(R.string.add_room_title)
        )

        alertDialog?.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.button_add),DialogInterface.OnClickListener{item,it->
            val objects:Objects=Objects(
                "",
                alertDialog.findViewById<EditText>(R.id.editRoomName).text.toString(),
                alertDialog.findViewById<EditText>(R.id.editDescriptionRoom).text.toString(),
                Storage.getInstance().user.id
            )

            if(objects.isAllEmpty()){
                Snackbar.make(view,R.string.fields_empty,Snackbar.LENGTH_SHORT).show()
                return@OnClickListener
            }
            scope.async {
                if (objectsController.create(objects)) {
                    Snackbar.make(view, R.string.room_created, Snackbar.LENGTH_SHORT).show()
                    refresh()
                } else {
                    Snackbar.make(view, R.string.error_create_room, Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        alertDialog?.setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.button_cancel),DialogInterface.OnClickListener{item,it->
            item.dismiss()
        })

        alertDialog?.show()
    }

    fun editObjects(view:View,objects:Objects){
        objectsAlertDialog.objects=objects
        val alertDialog=objectsAlertDialog.createAlertDialog(
            getString(R.string.update_room_title)
        )

        alertDialog?.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.button_update), DialogInterface.OnClickListener{ item, it->

            objects.name=alertDialog.findViewById<EditText>(R.id.editRoomName).text.toString()
            objects.description=alertDialog.findViewById<EditText>(R.id.editDescriptionRoom).text.toString()

            if(objects.isAllEmpty()){
                Snackbar.make(view,R.string.fields_empty, Snackbar.LENGTH_SHORT).show()
                return@OnClickListener
            }
            scope.async {
                if (objectsController.update(objects)) {
                    Snackbar.make(view, R.string.room_updated, Snackbar.LENGTH_SHORT).show()
                    refresh()
                } else {
                    Snackbar.make(view, R.string.error_update_room, Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        alertDialog?.setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.button_cancel), DialogInterface.OnClickListener{ item, it->
            item.dismiss()
        })

        alertDialog?.show()
    }

    fun deleteObjects(view:View,objects:Objects){
        deleteAlertDialog.createAlertDialog(
            view.context,
            getString(R.string.delete_room_title),
            getString(R.string.delete_room_content),
            { dialog,it->
                scope.async {
                    if(objectsController.delete(objects)){
                        Snackbar.make(view,R.string.room_deleted, Snackbar.LENGTH_SHORT).show()
                        refresh()
                    }else{
                        Snackbar.make(view,R.string.error_delete_room, Snackbar.LENGTH_SHORT).show()
                    }
                }
            },
            { dialog,it->
                dialog.dismiss()
            }
        ).show()
    }
}