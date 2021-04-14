package com.edgarpozas.inventario_objetos.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.edgarpozas.inventario_objetos.R
import com.edgarpozas.inventario_objetos.controllers.ObjectsEditController
import com.edgarpozas.inventario_objetos.controllers.ObjectsIndividualController
import com.edgarpozas.inventario_objetos.models.*
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.utils.*
import com.edgarpozas.inventario_objetos.views.components.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileDescriptor
import java.util.*
import kotlin.collections.ArrayList


class ObjectsIndividual : AppCompatActivity(), OnMapReadyCallback,
    CompoundButton.OnCheckedChangeListener {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var objectsController= ObjectsIndividualController(this)
    private var objectAux:Objects?=null

    private var name:TextView?=null
    private var description:TextView?=null
    private var functionality:TextView?=null
    private var listViewTag:ListView?=null
    private var containerImage:LinearLayout?=null
    private var imageView:ImageView?=null
    private var containerSound:LinearLayout?=null
    private var btnPlayAudio:ImageButton?=null
    private var price:TextView?=null
    private var listViewShared:ListView?=null
    private var googleMap:GoogleMap?=null
    private var room:TextView?=null
    private var checkBox:CheckBox?=null

    private var adapterListViewTag: ObjectsIndividualTagsListAdapter? = null
    private var adapterListViewShared: ObjectsIndividualSharedListAdapter? = null
    private val genericAlertDialog=GenericAlertDialog()

    private var player :MediaPlayer?=null

    private var tags=ArrayList<String>()
    private var imageLoaded=false
    private var fileAudio:File?=null
    private var tmpTitle:CharSequence=""
    private var location: Location?=null
    private var users=ArrayList<User>()
    private var markers=ArrayList<MarkerOptions>()

    private var db=DataBaseSQL(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objects_individual)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id=intent?.getStringExtra("id")
        objectAux=Storage.getInstance().objects.toList().find { r-> r.id==id }

        name=findViewById(R.id.objectName)
        description=findViewById(R.id.objectDescription)
        functionality=findViewById(R.id.objectFunctionality)
        listViewTag=findViewById(R.id.listViewTag)
        containerImage=findViewById(R.id.containerImage)
        imageView=findViewById(R.id.imageView)
        containerSound=findViewById(R.id.containerSound)
        btnPlayAudio=findViewById(R.id.btnPlayAudio)
        price=findViewById(R.id.objectPrice)
        listViewShared=findViewById(R.id.listViewShared)
        room=findViewById(R.id.objectRoom)
        checkBox=findViewById(R.id.checkBoxShowAll)

        checkBox?.setOnCheckedChangeListener(this)

        val objectsIndividual=this
        scope.async {


            Toast.makeText(objectsIndividual,
                if(Utils.isNetworkAvailable(objectsIndividual))R.string.object_downloading else R.string.object_loading,
                Toast.LENGTH_LONG).show()

            objectsController.getAllUsers(db.readableDatabase)
            objectsController.getAllRooms(db.readableDatabase)
            objectsController.getAllPositions(db.readableDatabase)

            title = getString(R.string.object_individual_title)
            name?.text = objectAux?.name
            description?.text = objectAux?.description
            functionality?.text = objectAux?.functionality
            price?.text = objectAux?.price!!.toInt().toString()
            val lastPositionId= objectAux?.positions!!.last()
            val lastPosition=Storage.getInstance().positions.find { x->x.id== lastPositionId }
            val roomSelected=Storage.getInstance().rooms.find { x->x.id== lastPosition?.room }
            println(lastPositionId)
            println(Storage.getInstance().positions)
            println(lastPosition)
            println(Storage.getInstance().rooms)
            println(roomSelected)
            room?.text=roomSelected?.name

            for (tag in objectAux!!.tags!!){
                tags.add(tag)
            }

            for (id in objectAux!!.sharedBy!!){
                val user=Storage.getInstance().users.filter { x->x.active }.find { x->x.id==id }
                users.add(user!!)
            }

            if(objectAux?.urlImage!!.isNotEmpty()&& Utils.isNetworkAvailable(objectsIndividual)){
                val byteArrayImage=objectsController.downloadFile(objectAux?.urlImage!!)
                val image: Drawable =BitmapDrawable(resources, BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.size))
                imageView?.setImageDrawable(image)
                showImage(true)
            }
            if(objectAux?.urlSound!!.isNotEmpty()&& Utils.isNetworkAvailable(objectsIndividual)){
                val byteArraySound=objectsController.downloadFile(objectAux?.urlSound!!)
                fileAudio=File(externalCacheDir!!.absolutePath + "/tmpAudio.3gp")
                fileAudio?.appendBytes(byteArraySound)
                showAudio(true)
            }

            adapterListViewTag=ObjectsIndividualTagsListAdapter(objectsIndividual, tags)
            listViewTag?.adapter=adapterListViewTag

            adapterListViewShared=ObjectsIndividualSharedListAdapter(objectsIndividual, users)
            listViewShared?.adapter=adapterListViewShared

            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
            mapFragment?.getMapAsync(objectsIndividual)

        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap=googleMap
        val mexicoCamera = LatLng(21.6938605, -104.666589)
        this.googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mexicoCamera, 20f))
        this.googleMap?.uiSettings?.isZoomControlsEnabled=true

        updateMap(false)
        requireLocation()
    }

    fun updateMap(showAll:Boolean){
        if(!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this,R.string.error_no_internet,Toast.LENGTH_SHORT).show()
            return
        }
        this.googleMap?.clear()
        var i=1
        for(id in objectAux?.positions!!){
            val position=Storage.getInstance().positions.find { x->x.id==id }
            if(position==null)
                continue
            val latLng=LatLng(position.latitude, position.longitude)
            val marker=MarkerOptions().position(latLng).title(i.toString())
            if(i==objectAux?.positions!!.size-1){
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                marker.visible(true)
                marker.zIndex(i.toFloat())
            }else{
                marker.visible(showAll)
            }
            this.googleMap?.addMarker(marker)
            i++
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    fun showImage(show:Boolean){
        if(show){
            containerImage?.visibility=View.VISIBLE
            imageLoaded=true

        }else{
            containerImage?.visibility=View.GONE
            imageLoaded=false
            imageView?.setImageResource(0)
        }
    }

    fun showAudio(show:Boolean){
        if(show){
            containerSound?.visibility=View.VISIBLE

        }else{
            containerSound?.visibility=View.GONE
            fileAudio=null
        }
    }

    fun playAudio(v: View){
        tmpTitle=title
        title=getString(R.string.object_playing_audio)

        player=MediaPlayer()

        if(player?.isPlaying!!){
            stopAudio(tmpTitle)
            return
        }

        player?.setDataSource(fileAudio?.path)

        player?.setOnCompletionListener(OnCompletionListener {
            stopAudio(tmpTitle)
        })

        btnPlayAudio?.setImageResource(R.drawable.ic_baseline_stop_24)
        player?.prepare()
        player?.start()
    }

    fun stopAudio(originalTitle: CharSequence) {

        btnPlayAudio?.setImageResource(R.drawable.ic_baseline_play_arrow_24)

        title=originalTitle
        player?.stop()
        player?.release()

        player=null
    }

    fun updateMap(v: View){
        requireLocation()
    }

    fun requireLocation(){
        if (checkCallingPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_LOCATION
            )
        } else {
            val locationManager =getSystemService(LOCATION_SERVICE) as LocationManager
            val status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if(!status){
                genericAlertDialog.createAlertDialog(
                    this,
                    getString(R.string.object_location_title),
                    getString(R.string.object_location_content),
                    { dialog, i ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent)
                    },
                    { dialog, i ->
                        Toast.makeText(
                            this,
                            R.string.error_no_location_permission,
                            Toast.LENGTH_LONG
                        ).show()
                        dialog.dismiss()
                    }
                ).show()
            }else{
                enableLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun enableLocation(){
        googleMap?.isMyLocationEnabled = true
        googleMap?.setOnMyLocationChangeListener { location->
            this.location=location
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation()
            } else {
                Toast.makeText(this, R.string.error_no_location_permission, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        updateMap(isChecked)
    }

}