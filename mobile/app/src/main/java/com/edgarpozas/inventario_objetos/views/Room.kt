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
import android.widget.ListView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.RoomController
import com.edgarpozas.inventario_objetos.models.Room
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.views.components.GenericAlertDialog
import com.edgarpozas.inventario_objetos.views.components.RoomAlertDialog
import com.edgarpozas.inventario_objetos.views.components.RoomListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class Room : Fragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {

    var swipeRefresh:SwipeRefreshLayout?=null
    var listView:ListView?=null
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    val roomAlertDialog:RoomAlertDialog= RoomAlertDialog(this)
    val roomController:RoomController= RoomController(this)
    val deleteAlertDialog: GenericAlertDialog = GenericAlertDialog()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.room, container, false)

        listView=view.findViewById<ListView>(R.id.roomsListView)
        swipeRefresh=view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshRoom)

        view.findViewById<FloatingActionButton>(R.id.btnAddRoom).setOnClickListener(this)
        swipeRefresh?.setOnRefreshListener(this)
        listView?.onItemClickListener = this

        refresh()

        return view
    }

    override fun onClick(v: View?) {
        if(v?.id==R.id.btnAddRoom)
            createRoom(v)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (view != null) {
            selectRoom(view,Storage.getInstance().rooms[position])
        }
    }

    override fun onRefresh() {
        refresh()
    }

    fun refresh(){
        swipeRefresh?.isRefreshing=true
        scope.async {
            val adapter:RoomListAdapter = RoomListAdapter(activity?.baseContext!!, R.layout.room_list_item, Storage.getInstance().rooms);
            listView?.adapter=adapter;
            swipeRefresh?.isRefreshing=false
        }
    }

    fun createRoom(view:View){
        val alertDialog=roomAlertDialog.createAlertDialog(
            getString(R.string.add_room_title)
        )

        alertDialog?.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.button_add),DialogInterface.OnClickListener{item,it->
            val room:Room=Room(
                alertDialog.findViewById<EditText>(R.id.editRoomName).text.toString(),
                alertDialog.findViewById<EditText>(R.id.editDescriptionRoom).text.toString(),
                Storage.getInstance().user.id
            )

            if(room.isAllEmpty()){
                Snackbar.make(view,R.string.fields_empty,Snackbar.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (roomController.create(room)) {
                Snackbar.make(view, R.string.room_created, Snackbar.LENGTH_SHORT).show()
                refresh()
            } else {
                Snackbar.make(view, R.string.error_create_room, Snackbar.LENGTH_SHORT).show()
            }
        })
        alertDialog?.setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.button_cancel),DialogInterface.OnClickListener{item,it->
            item.dismiss()
        })

        alertDialog?.show()
    }

    fun selectRoom(view:View,room:Room){
        roomAlertDialog.room=room

        val alertDialog=roomAlertDialog.createAlertDialog(
            getString(R.string.add_room_title)
        )

        alertDialog?.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.button_update),DialogInterface.OnClickListener{item,it->
            val room:Room=Room(
                alertDialog.findViewById<EditText>(R.id.editRoomName).text.toString(),
                alertDialog.findViewById<EditText>(R.id.editDescriptionRoom).text.toString()
            )
            if(room.isAllEmpty()){
                Snackbar.make(view,R.string.fields_empty,Snackbar.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (roomController.update(room)) {
                Snackbar.make(view, R.string.room_updated, Snackbar.LENGTH_SHORT).show()
                refresh()
            } else {
                Snackbar.make(view, R.string.error_update_room, Snackbar.LENGTH_SHORT).show()
            }
        })
        alertDialog?.setButton(AlertDialog.BUTTON_NEUTRAL,getString(R.string.button_delete),DialogInterface.OnClickListener{item,it->
            deleteAlertDialog.createAlertDialog(
                view.context,
                getString(R.string.delete_room_title),
                getString(R.string.delete_room_content),
                { dialog,it->
                    if(roomController.delete(room)){
                        Snackbar.make(view,R.string.room_deleted,Snackbar.LENGTH_SHORT).show()
                        refresh()
                    }else{
                        Snackbar.make(view,R.string.error_update_room,Snackbar.LENGTH_SHORT).show()
                    }
                },
                { dialog,it->
                    dialog.dismiss()
                }
            ).show()

        })
        alertDialog?.setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.button_cancel),DialogInterface.OnClickListener{item,it->
            item.dismiss()
        })

        alertDialog?.show()
    }
}