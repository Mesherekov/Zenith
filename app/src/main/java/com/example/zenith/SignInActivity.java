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

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mfirebaseAuth;
    private EditText edemail, edpassword;
    private Button bsignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
        bsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(edemail.getText().toString()) && !TextUtils.isEmpty(edpassword.getText().toString())) {
                    mfirebaseAuth.signInWithEmailAndPassword(edemail.getText().toString(), edpassword.getText().toString()).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(SignInActivity.this, ChartListActivity.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(SignInActivity.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(SignInActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void init(){
        edemail = findViewById(R.id.emailin);
        edpassword = findViewById(R.id.passin);
        bsignin = findViewById(R.id.enterac);
        mfirebaseAuth = FirebaseAuth.getInstance();
    }
}