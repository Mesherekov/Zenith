package com.example.zenith;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mfirebaseAuth;
    private EditText edemail, edname, edpassword;
    private Button bsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        bsignup.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(edname.getText().toString()) && !TextUtils.isEmpty(edemail.getText().toString()) && !TextUtils.isEmpty(edpassword.getText().toString())){
                mfirebaseAuth.createUserWithEmailAndPassword(edemail.getText().toString(), edpassword.getText().toString()).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        SendEmailMassageVerification();
                        Toast.makeText(getApplicationContext(), "User Sign Up", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = mfirebaseAuth.getCurrentUser();
                        assert user != null;
                        if(user.isEmailVerified()){
                            Intent intent = new Intent(SignUpActivity.this, ChartListActivity.class);
                            startActivity(intent);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "User Sign Up Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SignUpActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mfirebaseAuth.getCurrentUser();
        if(currentuser != null){
            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        edemail = findViewById(R.id.emailup);
        edname = findViewById(R.id.nameup);
        edpassword = findViewById(R.id.passup);
        bsignup = findViewById(R.id.createaccount);
        mfirebaseAuth = FirebaseAuth.getInstance();
    }
    private void SendEmailMassageVerification(){
        FirebaseUser user = mfirebaseAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Confirm your email address", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Send massage failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}