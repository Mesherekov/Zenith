package com.example.zenith

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zenith.R.id.friendname
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Suppress("DEPRECATION")
class UserChatActivity : AppCompatActivity(), SelectMassageListener {
    private lateinit var sendmp3: MediaPlayer
    private lateinit var name: TextView
    private lateinit var solid: TextView
    private lateinit var userImage: ImageView
    private lateinit var backImagee: ImageButton
    private lateinit var sendmassage : ImageButton
    private lateinit var ownmassage : EditText
    private lateinit var close: ImageButton
    private lateinit var delete: ImageButton
    private lateinit var copy: ImageButton
    private val MASSAGE_KEY = "Massage"
    private  var mfireauth : FirebaseAuth? = null
    private  var mdatabase : DatabaseReference? = null
    private lateinit var massages: Massages
    private lateinit var itemMassage: MutableList<ItemMassage>
    private lateinit var recyclermassageView: RecyclerView
    private lateinit var customMassageAdapter: CustomMassageAdapter
    var currentuser: FirebaseUser? = null
    private lateinit var itemMassagecopy: ItemMassage
    private var counter: Int = 0
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var vlistener: ValueEventListener
    private lateinit var settingsDatabase: SettingsDatabase
    private lateinit var sqldb: SQLiteDatabase
    @SuppressLint("MissingInflatedId")
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
        backImagee = findViewById(R.id.back)
        sendmp3 = MediaPlayer.create(this, R.raw.modern)
        mediaPlayer = MediaPlayer.create(this, R.raw.mouse)
        solid = findViewById(R.id.solid)
        recyclermassageView = findViewById(R.id.recyclermassage)
        recyclermassageView.layoutManager = LinearLayoutManager(this)
        val itemViewType = 0
        recyclermassageView.recycledViewPool.setMaxRecycledViews(itemViewType, 0)
        mfireauth = FirebaseAuth.getInstance()
        itemMassage = mutableListOf()
        customMassageAdapter = CustomMassageAdapter(applicationContext, itemMassage, this)
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

        var friendUID = "45"
        if (intent != null){
            val bitmap = intent.getByteArrayExtra("byteArray")
                ?.let { BitmapFactory.decodeByteArray(intent.getByteArrayExtra("byteArray"), 0, it.size) }
            userImage.setImageBitmap(bitmap)
            friendUID = intent.getStringExtra("UID").toString()
            name.text = intent.getStringExtra("NameUser")
            mdatabase = FirebaseDatabase.getInstance().getReference(MASSAGE_KEY).child((currentuser!!.uid.hashCode()+friendUID.hashCode()).toString())
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
            mediaPlayer.start()
            itemMassagecopy.textistrigger = false
            val transparentColor = Color.argb(0, 255, 0, 0)
            itemMassagecopy.setResColor(transparentColor)
        }
        copy.setOnClickListener{
            mediaPlayer.start()
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("The text has been copied", itemMassagecopy.getTextMassage())
            clipboard.setPrimaryClip(clip)
        }
        delete.setOnClickListener {
            mediaPlayer.start()
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
            sendmp3.start()
            if (ownmassage.text.toString() != "") {
                val id = mdatabase?.key
                massages = Massages(
                    id,
                    ownmassage.text.toString(),
                    currentuser!!.uid,
                    currentuser!!.uid,
                    friendUID
                )
                mdatabase?.push()?.setValue(massages)
                ownmassage.setText("")
                recyclermassageView.smoothScrollToPosition(counter++)
            }
        }

        backImagee.setOnClickListener {
            mdatabase?.removeEventListener(vlistener)
            mdatabase = null
            mfireauth = null
            currentuser = null
            finish()
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

    fun getData() {
        vlistener = object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
               val massagesgd = snapshot.children
                if(itemMassage.size > 0) {
                    itemMassage.clear()
                }
                massagesgd.forEach{ds: DataSnapshot? ->
                    val mass = ds?.getValue(Massages::class.java)
                        if (currentuser?.uid.equals(mass?.ownUID)){
                            val transparentColor = Color.argb(0, 255, 0, 0)
                            val itemMassage2 = mass?.text?.let { ItemMassage(it, true, ds.key.toString(), transparentColor) }
                                itemMassage2?.setOwnMassage(true)

                                itemMassage.add(itemMassage2!!)
                        }
                        else{
                            val transparentColor = Color.argb(0, 255, 0, 0)

                            val itemMassage3 = mass?.text?.let { ItemMassage(it, false,
                                    ds.key.toString(), transparentColor
                                ) }
                                itemMassage3?.setOwnMassage(false)
                                itemMassage.add(itemMassage3!!)

                    }
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

}


