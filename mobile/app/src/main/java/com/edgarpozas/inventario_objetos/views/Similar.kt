package com.edgarpozas.inventario_objetos.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.checkCallingPermission
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.SimilarController
import com.edgarpozas.inventario_objetos.controllers.SubListController
import com.edgarpozas.inventario_objetos.models.DataBaseSQL
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.utils.*
import com.edgarpozas.inventario_objetos.views.components.SimilarListAdapter
import com.edgarpozas.inventario_objetos.views.components.SubListAlertDialog
import com.edgarpozas.inventario_objetos.views.components.SubListListAdapter
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import java.io.ByteArrayOutputStream
import java.util.*

class Similar(val db: DataBaseSQL) : Fragment(), View.OnClickListener {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val similarController= SimilarController(this)
    private var listView: ListView?=null
    private var adapter: SimilarListAdapter? = null;

    private var imageView:ImageView?=null
    private var imageButton:ImageButton?=null

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
        imageView=view.findViewById(R.id.imageView)
        imageButton=view.findViewById(R.id.btnTakePhoto)
        imageButton?.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        goToTakePhoto(v!!)
    }

    fun goToTakePhoto(v: View){
        if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_CAMERA
            )
        } else {
            showCamera()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String?>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCamera()
            } else {
                Toast.makeText(requireContext(), R.string.error_no_camera_permission, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun showCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView?.setImageBitmap(imageBitmap)
            sendImage()
        }
    }

    fun sendImage(){
        if(!Utils.isNetworkAvailable(requireContext())){
            Toast.makeText(requireContext(),R.string.error_no_internet,Toast.LENGTH_SHORT).show()
            return
        }

        val similar=this
        scope.async {
            Toast.makeText(similar.requireContext(),R.string.similar_search_started,Toast.LENGTH_SHORT).show()

            val formData=io.ktor.client.request.forms.formData {
                val bos = ByteArrayOutputStream()
                val bitmap = (imageView?.drawable as BitmapDrawable).bitmap
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
                val bytes: ByteArray = bos.toByteArray()
                val uuidImage: String = UUID.randomUUID().toString()

                append("image", "imagen_$uuidImage.png", ContentType.Image.PNG){
                    for (b in bytes)
                        this.writeByte(b)
                }
            }

            val data=similarController.searchSimilar(formData)
            if(data==null){
                Toast.makeText(similar.requireContext(),R.string.similar_search_not_found,Toast.LENGTH_SHORT).show()
            }else{
                refreshList(data)
                Toast.makeText(similar.requireContext(),R.string.similar_search_completed,Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun refreshList(arrayList: ArrayList<Objects>){

    }
}