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
import com.edgarpozas.inventario_objetos.models.*
import com.edgarpozas.inventario_objetos.models.Objects
import com.edgarpozas.inventario_objetos.utils.*
import com.edgarpozas.inventario_objetos.views.components.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ObjectsEdit : AppCompatActivity(), OnMapReadyCallback {

    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var objectsController= ObjectsEditController(this)
    private var objectAux:Objects?=null
    private var editName:EditText?=null
    private var editDescription:EditText?=null
    private var editFunctionality:EditText?=null
    private var listViewTag:ListView?=null
    private var editPrice:EditText?=null
    private var listViewShared:ListView?=null
    private var spinnerRoom:Spinner?=null
    private var imageView:ImageView?=null

    private var btnAddImage:ImageButton?=null
    private var btnAddSound:ImageButton?=null
    private var btnAdd:Button?=null

    private var containerImage:LinearLayout?=null
    private var containerSound:LinearLayout?=null

    private var btnPlayAudio:ImageButton?=null

    private var adapterListViewTag: ObjectsTagsListAdapter? = null
    private var adapterListViewShared: ObjectsSharedListAdapter? = null
    private var player :MediaPlayer?=null
    private var recorder: MediaRecorder?=null
    private var isRecordingAudio=false

    private val addTagAlertDialog=ObjectsTagsAlertDialog(this)
    private val addSharedAlertDialog= ObjectsSharedAlertDialog(this)
    private val genericAlertDialog=GenericAlertDialog()

    private var tags=ArrayList<String>()
    private var imageLoaded=false
    private var fileAudio:File?=null
    private var tmpTitle:CharSequence=""
    private var location: Location?=null
    var users=ArrayList<User>()

    private var googleMap:GoogleMap?=null

    private val db=DataBaseSQL(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objects_create)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id=intent?.getStringExtra("id")
        objectAux=Storage.getInstance().objects.toList().find { r-> r.id==id }

        editName=findViewById(R.id.editObjectName)
        editDescription=findViewById(R.id.editObjectDescription)
        editFunctionality=findViewById(R.id.editObjectFunctionality)
        listViewTag=findViewById(R.id.listViewTag)
        editPrice=findViewById(R.id.editObjectPrice)
        listViewShared=findViewById(R.id.listViewShared)
        spinnerRoom=findViewById(R.id.spinnerRoom)
        imageView=findViewById(R.id.image)
        containerImage=findViewById(R.id.containerImage)
        containerSound=findViewById(R.id.containerSound)

        btnAddImage=findViewById(R.id.btnAddImage)
        btnAddSound=findViewById(R.id.btnAddSound)
        btnAdd=findViewById(R.id.btnAdd)

        btnPlayAudio=findViewById(R.id.btnPlayAudio)

        val objectsEdit=this
        scope.async {

            if(objectAux!=null)
                Toast.makeText(objectsEdit,R.string.object_downloading,Toast.LENGTH_LONG).show()

            objectsController.getAllUsers(db.readableDatabase)
            objectsController.getAllRooms(db.readableDatabase)
            objectsController.getAllPositions(db.readableDatabase)

            var ls=ArrayList<String>()
            for (room in Storage.getInstance().rooms.filter { x->x.active }){
                ls.add(room.name)
            }

            val adapter=ArrayAdapter(objectsEdit, R.layout.support_simple_spinner_dropdown_item, ls)
            spinnerRoom?.adapter=adapter

            if(objectAux==null) {
                title = getString(R.string.object_create)
            } else {
                title = getString(R.string.object_update)
                editName?.setText(objectAux?.name)
                editDescription?.setText(objectAux?.description)
                editFunctionality?.setText(objectAux?.functionality)
                editPrice?.setText(objectAux?.price!!.toInt().toString())
                val lastPositionId= objectAux?.positions!!.last()
                val lastPosition=Storage.getInstance().positions.find { x->x.id== lastPositionId }
                val roomSelected=Storage.getInstance().rooms.find { x->x.id== lastPosition?.room }
                val index=Storage.getInstance().rooms.filter { x->x.active }.indexOf(roomSelected)
                spinnerRoom?.setSelection(index)
                btnAdd?.text = getString(R.string.object_update)

                for (tag in objectAux!!.tags!!){
                    tags.add(tag)
                }

                for (id in objectAux!!.sharedBy!!){
                    val user=Storage.getInstance().users.filter { x->x.active }.find { x->x.id==id }
                    users.add(user!!)
                }

                if(objectAux?.urlImage!!.isNotEmpty()){
                    val byteArrayImage=objectsController.downloadFile(objectAux?.urlImage!!)
                    val image: Drawable =BitmapDrawable(resources, BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.size))
                    imageView?.setImageDrawable(image)
                    showImage(true)
                }
                if(objectAux?.urlSound!!.isNotEmpty()){
                    val byteArraySound=objectsController.downloadFile(objectAux?.urlSound!!)
                    fileAudio=File(externalCacheDir!!.absolutePath + "/tmpAudio.mp3")
                    fileAudio?.appendBytes(byteArraySound)
                    showAudio(true)
                }

            }

            refreshTags()
            refreshShared()

        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(objectsEdit)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap=googleMap
        val mexicoCamera = LatLng(21.6938605, -104.666589)
        this.googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mexicoCamera, 20f))
        this.googleMap?.uiSettings?.isZoomControlsEnabled=true
        requireLocation()
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

    fun refreshTags(){
        adapterListViewTag=ObjectsTagsListAdapter(this, tags)
        listViewTag?.adapter=adapterListViewTag
    }

    fun addTag(view: View){
        addTagAlertDialog.tag=null
        val alertDialog=addTagAlertDialog.createAlertDialog(
            getString(R.string.add_objects_tags_title)
        )

        alertDialog?.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.button_add),
            DialogInterface.OnClickListener { item, it ->

                val tag = alertDialog.findViewById<EditText>(R.id.editTagName).text.toString()

                if (tag.isEmpty()) {
                    Toast.makeText(this, R.string.fields_empty, Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

                if (tags.find { x -> x.equals(tag) } != null) {
                    Toast.makeText(this, R.string.error_tag_repeated, Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

                tags.add(tag)
                Toast.makeText(this, R.string.object_add_tag, Toast.LENGTH_SHORT).show()
                refreshTags()
            })
        alertDialog?.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.button_cancel),
            DialogInterface.OnClickListener { item, it ->
                item.dismiss()
            })

        alertDialog?.show()
    }

    fun deleteTag(view: View, position: Int){
        genericAlertDialog.createAlertDialog(
            view.context,
            getString(R.string.delete_objects_tags_title),
            getString(R.string.delete_objects_tags_content),
            { dialog, it ->
                tags.removeAt(position)
                Toast.makeText(this, R.string.object_delete_tag, Toast.LENGTH_SHORT).show()
                refreshTags()
            },
            { dialog, it ->
                dialog.dismiss()
            }
        ).show()
    }

    fun goToTakePhoto(v: View){
        if (checkCallingPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
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
                Toast.makeText(this, R.string.error_no_camera_permission, Toast.LENGTH_LONG).show()
            }
        }
        else if (requestCode == PERMISSION_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showRecordAudio()
            } else {
                Toast.makeText(this, R.string.error_no_audio_permission, Toast.LENGTH_LONG).show()
            }
        }
        else if (requestCode == PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation()
            } else {
                Toast.makeText(this, R.string.error_no_location_permission, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun showCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView?.setImageBitmap(imageBitmap)
            showImage(true)
        }
    }

    fun showImage(show:Boolean){
        if(show){
            containerImage?.visibility=View.VISIBLE
            btnAddImage?.visibility=View.GONE
            imageLoaded=true

        }else{
            containerImage?.visibility=View.GONE
            btnAddImage?.visibility=View.VISIBLE
            imageLoaded=false
            imageView?.setImageResource(0)
        }
    }

    fun showAudio(show:Boolean){
        if(show){
            containerSound?.visibility=View.VISIBLE
            btnAddSound?.visibility=View.GONE

        }else{
            containerSound?.visibility=View.GONE
            btnAddSound?.visibility=View.VISIBLE
            fileAudio=null
        }
    }


    fun deletePhoto(v: View){
        showImage(false)
    }

    fun goToRecordAudio(v: View){
        if (checkCallingPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSION_AUDIO
            )
        } else {
            showRecordAudio()
        }
    }

    fun showRecordAudio(){
        if(isRecordingAudio){
            stopRecording()
            return
        }
        genericAlertDialog.createAlertDialog(
            this,
            getString(R.string.object_record_title),
            getString(R.string.object_record_content),
            { dialog, it ->
                startRecording()
            },
            { dialog, it ->
                dialog.dismiss()
            }
        ).show()
    }

    @SuppressLint("NewApi")
    fun startRecording() {
        fileAudio=File(externalCacheDir!!.absolutePath + "/tmpAudio.mp3")

        recorder = MediaRecorder()

        recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder?.setOutputFile(fileAudio)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        recorder?.prepare()


        recorder?.start()
        tmpTitle = title
        title = getString(R.string.object_recording_audio)
        isRecordingAudio=true
        btnAddSound?.setImageResource(R.drawable.ic_baseline_stop_24)
    }

    fun stopRecording(){
        title=tmpTitle
        containerSound?.visibility=View.VISIBLE
        btnAddSound?.visibility=View.GONE
        recorder?.release()
        isRecordingAudio=false
        btnAddSound?.setImageResource(R.drawable.ic_baseline_mic_24)
    }

    fun deleteAudio(v: View){
        showAudio(false)
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

    fun refreshShared(){
        adapterListViewShared=ObjectsSharedListAdapter(this, users.filter { x -> x.active })
        listViewShared?.adapter=adapterListViewShared
    }

    fun addShared(view: View){
        val objectsEdit=this
        scope.async {
            objectsController.getAllUsers(db.readableDatabase)
            val alertDialog=addSharedAlertDialog.createAlertDialog(
                getString(R.string.add_objects_shared_title),
                DialogInterface.OnClickListener { dialog, i ->
                    val user = Storage.getInstance().users.filter { x -> x.active }[i]

                    users.add(user)
                    Toast.makeText(objectsEdit, R.string.object_add_shared, Toast.LENGTH_SHORT)
                        .show()
                    refreshShared()
                }
            )
            alertDialog?.setButton(
                AlertDialog.BUTTON_NEGATIVE, getString(R.string.button_cancel),
                DialogInterface.OnClickListener { item, it ->
                    item.dismiss()
                })

            alertDialog?.show()
        }
    }

    fun deleteShared(view: View, position: Int){
        genericAlertDialog.createAlertDialog(
            view.context,
            getString(R.string.delete_objects_shared_title),
            getString(R.string.delete_objects_shared_content),
            { dialog, it ->
                users.removeAt(position)
                Toast.makeText(this, R.string.object_delete_shared, Toast.LENGTH_SHORT).show()
                refreshShared()
            },
            { dialog, it ->
                dialog.dismiss()
            }
        ).show()
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

    fun createObject(v: View){

        if(!Utils.isNetworkAvailable(this)){
            Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_SHORT).show()
            return
        }

        val objectsTmp=Objects()
        objectsTmp.name= editName?.text.toString()
        objectsTmp.description= editDescription?.text.toString()
        objectsTmp.functionality= editFunctionality?.text.toString()
        objectsTmp.tags= tags
        //objectsTmp.name= editName?.text.toString()
        //objectsTmp.name= editName?.text.toString()
        var price=0.0
        if(!editPrice?.text.toString().isEmpty())
            price=editPrice?.text.toString().toDouble()
        objectsTmp.price= price
        val sharedBy=ArrayList<String>()
        users.forEach { x->sharedBy.add(x.id) }
        objectsTmp.sharedBy= sharedBy
        objectsTmp.createdBy= Storage.getInstance().user.id

        if(Storage.getInstance().rooms.size==0){
            Toast.makeText(this, R.string.error_no_room, Toast.LENGTH_SHORT).show()
            return
        }

        if(location!=null) {
            val position = Position(
                "",
                location!!.latitude,
                location!!.longitude,
                location!!.altitude,
                Storage.getInstance().rooms.filter { x -> x.active }[spinnerRoom!!.selectedItemPosition].id,
                Storage.getInstance().user.id
            )
            objectsTmp.position=position
        }else{
            Toast.makeText(this, R.string.error_no_location, Toast.LENGTH_SHORT).show()
            return
        }

        if(objectsTmp.sharedBy!!.size==0){
            Toast.makeText(this, R.string.error_no_shared, Toast.LENGTH_SHORT).show()
            return
        }

        if(objectsTmp.isAllEmpty()){
            Toast.makeText(this, R.string.fields_empty, Toast.LENGTH_SHORT).show()
            return
        }
        val objectsEdit=this
        scope.async {

            Toast.makeText(objectsEdit,R.string.object_uploading,Toast.LENGTH_LONG).show()

            var uploadData=false
            val formData=io.ktor.client.request.forms.formData {
                if(imageLoaded){
                    val bos = ByteArrayOutputStream()
                    val bitmap = (imageView?.drawable as BitmapDrawable).bitmap
                    bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
                    val bytes: ByteArray = bos.toByteArray()
                    val uuidImage: String = UUID.randomUUID().toString()

                    append("image", "imagen_$uuidImage.png", ContentType.Image.PNG){
                        for (b in bytes)
                            this.writeByte(b)
                    }
                    uploadData=true
                }

                if(fileAudio!=null){
                    val bytes= fileAudio!!.readBytes()
                    val uuidAudio: String = UUID.randomUUID().toString()
                    append("audio", "audio_$uuidAudio.3gp", ContentType.Audio.OGG){
                        for (b in bytes)
                            this.writeByte(b)
                    }
                    uploadData=true
                }

            }

            if(uploadData){
                val json=objectsController.uploadFile(formData)
                val status=json.getInt("status")
                if(status==200){
                    var files=json.getJSONArray("files")
                    for(i in 0 until files.length()){
                        val file=files.getJSONObject(i)
                        if(file.getString("type")=="image"){
                            objectsTmp.urlImage=file.getString("url")
                        }else{
                            objectsTmp.urlSound=file.getString("url")
                        }
                    }
                }else{
                    Toast.makeText(objectsEdit, R.string.error_upload_files, Toast.LENGTH_SHORT).show()
                }
            }

            if(objectAux==null){

                if(objectsController.create(objectsTmp)){
                    Toast.makeText(objectsEdit, R.string.object_created, Toast.LENGTH_SHORT).show()
                    objectsController.goToPrincipal()
                }else{
                    Toast.makeText(objectsEdit, R.string.error_create_object, Toast.LENGTH_SHORT).show()
                }
            }else{
                objectsTmp.id= objectAux!!.id
                if(objectsController.update(objectsTmp)){
                    Toast.makeText(objectsEdit, R.string.object_updated, Toast.LENGTH_SHORT).show()
                    objectsController.goToPrincipal()
                }else{
                    Toast.makeText(objectsEdit, R.string.error_update_object, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}