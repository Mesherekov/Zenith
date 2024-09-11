package com.example.zenith;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private static final String SAVED_TEXT = "savedtext";
    ImageButton signin, signup;
    boolean isfirst;
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
    void init(){
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
    } void loadbool(){
        spref = getPreferences(MODE_PRIVATE);
        isfirst = spref.getBoolean(SAVED_TEXT, false);
    }
    void savebool(){
        spref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = spref.edit();
        editor.putBoolean(SAVED_TEXT, isfirst);
        editor.apply();
    }

}