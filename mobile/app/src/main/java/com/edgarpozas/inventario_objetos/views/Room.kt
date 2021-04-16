package com.edgarpozas.inventario_objetos.views

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.RoomController
import com.edgarpozas.inventario_objetos.models.DataBaseSQL
import com.edgarpozas.inventario_objetos.models.Room
import com.edgarpozas.inventario_objetos.models.Storage
import com.edgarpozas.inventario_objetos.utils.Utils
import com.edgarpozas.inventario_objetos.views.components.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class Room(val db: DataBaseSQL) : Fragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var swipeRefresh:SwipeRefreshLayout?=null
    private var listView:ListView?=null
    private val roomAlertDialog:RoomAlertDialog= RoomAlertDialog(this)
    private val roomController:RoomController= RoomController(this)
    private val deleteAlertDialog: GenericAlertDialog = GenericAlertDialog()
    private var adapter:RoomListAdapter? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.room, container, false)

        listView=view.findViewById(R.id.listView)
        swipeRefresh=view.findViewById(R.id.swipeRefresh)

        view.findViewById<FloatingActionButton>(R.id.btnAdd).setOnClickListener(this)
        swipeRefresh?.setOnRefreshListener(this)

        adapter= RoomListAdapter(this,Storage.getInstance().rooms)
        listView?.onItemClickListener = this

        refresh()

        return view
    }

    override fun onClick(v: View?) {
        if(v?.id==R.id.btnAdd)
            createRoom(v)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        roomController.goToIndividual(Storage.getInstance().rooms.filter { x->x.active }[position])
    }

    override fun onRefresh() {
        refresh()
    }

    fun refresh(){
        swipeRefresh?.isRefreshing=true
        val room=this
        scope.async {
            roomController.getAll(db.readableDatabase)
            adapter= RoomListAdapter(room,Storage.getInstance().rooms.filter { x->x.active });
            listView?.adapter=adapter;
            swipeRefresh?.isRefreshing=false
        }
    }

    fun createRoom(view:View){
        if(!Utils.isNetworkAvailable(view.context)){
            Toast.makeText(requireContext(),R.string.error_no_internet,Toast.LENGTH_SHORT).show()
            return
        }

        roomAlertDialog.room=Room()
        val alertDialog=roomAlertDialog.createAlertDialog(
            getString(R.string.add_room_title)
        )

        alertDialog?.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.button_add),DialogInterface.OnClickListener{item,it->
            if(!Utils.isNetworkAvailable(view.context)){
                Toast.makeText(requireContext(),R.string.error_no_internet,Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val room=Room(
                "",
                alertDialog.findViewById<EditText>(R.id.editRoomName).text.toString(),
                alertDialog.findViewById<EditText>(R.id.editDescriptionRoom).text.toString(),
                Storage.getInstance().user.id
            )

            if(room.isAllEmpty()){
                Toast.makeText(requireContext(),R.string.fields_empty,Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            scope.async {
                if (roomController.create(room)) {
                    Toast.makeText(requireContext(), R.string.room_created, Toast.LENGTH_SHORT).show()
                    refresh()
                } else {
                    Toast.makeText(requireContext(), R.string.error_create_room, Toast.LENGTH_SHORT).show()
                }
            }
        })
        alertDialog?.setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.button_cancel),DialogInterface.OnClickListener{item,it->
            item.dismiss()
        })

        alertDialog?.show()
    }

    fun editRoom(view:View,room:Room){
        if(!Utils.isNetworkAvailable(view.context)){
            Toast.makeText(requireContext(),R.string.error_no_internet,Toast.LENGTH_SHORT).show()
            return
        }
        roomAlertDialog.room=room
        val alertDialog=roomAlertDialog.createAlertDialog(
            getString(R.string.update_room_title)
        )

        alertDialog?.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.button_update), DialogInterface.OnClickListener{ item, it->
            if(!Utils.isNetworkAvailable(view.context)){
                Toast.makeText(requireContext(),R.string.error_no_internet,Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            room.name=alertDialog.findViewById<EditText>(R.id.editRoomName).text.toString()
            room.description=alertDialog.findViewById<EditText>(R.id.editDescriptionRoom).text.toString()

            if(room.isAllEmpty()){
                Toast.makeText(requireContext(),R.string.fields_empty, Toast.LENGTH_SHORT).show()
                refresh()
                return@OnClickListener
            }
            scope.async {
                if (roomController.update(room)) {
                    Toast.makeText(requireContext(), R.string.room_updated, Toast.LENGTH_SHORT).show()
                    refresh()
                } else {
                    Toast.makeText(requireContext(), R.string.error_update_room, Toast.LENGTH_SHORT).show()
                }
            }
        })
        alertDialog?.setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.button_cancel), DialogInterface.OnClickListener{ item, it->
            item.dismiss()
        })

        alertDialog?.show()
    }

    fun deleteRoom(view:View,room:Room){
        if(!Utils.isNetworkAvailable(view.context)){
            Toast.makeText(requireContext(),R.string.error_no_internet,Toast.LENGTH_SHORT).show()
            return
        }
        deleteAlertDialog.createAlertDialog(
            view.context,
            getString(R.string.delete_room_title),
            getString(R.string.delete_room_content),
            DialogInterface.OnClickListener{ dialog,it->
                if(!Utils.isNetworkAvailable(view.context)){
                    Toast.makeText(requireContext(),R.string.error_no_internet,Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                scope.async {
                    if(roomController.delete(room)){
                        Toast.makeText(requireContext(),R.string.room_deleted, Toast.LENGTH_SHORT).show()
                        refresh()
                    }else{
                        Toast.makeText(requireContext(),R.string.error_delete_room, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            DialogInterface.OnClickListener{ dialog,it->
                dialog.dismiss()
            }
        ).show()
    }
}