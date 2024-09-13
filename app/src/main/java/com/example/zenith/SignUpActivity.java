package com.example.zenith;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mfirebaseAuth;
    private EditText edemail, edname, edpassword;
    private Button bsignup, next;
    private DatabaseReference mdatabase;
    private String USER_KEY = "User";
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
                mfirebaseAuth.signInWithEmailAndPassword(edemail.getText().toString(), edpassword.getText().toString()).addOnCompleteListener(SignUpActivity.this, task -> {
                    if(task.isSuccessful()){
                        String id = mdatabase.getKey();
                        String name = edname.getText().toString();
                        String email = edemail.getText().toString();
                        String password = edpassword.getText().toString();
                        User user = new User(id, name, email, password);
                        mdatabase.push().setValue(user);
                        FirebaseUser currentUser = mfirebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        if(currentUser.isEmailVerified()){
                            Intent intent = new Intent(SignUpActivity.this, ChartListActivity.class);
                            startActivity(intent);
                        }
                        else Toast.makeText(this, "Please confirm your email", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SignUpActivity.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                });
            }else {
                Toast.makeText(SignUpActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void init(){
        edemail = findViewById(R.id.emailup);
        edname = findViewById(R.id.nameup);
        edpassword = findViewById(R.id.passup);
        bsignup = findViewById(R.id.createaccount);
        next = findViewById(R.id.next);
        mfirebaseAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
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