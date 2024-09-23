package com.example.zenith

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.zenith.R.id.friendname
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
        mdatabase = FirebaseDatabase.getInstance().getReference(MASSAGE_KEY)
        mfireauth = FirebaseAuth.getInstance()
        var friendUID:String = "45"
        if (intent != null){
            var bitmap = intent.getByteArrayExtra("byteArray")
                ?.let { BitmapFactory.decodeByteArray(intent.getByteArrayExtra("byteArray"), 0, it.size) }
            userImage.setImageBitmap(bitmap)
            friendUID = intent.getStringExtra("UID").toString()
            name.text = intent.getStringExtra("NameUser")
        }

        sendmassage.setOnClickListener{
            val id = mdatabase.key
            val currentuser = mfireauth.currentUser
            if (currentuser != null) {
                massages = Massages(id, ownmassage.text.toString(), currentuser.uid, currentuser.uid, friendUID)
                mdatabase.push().setValue(massages)
            }

        }

        backImagee.setOnClickListener {
            finish()
        }

    }


}


