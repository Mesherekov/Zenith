package com.example.zenith;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChartListActivity extends AppCompatActivity {
    private TextView email;
    ImageButton logout;
    private FirebaseAuth mfirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_list);
        init();
        FirebaseUser currentuser = mfirebaseAuth.getCurrentUser();
        assert currentuser != null;
        email.setText(currentuser.getEmail());
        logout.setOnClickListener(view -> {
            mfirebaseAuth.signOut();
            Intent intent = new Intent(ChartListActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
    private void init(){
        email = findViewById(R.id.ema);
        logout = findViewById(R.id.logout);
        mfirebaseAuth = FirebaseAuth.getInstance();
    }
}