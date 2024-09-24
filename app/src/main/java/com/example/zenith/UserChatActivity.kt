package com.example.zenith

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.zenith.R.id.friendname
import com.google.firebase.auth.FirebaseAuth
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

        mdatabase = FirebaseDatabase.getInstance().getReference(MASSAGE_KEY)
        mfireauth = FirebaseAuth.getInstance()
        itemMassage = mutableListOf()
        customMassageAdapter = CustomMassageAdapter(applicationContext, itemMassage)

        var friendUID:String = "45"
        if (intent != null){
            val bitmap = intent.getByteArrayExtra("byteArray")
                ?.let { BitmapFactory.decodeByteArray(intent.getByteArrayExtra("byteArray"), 0, it.size) }
            userImage.setImageBitmap(bitmap)
            friendUID = intent.getStringExtra("UID").toString()
            name.text = intent.getStringExtra("NameUser")
            if(name.text.length>8){
                name.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                userImage.layoutParams.height = 75*3
                userImage.layoutParams.width = 75*3
            }
        }

        sendmassage.setOnClickListener{
            val id = mdatabase.key
            val currentuser = mfireauth.currentUser
            if (currentuser != null) {
                massages = Massages(id, ownmassage.text.toString(), currentuser.uid, currentuser.uid, friendUID)
                mdatabase.child(currentuser.uid+friendUID).push().setValue(massages)

            }

        }

        backImagee.setOnClickListener {
            finish()
        }

    }
    fun getData(friendUID: String){
        val vlistener = object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
               val massagesgd = snapshot.children
                massagesgd.forEach{ds: DataSnapshot? ->
                    val mass = ds?.getValue<Massages>()
                    val currentuser = mfireauth.currentUser
                    if (((currentuser?.uid + friendUID) == ds?.key) or ((friendUID + currentuser?.uid) == ds?.key)){
                        if (currentuser?.uid == mass?.ownUID){
                            val add =
                                mass?.text?.let { itemMassage.add(object : ItemMassage(it, true){}) }
                        }
                        else{
                            val add =
                                mass?.text?.let { itemMassage.add(object : ItemMassage(it, false){}) }
                        }
                    }
                }
                customMassageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }


    }
}


