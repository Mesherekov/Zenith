package com.example.zenith;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChartListActivity extends AppCompatActivity {
    private TextView email, name;
    ImageButton logout;
    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference mdatabase;
    private String USER_KEY = "User";
    FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_list);
        init();
        currentuser = mfirebaseAuth.getCurrentUser();
        assert currentuser != null;
        email.setText(currentuser.getEmail());
        logout.setOnClickListener(view -> {
            mfirebaseAuth.signOut();
            Intent intent = new Intent(ChartListActivity.this, MainActivity.class);
            startActivity(intent);
        });
        getData();
    }
    private void init(){
        email = findViewById(R.id.ema);
        logout = findViewById(R.id.logout);
        name = findViewById(R.id.namechar);
        mfirebaseAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
    private void getData(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    if(user.email.equals(currentuser.getEmail())){
                        name.setText(user.name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mdatabase.addValueEventListener(vListener);
    }
}