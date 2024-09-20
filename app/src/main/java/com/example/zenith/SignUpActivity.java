package com.example.zenith;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mfirebaseAuth;
    private EditText edemail, edname, edpassword;
    private Button bsignup, next;
    private DatabaseReference mdatabase;
    private String USER_KEY = "User";
    private boolean applynext;
    private ImageView im;
    private StorageReference mstorage;
    private Uri uploaduri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        bsignup.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(edname.getText().toString()) && !TextUtils.isEmpty(edemail.getText().toString()) && !TextUtils.isEmpty(edpassword.getText().toString())) {
                if (edpassword.getText().toString().length() >= 6) {
                    mfirebaseAuth.createUserWithEmailAndPassword(edemail.getText().toString(), edpassword.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            SendEmailMassageVerification();
                            Toast.makeText(getApplicationContext(), "User Sign Up", Toast.LENGTH_SHORT).show();
                            applynext = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "User Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else Toast.makeText(this, "Your password must have at least 6 characters", Toast.LENGTH_SHORT).show();
            }
                else {
                Toast.makeText(SignUpActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
            }

        });
        next.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(edname.getText().toString()) && !TextUtils.isEmpty(edemail.getText().toString()) && !TextUtils.isEmpty(edpassword.getText().toString())) {
                if (applynext) {
                    mfirebaseAuth.signInWithEmailAndPassword(edemail.getText().toString(), edpassword.getText().toString()).addOnCompleteListener(SignUpActivity.this, task -> {
                        if (task.isSuccessful()) {
                            String id = mdatabase.getKey();
                            String name = edname.getText().toString();
                            String email = edemail.getText().toString();
                            String password = edpassword.getText().toString();
                            uploadImage();
                            User user = new User(id, name, email, password, uploaduri.toString());
                            mdatabase.push().setValue(user);
                            FirebaseUser currentUser = mfirebaseAuth.getCurrentUser();
                            assert currentUser != null;
                            if (currentUser.isEmailVerified()) {
                                Intent intent = new Intent(SignUpActivity.this, ChartListActivity.class);
                                startActivity(intent);
                            } else
                                Toast.makeText(this, "Please confirm your email", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(SignUpActivity.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                    });
                } else Toast.makeText(this, "This account has already been created, please sign in", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
                }
        });
    }


    private void uploadImage(){
        Bitmap bitmap =((BitmapDrawable) im.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        final StorageReference mref = mstorage.child(System.currentTimeMillis()+ "user_image");
        UploadTask uptask = mref.putBytes(bytes);
        Task<Uri> task = uptask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploaduri = task.getResult();
            }
        });
    }
    private void init(){
        edemail = findViewById(R.id.emailup);
        edname = findViewById(R.id.nameup);
        edpassword = findViewById(R.id.passup);
        bsignup = findViewById(R.id.createaccount);
        next = findViewById(R.id.next);
        mstorage = FirebaseStorage.getInstance().getReference("ImageDB");
        mfirebaseAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        applynext = false;
        im = findViewById(R.id.imageView2);
    }
    private void SendEmailMassageVerification(){
        FirebaseUser user = mfirebaseAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(SignUpActivity.this, "Confirm your email address", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(SignUpActivity.this, "Send massage failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}