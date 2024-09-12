package com.example.zenith;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String SAVED_TEXT = "savedtext";
    ImageButton signin, signup;
    private FirebaseAuth mfirebaseAuth;
    SharedPreferences spref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mfirebaseAuth.getCurrentUser();
        if(currentuser != null){
            Intent intent = new Intent(MainActivity.this, ChartListActivity.class);
            startActivity(intent);
        }
    }

    void init(){
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        mfirebaseAuth = FirebaseAuth.getInstance();
    }
}