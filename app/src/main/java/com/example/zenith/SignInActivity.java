package com.example.zenith;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mfirebaseAuth;
    private EditText edemail, edpassword;
    private Button bsignin;
    private MediaPlayer mp3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
        bsignin.setOnClickListener(view -> {
            mp3.start();
            if(!TextUtils.isEmpty(edemail.getText().toString()) && !TextUtils.isEmpty(edpassword.getText().toString())) {
                mfirebaseAuth.signInWithEmailAndPassword(edemail.getText().toString(), edpassword.getText().toString()).addOnCompleteListener(SignInActivity.this, task -> {
                    if(task.isSuccessful()){
                        FirebaseUser user = mfirebaseAuth.getCurrentUser();
                        assert user != null;
                        if(user.isEmailVerified()){
                            Intent intent = new Intent(SignInActivity.this, ChartListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else Toast.makeText(this, "Please confirm your email", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SignInActivity.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                });
            } else Toast.makeText(SignInActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
        });
    }
    private void init(){
        edemail = findViewById(R.id.emailin);
        edpassword = findViewById(R.id.passin);
        bsignin = findViewById(R.id.enterac);
        mp3 = MediaPlayer.create(this, R.raw.mouse);
        mfirebaseAuth = FirebaseAuth.getInstance();
    }
}