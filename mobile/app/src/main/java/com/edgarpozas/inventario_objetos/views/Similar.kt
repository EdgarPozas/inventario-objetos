package com.edgarpozas.inventario_objetos.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.SimilarController
import com.edgarpozas.inventario_objetos.controllers.SubListController
import com.edgarpozas.inventario_objetos.models.DataBaseSQL
import com.edgarpozas.inventario_objetos.views.components.SimilarListAdapter
import com.edgarpozas.inventario_objetos.views.components.SubListAlertDialog
import com.edgarpozas.inventario_objetos.views.components.SubListListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class Similar(val db: DataBaseSQL) : Fragment() {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val subListController= SimilarController(this)
    private var listView: ListView?=null
    private var adapter: SimilarListAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.similar, container, false)

        listView=view.findViewById(R.id.listView)


        return view
    }
}