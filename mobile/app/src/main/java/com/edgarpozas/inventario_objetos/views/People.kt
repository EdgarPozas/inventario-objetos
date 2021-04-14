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
import com.edgarpozas.inventario_objetos.controllers.PeopleController
import com.edgarpozas.inventario_objetos.controllers.RoomController
import com.edgarpozas.inventario_objetos.models.*
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.models.Room
import com.edgarpozas.inventario_objetos.views.components.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class People(val db:DataBaseSQL) : Fragment(), SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var swipeRefresh:SwipeRefreshLayout?=null
    private var listView:ListView?=null
    private var adapter:PeopleListAdapter? = null;
    private var peopleController = PeopleController(this);

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.people, container, false)

        listView=view.findViewById(R.id.listView)
        swipeRefresh=view.findViewById(R.id.swipeRefresh)

        swipeRefresh?.setOnRefreshListener(this)
        listView?.onItemClickListener = this

        refresh()

        return view
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //selectRoom(requireView(),Storage.getInstance().rooms[position])
    }

    override fun onRefresh() {
        refresh()
    }

    fun refresh(){
        swipeRefresh?.isRefreshing=true
        val people=this
        scope.async {

            peopleController.getAll(db.readableDatabase)

            adapter= PeopleListAdapter(people,Storage.getInstance().users.filter { x->x.active })
            listView?.adapter=adapter
            swipeRefresh?.isRefreshing=false
        }
    }
}