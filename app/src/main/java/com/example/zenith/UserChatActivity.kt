package com.example.zenith

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zenith.OnClickListeners.SelectMassageListener
import com.example.zenith.RelLayouts.CustomMassageAdapter
import com.example.zenith.R.id.addimage
import com.example.zenith.R.id.friendname
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
class UserChatActivity : AppCompatActivity(),
    SelectMassageListener {
    private lateinit var name: TextView
    private lateinit var userID: String
    private lateinit var nameown: String
    private lateinit var imageUri: String
    private lateinit var solid: TextView
    private lateinit var userImage: ImageView
    private lateinit var backImage: ImageButton
    private lateinit var addImage: ImageButton
    private lateinit var sendmassage : ImageButton
    private lateinit var ownmassage : EditText
    private lateinit var close: ImageButton
    private lateinit var delete: ImageButton
    private lateinit var copy: ImageButton
    private var mSoundPool: SoundPool? = null
    private val MASSAGE_KEY = "Massage"
    private  var mfireauth : FirebaseAuth? = null
    private  var mdatabase : DatabaseReference? = null
    private var notidatabase : DatabaseReference? = null
    private lateinit var massages: Massages
    private lateinit var itemMassage: MutableList<ItemMassage>
    private lateinit var recyclermassageView: RecyclerView
    private lateinit var customMassageAdapter: CustomMassageAdapter
    var currentuser: FirebaseUser? = null
    private lateinit var itemMassagecopy: ItemMassage
    private var counter: Int = 0
    private lateinit var vlistener: ValueEventListener
    private lateinit var settingsDatabase: SettingsDatabase
    private lateinit var sqldb: SQLiteDatabase
    private lateinit var yourimage: ImageView
    private lateinit var uploaduri: Uri
    private lateinit var massageUri: Uri
    private lateinit var mstorage: StorageReference
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chat)
        name = findViewById(friendname)
        sendmassage = findViewById(R.id.sendmassage)
        ownmassage = findViewById(R.id.ownmassage)
        userImage = findViewById(R.id.friendimage)
        close = findViewById(R.id.closema)
        delete = findViewById(R.id.deletemass)
        copy = findViewById(R.id.copy)
        backImage = findViewById(R.id.back)
        addImage = findViewById(addimage)
        mstorage = FirebaseStorage.getInstance().getReference("ImageDB")
        mSoundPool = SoundPool(4, AudioManager.STREAM_MUSIC, 100)
        mSoundPool!!.load(this, R.raw.mouse, 1)
        mSoundPool!!.load(this, R.raw.modern, 2)
        solid = findViewById(R.id.solid)
        yourimage = findViewById(R.id.yourimage)
        recyclermassageView = findViewById(R.id.recyclermassage)
        recyclermassageView.layoutManager = LinearLayoutManager(this)
        val itemViewType = 0
        recyclermassageView.recycledViewPool.setMaxRecycledViews(itemViewType, 0)
        mfireauth = FirebaseAuth.getInstance()
        itemMassage = mutableListOf()
        customMassageAdapter = CustomMassageAdapter(
            applicationContext,
            itemMassage,
            this
        )
        recyclermassageView.adapter = customMassageAdapter
        settingsDatabase = SettingsDatabase(this)
        sqldb = settingsDatabase.writableDatabase
        val cursor = sqldb.query(SettingsDatabase.TABLE_SETTINGS, null, null, null, null, null, null)
        if (cursor.moveToFirst()){
            val coloroftheme = cursor.getColumnIndex(SettingsDatabase.COLOROFTHEME)
            window.statusBarColor = cursor.getInt(coloroftheme)
            window.navigationBarColor = cursor.getInt(coloroftheme)
        }
        cursor.close()
        currentuser = mfireauth!!.currentUser!!
        addImage.setOnClickListener {
            playSoundBool(2)
            if (!yourimage.isVisible) {
                ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1920, 1080).start()
            } else{
                yourimage.visibility = View.GONE
                addImage.setImageResource(R.drawable.addimage)
            }
        }
        var friendUID = "45"
        if (intent != null){
            val bitmap = intent.getByteArrayExtra("byteArray")
                ?.let { BitmapFactory.decodeByteArray(intent.getByteArrayExtra("byteArray"), 0, it.size) }
            userImage.setImageBitmap(bitmap)
            friendUID = intent.getStringExtra("UID").toString()
            name.text = intent.getStringExtra("NameUser")
            imageUri = intent.getStringExtra("PNG").toString()
            nameown = intent.getStringExtra("NameOwn").toString()
            userID = intent.getStringExtra("useid").toString()
            mdatabase = FirebaseDatabase.getInstance().getReference(MASSAGE_KEY).child((currentuser!!.uid.hashCode()+friendUID.hashCode()).toString())
            notidatabase = FirebaseDatabase.getInstance().getReference(FirebaseHelper.NOTIFICATION_KEY)
            getData()

            if(name.text.length>12){
                name.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                solid.layoutParams.height = 75*3
                userImage.layoutParams.height = 75*3
                userImage.layoutParams.width = 75*3
            }
        }


        close.setOnClickListener{
            copy.visibility = View.GONE
            close.visibility = View.GONE
            delete.visibility = View.GONE
            solid.visibility = View.GONE
            playSoundBool(1)
            itemMassagecopy.textistrigger = false
            val transparentColor = Color.argb(0, 255, 0, 0)
            itemMassagecopy.setResColor(transparentColor)
        }
        copy.setOnClickListener{
            playSoundBool(1)
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("The text has been copied", itemMassagecopy.getTextMassage())
            clipboard.setPrimaryClip(clip)
        }
        delete.setOnClickListener {
            playSoundBool(1)
            if (itemMassagecopy.ownmassage) {
                mdatabase?.child(itemMassagecopy.key)?.removeValue()
                copy.visibility = View.GONE
                close.visibility = View.GONE
                delete.visibility = View.GONE
                solid.visibility = View.GONE
                itemMassagecopy.textistrigger = false
            } else {
                Toast.makeText(this, "You can`t delete friend`s massage", Toast.LENGTH_SHORT).show()
            }
        }
        sendmassage.setOnClickListener{
            playSoundBool(2)
            if (ownmassage.text.isNotEmpty() || yourimage.visibility == View.VISIBLE) {
                val idofnoti = notidatabase?.key
                val notification = Notification(userID, currentuser?.uid, nameown, imageUri, FirebaseHelper.TYPE_MASSAGE)
                notidatabase?.child(friendUID.hashCode().toString())?.push()?.setValue(notification)
                if (yourimage.visibility == View.VISIBLE) {
                    uploadImage()
                    if (ownmassage.text.toString() != "") {
                        val id = mdatabase?.key
                        massages = Massages(
                            id,
                            ownmassage.text.toString(),
                            currentuser!!.uid,
                            currentuser!!.uid,
                            friendUID,
                            massageUri.toString()
                        )
                        mdatabase?.push()?.setValue(massages)
                        ownmassage.setText("")
                        recyclermassageView.smoothScrollToPosition(counter++)
                        yourimage.visibility = View.GONE
                        addImage.setImageResource(R.drawable.addimage)
                    }else{
                        val id = mdatabase?.key
                        massages = Massages(
                            id,
                            "",
                            currentuser!!.uid,
                            currentuser!!.uid,
                            friendUID,
                            massageUri.toString()
                        )
                        mdatabase?.push()?.setValue(massages)
                        ownmassage.setText("")
                        recyclermassageView.smoothScrollToPosition(counter++)
                        yourimage.visibility = View.GONE
                        addImage.setImageResource(R.drawable.addimage)
                    }
                }
                else {
                    if (ownmassage.text.toString() != "") {
                        val id = mdatabase?.key
                        massages = Massages(
                            id,
                            ownmassage.text.toString(),
                            currentuser!!.uid,
                            currentuser!!.uid,
                            friendUID,
                            "null"
                        )
                        mdatabase?.push()?.setValue(massages)
                        ownmassage.setText("")
                        recyclermassageView.smoothScrollToPosition(counter++)
                    }
                }
            }
        }

        backImage.setOnClickListener {
            mdatabase?.removeEventListener(vlistener)
            mdatabase = null
            mfireauth = null
            currentuser = null
            finish()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        assert(data != null)
        val uri = data?.data
        try {
            if (uri!=null) {
                yourimage.setImageURI(uri)
                yourimage.visibility = View.VISIBLE
                massageUri = uri
                addImage.setImageResource(R.drawable.close)
            }
        }catch (ex: Exception){
            yourimage.visibility = View.GONE
            addImage.setImageResource(R.drawable.addimage)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        mdatabase?.removeEventListener(vlistener)
        mdatabase = null
        mfireauth = null
        currentuser = null
        finish()
    }

    private fun getData() {
        vlistener = object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
               val massagesgd = snapshot.children
                if(itemMassage.size > 0) {
                    itemMassage.clear()
                }
                massagesgd.forEach{ds: DataSnapshot? ->
                    val mass = ds?.getValue(Massages::class.java)


                                if (currentuser?.uid.equals(mass?.ownUID)) {
                                    val transparentColor = Color.argb(0, 255, 0, 0)
                                    val itemMassage2 = mass?.text?.let {
                                        ItemMassage(
                                            it,
                                            true,
                                            ds.key.toString(),
                                            transparentColor,
                                            null,
                                            mass.uri
                                        )
                                    }
                                    itemMassage2?.setOwnMassage(true)
                                    itemMassage.add(itemMassage2!!)
                                } else {
                                    val transparentColor = Color.argb(0, 255, 0, 0)

                                    val itemMassage3 = mass?.text?.let {
                                        ItemMassage(
                                            it, false,
                                            ds.key.toString(), transparentColor, null,
                                            mass.uri
                                        )
                                    }
                                    itemMassage3?.setOwnMassage(false)
                                    itemMassage.add(itemMassage3!!)
                                }

                        //Picasso.get().load(mass?.uri).resize(400, 400).placeholder(R.drawable.profileicon).into(target)
                        //Glide.with(applicationContext).load(mass?.uri).placeholder(R.drawable.profileicon).into(requere)

                }
                customMassageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        mdatabase?.addValueEventListener(vlistener)
        recyclermassageView.layoutManager = LinearLayoutManager(this)
        recyclermassageView.adapter = customMassageAdapter
        for (x in 0..1000000){
            try {
                recyclermassageView.smoothScrollToPosition(x)
                counter++
            }catch (ex: Exception){
                break
            }
        }
    }

    override fun onItemMassageClick(item: ItemMassage?) {
        item?.textistrigger = true
        if (item != null) {
            itemMassagecopy = item
            item.setResColor(R.color.CadetBlue)
        }
        copy.visibility = View.VISIBLE
        close.visibility = View.VISIBLE
        delete.visibility = View.VISIBLE
        solid.visibility = View.VISIBLE
    }
    private fun playSoundBool(mSoundId: Int) {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        val leftVolume = curVolume / maxVolume
        val rightVolume = curVolume / maxVolume
        val priority = 1
        val noLoop = 0
        val normalPlaybackRate = 1f
        mSoundPool!!.play(
            mSoundId,
            leftVolume,
            rightVolume,
            priority,
            noLoop,
            normalPlaybackRate
        )
    }
    private fun uploadImage() {
        val bitmap = (yourimage.drawable as BitmapDrawable).bitmap
        val compressBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
        val baos = ByteArrayOutputStream()
        compressBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val bytes = baos.toByteArray()
        val mref = mstorage.child(System.currentTimeMillis().toString() + "send_image")
        val uptask = mref.putBytes(bytes)
        uptask.continueWithTask { _: Task<UploadTask.TaskSnapshot?>? -> mref.downloadUrl }
            .addOnCompleteListener { task12: Task<Uri> ->
                uploaduri = task12.result
            }
    }

}


