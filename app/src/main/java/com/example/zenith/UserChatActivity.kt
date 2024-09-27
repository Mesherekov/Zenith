package com.example.zenith

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.zenith.R.id.friendname
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class UserChatActivity : AppCompatActivity() {
    lateinit var name: TextView
    lateinit var userImage: ImageView
    lateinit var backImagee: ImageButton
    lateinit var sendmassage : ImageButton
    lateinit var ownmassage : EditText
    private val MASSAGE_KEY = "Massage"
    private lateinit var mfireauth : FirebaseAuth
    private lateinit var mdatabase : DatabaseReference
    private lateinit var massages: Massages
    lateinit var itemMassage: MutableList<ItemMassage>
    lateinit var recyclermassageView: RecyclerView
    lateinit var customMassageAdapter: CustomMassageAdapter
    lateinit var currentuser: FirebaseUser
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chat)
        name = findViewById(friendname)
        sendmassage = findViewById(R.id.sendmassage)
        ownmassage = findViewById(R.id.ownmassage)
        userImage = findViewById(R.id.friendimage)
        backImagee = findViewById(R.id.back)
        backImagee = findViewById(R.id.back)
        recyclermassageView = findViewById(R.id.recyclermassage)
        recyclermassageView.layoutManager = LinearLayoutManager(this)
        mfireauth = FirebaseAuth.getInstance()
        itemMassage = mutableListOf()
        customMassageAdapter = CustomMassageAdapter(applicationContext, itemMassage)
        recyclermassageView.adapter = customMassageAdapter
        currentuser = mfireauth.currentUser!!

        var friendUID:String = "45"
        if (intent != null){
            val bitmap = intent.getByteArrayExtra("byteArray")
                ?.let { BitmapFactory.decodeByteArray(intent.getByteArrayExtra("byteArray"), 0, it.size) }
            userImage.setImageBitmap(bitmap)
            friendUID = intent.getStringExtra("UID").toString()
            name.text = intent.getStringExtra("NameUser")
            mdatabase = FirebaseDatabase.getInstance().getReference(MASSAGE_KEY).child((currentuser?.uid.hashCode()+friendUID.hashCode()).toString())

            getData(friendUID)
            if(name.text.length>12){
                name.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                userImage.layoutParams.height = 75*3
                userImage.layoutParams.width = 75*3
            }
        }

        sendmassage.setOnClickListener{
            val id = mdatabase.key
            if (currentuser != null) {
                massages = Massages(id, ownmassage.text.toString(), currentuser.uid, currentuser.uid, friendUID)
                mdatabase.push().setValue(massages)

            }

        }

        backImagee.setOnClickListener {
            finish()
        }

    }
    fun getData(friendUID: String){
        val vlistener = object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
               val massagesgd = snapshot.children
                if(itemMassage.size > 0) itemMassage.clear()
                var b : Boolean
                massagesgd.forEach{ds: DataSnapshot? ->
                    val mass = ds?.getValue(Massages::class.java)
                        if (currentuser.uid.hashCode() == mass?.ownUID.hashCode()){
                            b = true
                            val itemMassage2 = mass?.text?.let { ItemMassage(it, b) }
                            if (itemMassage2 != null) {
                                itemMassage.add(itemMassage2)
                            }
                        }
                        else{
                            b = false
                            val itemMassage3 = mass?.text?.let { ItemMassage(it, b) }
                            if (itemMassage3 != null) {
                                itemMassage.add(itemMassage3)
                            }
                    }
                }
                customMassageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }
        mdatabase.addValueEventListener(vlistener)
        recyclermassageView.layoutManager = LinearLayoutManager(this)
        recyclermassageView.adapter = customMassageAdapter
    }
}


